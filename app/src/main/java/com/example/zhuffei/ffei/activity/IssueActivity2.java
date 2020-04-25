package com.example.zhuffei.ffei.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.tool.EditInputFilter;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.UrlTool;
import com.example.zhuffei.ffei.view.LoadingViewManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/6 11:55
 */
public class IssueActivity2 extends BaseActivity {
    private Button mButton;
    private EditText title, describe, location, price;
    private ImageView back;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 0:
                    if ((Integer) msg.obj == 1) {
                        ToastHelper.showToast("上传成功，等待审核");
                    } else {
                        ToastHelper.showToast("上传失败");
                    }
                    break;
            }
            LoadingViewManager.dismiss();
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue2);
        findViews();
        //判断和申请权限
        InputFilter[] filters = {new EditInputFilter()};
        price.setFilters(filters);
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;


        //开始商品信息上传
        mButton.setOnClickListener(v -> {
            if (title.getText().toString().isEmpty() ||
                    describe.getText().toString().isEmpty() ||
                    location.getText().toString().isEmpty() ||
                    price.getText().toString().isEmpty()) {
                ToastHelper.showToast("请填写完整信息");
                return;
            }
            upload();
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IssueActivity2.this.finish();
            }
        });

    }

    public void findViews() {
        back = findViewById(R.id.back);
        mButton = findViewById(R.id.button);
        title = findViewById(R.id.title);
        describe = findViewById(R.id.describe);
        location = findViewById(R.id.location);
        price = findViewById(R.id.price);
    }

    public void upload() {
        //开启加载动画
        LoadingViewManager.with(IssueActivity2.this).setHintText("请稍后").setMinAnimTime(1500).build();
        Goods goods = new Goods();
        goods.setName("【求购】"+title.getText().toString());
        goods.setDes(describe.getText().toString());
        goods.setLocation(location.getText().toString());
        goods.setPrice(Double.valueOf(price.getText().toString()));
        goods.setuId(FfeiApplication.user.getId());
        String param = JSON.toJSONString(goods);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.ADDBUY, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                String responseBody = response.body().string();
                Map<String, Object> map = JSON.parseObject(responseBody);
                int s = (int) map.get("data");
                message.obj = s;
                handler.sendMessage(message);
            }
        });
    }


}
