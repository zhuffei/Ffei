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
import com.example.zhuffei.ffei.entity.GoodsUserOV;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;
import com.example.zhuffei.ffei.view.CircleImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private List<GoodsUserOV> data;

    private AsyncImageLoader asyncImageLoader;

    private View mHeaderView;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public GoodsItemAdapter(List<GoodsUserOV> data, Context context) {
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
        GoodsUserOV goods = data.get(pos);
        holder.price.setText(goods.getPrice() + "");
        holder.name.setText(goods.getName());
        holder.time.setText(parseTime(goods.getCreateTime()));
        holder.userName.setText(goods.getUserName());
        asyncImageLoader.asyncloadImage(holder.goodsImage, UrlTool.GOODSIMG + goods.getImg1());
        asyncImageLoader.asyncloadImage(holder.avator, UrlTool.AVATOR + goods.getAvator());
        holder.setImgHeigh(mContext);
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    static class GoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView goodsImage;
        TextView name;
        TextView price;
        TextView time;
        TextView userName;
        CircleImageView avator;

        public GoodsViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            userName = itemView.findViewById(R.id.userName);
            avator = itemView.findViewById(R.id.avator);
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

    public String parseTime(String time) {

        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
            Date now = new Date();
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            String s1 = "";
            if (day > 0) {
                s1 = day + "天前";
                return s1;
            }
            if (hour > 0) {
                s1 = hour + "小时前";
                return s1;
            }
            if (min > 0){
                s1 = min + "分钟前";
            return s1;}else {
                return  "刚刚";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "未知时间";
        }

    }
}