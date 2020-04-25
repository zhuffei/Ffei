package com.example.zhuffei.ffei.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.MyGoodsActivity;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.HttpUtil;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/3 15:43
 */
public class GoodsAdapter extends BaseAdapter {

    private List<Goods> goodsList;

    private Context context;

    private AsyncImageLoader asyncImageLoader;

    private OnclickListener onclickListener;

    private int code;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 1:
                    if ((int) msg.obj == 1) {
                        ToastHelper.showToast("已确认");
                        MyGoodsActivity myGoodsActivity = (MyGoodsActivity)context;
                        myGoodsActivity.getData();
                    } else {
                        ToastHelper.showToast("操作失败");
                    }
                    break;
            }
        }
    };


    public GoodsAdapter(List<Goods> goodsList, Context context, int code) {
        this.goodsList = goodsList;
        this.context = context;
        this.code = code;
        asyncImageLoader = new AsyncImageLoader(context);
    }

    public void setOnclickListener(OnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return goodsList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_goods_item, null);
        Button confirm = view.findViewById(R.id.confirm);
        ImageView img = view.findViewById(R.id.goodsImg);
        TextView goodsName = view.findViewById(R.id.goodsName);
        TextView goodsPrice = view.findViewById(R.id.goodsPrice);
        TextView goodsState = view.findViewById(R.id.goodsState);
        Goods goods = goodsList.get(position);
        view.setOnClickListener(v -> {
            GoodsAdapter.this.onclickListener.onClick(goods.getId()
            );
        });
        if (goods.getType() == 2) {
            img.setImageResource(R.mipmap.qiugou);
            goodsName.setText(goods.getName());
        } else {
            asyncImageLoader.asyncloadImage(img, UrlTool.GOODSIMG + goods.getImg1());
            goodsName.setText(goods.getName());
        }
        if ((goods.getState() == 2 || goods.getState() == 3) && FfeiApplication.user.getId() == goods.getuId() && (code == 1 || code == 4)) {
            confirm.setVisibility(View.VISIBLE);
            confirm.setText("确认发货");
            confirm.setOnClickListener(v -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("确定已将商品交付买家？");
                dialog.setTitle("发货确认");
                dialog.setNegativeButton("取消", (dialog1, which) -> {
                    dialog1.dismiss();
                });
                dialog.setPositiveButton("确定", (dialog1, which) -> {
                    changeState(goods.getId(), 4);
                });
                dialog.show();
            });
        }
        if ((goods.getState() == 2 || goods.getState() == 4) && FfeiApplication.user.getId() != goods.getuId() && code == 3) {
            confirm.setVisibility(View.VISIBLE);
            confirm.setText("确认收货");
            confirm.setOnClickListener(v -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("确定已收到商品？");
                dialog.setTitle("收货确认");
                dialog.setNegativeButton("取消", (dialog1, which) -> {
                    dialog1.dismiss();
                });
                dialog.setPositiveButton("确定", (dialog1, which) -> {
                    changeState(goods.getId(), 3);
                });
                dialog.show();
            });
        }
        goodsPrice.setText(String.format("%.2f", goods.getPrice()));
        goodsState.setText(Tool.getState(goods.getState(), code));
        return view;
    }

    public void changeState(int gid, int state) {
        Map<String, Integer> map = new HashMap<>();
        map.put("gid", gid);
        map.put("state", state);
        String param = JSON.toJSONString(map);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), param);
        HttpUtil.sendHttpRequest(UrlTool.CHANGESTATE, requestBody, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                handler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Map map = JSON.parseObject(json, HashMap.class);
                Message msg = new Message();
                msg.obj = (Integer) map.get("data");
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
    }

    public interface OnclickListener {
        void onClick(int gid);
    }
}