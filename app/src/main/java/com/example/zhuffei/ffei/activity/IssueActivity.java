package com.example.zhuffei.ffei.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.tool.EditInputFilter;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.UrlTool;
import com.example.zhuffei.ffei.view.LoadingViewManager;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/3/24 13:11
 */
public class IssueActivity extends BaseActivity {

    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private ArrayList<String> imagePaths = new ArrayList<>();

    private GridView gridView;
    private GridAdapter gridAdapter;
    private Button mButton;
    private EditText title, describe, location, price;
    private String TAG = IssueActivity.class.getSimpleName();
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);
        findViews();
        InputFilter[] filters = {new EditInputFilter()};
        price.setFilters(filters);
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);

        // preview
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            String imgs = (String) parent.getItemAtPosition(position);
            if ("000000".equals(imgs)) {
                PhotoPickerIntent intent = new PhotoPickerIntent(IssueActivity.this);
                intent.setSelectModel(SelectModel.MULTI);
                intent.setShowCarema(true); // 是否显示拍照
                intent.setMaxTotal(6); // 最多选择照片数量，默认为6
                intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                startActivityForResult(intent, REQUEST_CAMERA_CODE);
            } else {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(IssueActivity.this);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);
            }
        });
        imagePaths.add("000000");
        gridAdapter = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);

        //开始商品信息上传
        mButton.setOnClickListener(v -> {
            if (title.getText().toString().isEmpty() ||
                    describe.getText().toString().isEmpty() ||
                    location.getText().toString().isEmpty() ||
                    price.getText().toString().isEmpty()) {
                ToastHelper.showToast("请填写完整信息");
                return;
            }
            if (imagePaths.size() < 2) {
                ToastHelper.showToast("请上传至少一张图片");
                return;
            }
            imagePaths.remove("000000");
            upLoad(imagePaths);

        });
        back.setOnClickListener(v -> IssueActivity.this.finish());

    }

    private void upLoad(List<String> list) {
        //开启加载动画
        LoadingViewManager.with(IssueActivity.this).setHintText("请稍后").setMinAnimTime(1500).build();

        //获取已登录用户的id
        int uId = this.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("uId", 0);
        if (uId == 0) {
            ToastHelper.showToast("登录失效，请重新登录");
            return;
        }
        File file;
        if (list != null) {
            OkHttpClient okHttpClient = new OkHttpClient();
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.MIXED);
            double ratio = 0;
            //不加载图片到内存而获取其宽高比
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(list.get(0), options);
            ratio = (double) options.outHeight / options.outWidth;

            for (int i = 0; i < list.size(); i++) {
                file = new File(list.get(i));
                if (null != file) {
                    builder.addFormDataPart("img", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
                }
            }

            builder.addFormDataPart("title", title.getText().toString());
            builder.addFormDataPart("type", "1");//1代表是出售的商品
            builder.addFormDataPart("uId", String.valueOf(uId));
            builder.addFormDataPart("describe", describe.getText().toString());
            builder.addFormDataPart("location", location.getText().toString());
            builder.addFormDataPart("price", price.getText().toString());
            builder.addFormDataPart("ratio", ratio + "");
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(UrlTool.ADDGOODS)
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(IssueActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    Map map = JSON.parseObject(json, HashMap.class);
                    if ((boolean) map.get("state")) {
                        runOnUiThread(() -> {
                            Toast.makeText(IssueActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(IssueActivity.this, HomeActivity.class));
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(IssueActivity.this, "发布失败，请稍后再试", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });

        }
    }


    public void findViews() {
        back = findViewById(R.id.back);
        gridView = findViewById(R.id.gridView);
        mButton = findViewById(R.id.button);
        title = findViewById(R.id.title);
        describe = findViewById(R.id.describe);
        location = findViewById(R.id.location);
        price = findViewById(R.id.price);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d(TAG, "list: " + "list = [" + list.size());
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    Log.d(TAG, "ListExtra: " + "ListExtra = [" + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        paths.remove("000000");
        paths.add("000000");
        imagePaths.addAll(paths);
        gridAdapter = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);
        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            if (listUrls.size() == 7) {
                listUrls.remove(listUrls.size() - 1);
            }
            inflater = LayoutInflater.from(IssueActivity.this);
        }

        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_image, parent, false);
                holder.image = convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final String path = listUrls.get(position);
            if (path.equals("000000")) {
                holder.image.setImageResource(R.mipmap.plusimg);
            } else {
                Glide.with(IssueActivity.this)
                        .load(path)
                        .placeholder(R.mipmap.default_error)
                        .error(R.mipmap.default_error)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.image);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView image;
        }
    }
}
