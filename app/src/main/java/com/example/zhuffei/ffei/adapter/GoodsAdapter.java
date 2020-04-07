package com.example.zhuffei.ffei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.Goods;
import com.lidong.photopicker.Image;

import java.util.List;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/3 15:43
 */
public class GoodsAdapter extends BaseAdapter{

    private List<Goods> goodsList;

    private Context context;


    public GoodsAdapter(List<Goods> goodsList, Context context) {
        this.goodsList = goodsList;
        this.context = context;
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
        View view = LayoutInflater.from(context).inflate(R.layout.my_goods_item,null);
        ImageView img = view.findViewById(R.id.goodsImg);
        TextView goodsName = view.findViewById(R.id.goodsName);
        TextView goodsPrice = view.findViewById(R.id.goodsPrice);
        TextView goodsState = view.findViewById(R.id.goodsState);
        Goods goods = goodsList.get(position);
        img.setImageResource(goods.getImg());
        goodsName.setText(goods.getName());
        goodsPrice.setText(goods.getPrice()+"");
        goodsState.setText(goods.getState());
        return  view;
    }
}