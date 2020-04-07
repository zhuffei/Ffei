package com.example.zhuffei.ffei.adapter;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.tool.Tool;

import java.util.List;

/**
 * Created by Mr.Dong on 2019/3/10.
 */

public class GoodsItemAdapter extends RecyclerView.Adapter<GoodsItemAdapter.GoodsViewHolder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据集合
     */
    private List<Goods> data;

    public GoodsItemAdapter(List<Goods> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false);
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoodsViewHolder holder, int position) {
        //将数据设置到item上
        Goods goods = data.get(position);
        holder.price.setText(goods.getPrice()+"");
        holder.name.setText(goods.getName());
        holder.goodsImage.setImageResource(goods.getImg());
        holder.setImgHeigh(mContext);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImage;
        TextView name;
        TextView price;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            goodsImage = (ImageView) itemView.findViewById(R.id.image_item);
            name = (TextView) itemView.findViewById(R.id.goods_name);
            price = (TextView)itemView.findViewById(R.id.goods_price);
        }

        public void setImgHeigh(Context context){
            goodsImage.setMinimumWidth(Tool.getScreenWidth(context)/2-10);

        }
    }
}