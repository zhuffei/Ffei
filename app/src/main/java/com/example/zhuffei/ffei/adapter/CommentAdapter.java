package com.example.zhuffei.ffei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.CommentUserVO;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.UrlTool;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentAdapter extends BaseAdapter {
    private List<CommentUserVO> data;
    private Context mContext;
    public CommentAdapter(List<CommentUserVO> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item,null);
        CircleImageView avator = view.findViewById(R.id.avator);
        TextView commentText = view.findViewById(R.id.commentText);
        TextView userName = view.findViewById(R.id.userName);
        TextView createTime = view.findViewById(R.id.createTime);
        AsyncImageLoader asyncImageLoader = new AsyncImageLoader(mContext);
        asyncImageLoader.asyncloadImage(avator, UrlTool.AVATOR+data.get(position).getAvator());
        commentText.setText(data.get(position).getComment());
        userName.setText(data.get(position).getUserName());
        createTime.setText(data.get(position).getCreateTime());
        return view;
    }
}
