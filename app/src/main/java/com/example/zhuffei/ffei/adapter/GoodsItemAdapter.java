package com.example.zhuffei.ffei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.Goods;
import com.example.zhuffei.ffei.tool.Tool;

import java.util.List;

/**
 * Created by Mr.Dong on 2019/3/10.
 */

public class GoodsItemAdapter extends RecyclerView.Adapter<GoodsItemAdapter.GoodsViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据集合
     */
    private List<Goods> data;

    private View mHeaderView;


    public GoodsItemAdapter(List<Goods> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        if (mHeaderView != null && viewType == TYPE_HEADER) return new GoodsViewHolder(mHeaderView);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false);
        return new GoodsViewHolder(view);
    }

    //    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//        if(manager instanceof GridLayoutManager) {
//            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
//            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    Log.d("aaaaaaaaaaa",(getItemViewType(position) == TYPE_HEADER
//                            ? gridManager.getSpanCount() : 1) +"");
//                    return getItemViewType(position) == TYPE_HEADER
//                            ? gridManager.getSpanCount() : 1;
//                }
//            });
//        }
//    }
    @Override
    public void onBindViewHolder(GoodsViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
        //将数据设置到item上
        Goods goods = data.get(pos);
        holder.price.setText(goods.getPrice() + "");
        holder.name.setText(goods.getName());
        holder.goodsImage.setImageResource(goods.getImg());
        holder.setImgHeigh(mContext);
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImage;
        TextView name;
        TextView price;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            goodsImage = itemView.findViewById(R.id.image_item);
            name = itemView.findViewById(R.id.goods_name);
            price = itemView.findViewById(R.id.goods_price);
        }

        public void setImgHeigh(Context context) {
            goodsImage.setMinimumWidth(Tool.getScreenWidth(context) / 2 - 10);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) return TYPE_NORMAL;
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        StaggeredGridLayoutManager.LayoutParams layoutParams =
                new StaggeredGridLayoutManager.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setFullSpan(true);
        mHeaderView.setLayoutParams(layoutParams);
        notifyItemInserted(0);
    }

    public View getHeaderView() {
        return mHeaderView;
    }
}