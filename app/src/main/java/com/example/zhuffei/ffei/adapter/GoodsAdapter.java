package com.example.zhuffei.ffei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.UrlTool;

import java.util.List;

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


    public GoodsAdapter(List<Goods> goodsList, Context context) {
        this.goodsList = goodsList;
        this.context = context;
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

        ImageView img = view.findViewById(R.id.goodsImg);
        TextView goodsName = view.findViewById(R.id.goodsName);
        TextView goodsPrice = view.findViewById(R.id.goodsPrice);
        TextView goodsState = view.findViewById(R.id.goodsState);
        Goods goods = goodsList.get(position);
        view.setOnClickListener(v->{
            GoodsAdapter.this.onclickListener.onClick(goods.getId());
        });
        if (goods.getType() == 2) {
            img.setImageResource(R.mipmap.qiugou);
            goodsName.setText("【求购】" + goods.getName());
        } else {
            asyncImageLoader.asyncloadImage(img, UrlTool.GOODSIMG + goods.getImg1());
            goodsName.setText(goods.getName());
        }
        goodsPrice.setText(goods.getPrice() + "");
        goodsState.setText(goods.getState()+"");
        return view;
    }

   public  interface OnclickListener{
        void onClick(int gid);
    }
}