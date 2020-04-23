package com.example.zhuffei.ffei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.GoodsUserVO;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private List<GoodsUserVO> data;

    private AsyncImageLoader asyncImageLoader;

    private View mHeaderView;


    public GoodsItemAdapter(List<GoodsUserVO> data, Context context) {
        this.data = data;
        this.mContext = context;
        asyncImageLoader = new AsyncImageLoader(context);
    }

    @Override
    public GoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        if (mHeaderView != null && viewType == TYPE_HEADER) return new GoodsViewHolder(mHeaderView);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false);
        return new GoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoodsViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) return;
        final int pos = getRealPosition(holder);
        //将数据设置到item上
        GoodsUserVO goods = data.get(pos);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tool.toDetail(mContext, goods.getId());
            }
        });
        holder.price.setText(String.format("%.2f", goods.getPrice()));
        holder.name.setText(goods.getName());
        holder.time.setText(Tool.parseTime(goods.getCreateTime()));
        holder.userName.setText(goods.getUserName());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.goodsImage.getLayoutParams();
        layoutParams.width = (Tool.getScreenWidth(mContext) / 2 - 10);
        layoutParams.height = (int) (layoutParams.width * goods.getRatio());
        holder.goodsImage.setLayoutParams(layoutParams);
        asyncImageLoader.asyncloadImage(holder.goodsImage, UrlTool.GOODSIMG + goods.getImg1());
        asyncImageLoader.asyncloadImage(holder.avator, UrlTool.AVATOR + goods.getAvator());

    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {

        return mHeaderView == null ? data.size() : data.size() + 1;
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        ImageView goodsImage;
        TextView name;
        TextView price;
        TextView time;
        TextView userName;
        CircleImageView avator;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            time = itemView.findViewById(R.id.time);
            userName = itemView.findViewById(R.id.userName);
            avator = itemView.findViewById(R.id.avator);
            goodsImage = itemView.findViewById(R.id.image_item);
            name = itemView.findViewById(R.id.goods_name);
            price = itemView.findViewById(R.id.goods_price);
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