package com.example.zhuffei.ffei.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.CropActivity;
import com.example.zhuffei.ffei.activity.FocusActivity;
import com.example.zhuffei.ffei.activity.LoginActivity;
import com.example.zhuffei.ffei.activity.MyGoodsActivity;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.FileUtil;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;
import com.leon.lib.settingview.LSettingItem;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_CANCELED;


/**
 * 我的Fragment
 */
public class WDFragment extends BaseFragment {

    private Context mContext;

    private TextView focusNum, fansNum;

    private ImageView avator;
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;


    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";


    Dialog dialog;
    LSettingItem itemOne, itemTwo, itemThree, itemFour;

    LinearLayout focus, fans;

    TextView cancel, local, camera, userName, phone;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 0:
                    Map<String, Integer> map = (Map<String, Integer>) msg.obj;
                    fansNum.setText(map.get("fans") + "");
                    focusNum.setText(map.get("focus") + "");
                    break;
                case 1:
                    ToastHelper.showToast("头像上传成功");
                    break;
                case 2:
                    ToastHelper.showToast("头像上传失败");
                    break;

            }

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
        findViews(view);
        mContext = this.getActivity();
        setListener();
        countFocusAndFans();
        setUser();
        return view;
    }

    private void countFocusAndFans() {
        if (!FfeiApplication.isLogin) return;
        Map<String, Integer> map = new HashMap<>();
        map.put("uid", FfeiApplication.user.getId());
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.COUNTFOCUSANDFANS, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Map<String, Object> map = JSON.parseObject(responseBody);
                JSONObject obj = (JSONObject) map.get("data");
                Map<String, Integer> map1 = JSONObject.parseObject(obj.toJSONString(), new TypeReference<Map<String, Integer>>() {
                });
                Message msg = new Message();
                msg.obj = map1;
                msg.what = 0;
                handler.sendMessage(msg);
            }
        });


    }

    private void setListener() {
        avator.setOnClickListener(v -> {
            if (FfeiApplication.isLogin) {
                dialog = new Dialog(mContext, R.style.BottomDialogStyle);
                View view1 = View.inflate(mContext, R.layout.activity_avator, null);
                dialog.setContentView(view1);
                dialog.setCanceledOnTouchOutside(true);
                view1.setMinimumHeight((int) (Tool.getScreenHeight(mContext) * 0.2f));
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = Tool.getScreenWidth(mContext);
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.BOTTOM;
                dialogWindow.setAttributes(lp);
                cancel = view1.findViewById(R.id.cancle);
                local = view1.findViewById(R.id.local);
                camera = view1.findViewById(R.id.camera);
                cancel.setOnClickListener(v13 -> dialog.dismiss());
                local.setOnClickListener(v12 -> choseHeadImageFromGallery());
                camera.setOnClickListener(v1 -> choseHeadImageFromCameraCapture());
                dialog.show();
            } else {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
        itemOne.setmOnLSettingItemClick(() -> {
            if (!FfeiApplication.isLogin) {
                ToastHelper.showToast("请登录");
                return;
            }
            Intent intent = new Intent(mContext, MyGoodsActivity.class);
            intent.putExtra("code", MyGoodsActivity.RELEASE);
            startActivity(intent);
        });
        itemTwo.setmOnLSettingItemClick(() -> {
            if (!FfeiApplication.isLogin) {
                ToastHelper.showToast("请登录");
                return;
            }
            Intent intent = new Intent(mContext, MyGoodsActivity.class);
            intent.putExtra("code", MyGoodsActivity.COLLECTION);
            startActivity(intent);
        });
        itemThree.setmOnLSettingItemClick(() -> {
            if (!FfeiApplication.isLogin) {
                ToastHelper.showToast("请登录");
                return;
            }
            Intent intent = new Intent(mContext, MyGoodsActivity.class);
            intent.putExtra("code", MyGoodsActivity.BUY);
            startActivity(intent);
        });
        itemFour.setmOnLSettingItemClick(() -> {
            if (!FfeiApplication.isLogin) {
                ToastHelper.showToast("请登录");
                return;
            }
            Intent intent = new Intent(mContext, MyGoodsActivity.class);
            intent.putExtra("code", MyGoodsActivity.SELL);
            startActivity(intent);
        });

        focus.setOnClickListener(v -> {
            if (!FfeiApplication.isLogin) {
                ToastHelper.showToast("请登录");
                return;
            }
            Intent intent = new Intent(mContext, FocusActivity.class);
            intent.putExtra("code", FocusActivity.FOCUS);
            startActivityForResult(intent, 1);
        });
        fans.setOnClickListener(v -> {
            if (!FfeiApplication.isLogin) {
                ToastHelper.showToast("请登录");
                return;
            }
            Intent intent = new Intent(mContext, FocusActivity.class);
            intent.putExtra("code", FocusActivity.FANS);
            startActivityForResult(intent, 1);
        });
    }

    private void findViews(View view) {
        phone = view.findViewById(R.id.phone);
        avator = view.findViewById(R.id.avator);
        itemOne = view.findViewById(R.id.item_one);
        itemTwo = view.findViewById(R.id.item_two);
        itemThree = view.findViewById(R.id.item_three);
        itemFour = view.findViewById(R.id.item_four);
        focus = view.findViewById(R.id.focus);
        fans = view.findViewById(R.id.fans);
        userName = view.findViewById(R.id.userName);
        focusNum = view.findViewById(R.id.focusNum);
        fansNum = view.findViewById(R.id.fansNum);
    }


    //展示已登录用户的信息
    public void setUser() {
        SharedPreferences sp = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        String img = sp.getString("img", "");
        String phoneNumber = sp.getString("phone", "");

        if (null != name && !name.isEmpty()) {
            phoneNumber = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11);
            userName.setText(name);
            phone.setText(phoneNumber);
            AsyncImageLoader asyncImageLoader = new AsyncImageLoader(mContext);
            asyncImageLoader.asyncloadImage(avator, UrlTool.prefix + "avator/" + img);
        }

    }

    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    //启动相机拍照
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(mContext, "com.example.zhuffei.ffei.fileprovider", new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        // 有存储的SDCard
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {
        if (resultCode == 1) {
            countFocusAndFans();
            return;
        }
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(mContext, "操作取消", Toast.LENGTH_LONG).show();
            if (null != dialog) {
                dialog.dismiss();
            }
            return;
        }
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(mContext, "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {
        Intent intent = new Intent(mContext, CropActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {

        if (null != Tool.bitmap) {
            File file = FileUtil.getFile(Tool.bitmap);
            uploadAvator(file);
            avator.setImageBitmap(Tool.bitmap);
        }
        if (null != dialog) {
            dialog.dismiss();
        }
    }

    private void uploadAvator(File file) {
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.MIXED);
        builder.addFormDataPart("img", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file));
        builder.addFormDataPart("uid", String.valueOf(FfeiApplication.user.getId()));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(UrlTool.CHANGEAVATOR)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Map map = JSON.parseObject(json, HashMap.class);
                if ((boolean) map.get("state")) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        });
    }


    @Override
    protected void setTitle(TextView tvTitle) {

    }


}
