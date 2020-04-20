package com.example.zhuffei.ffei.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.entity.CommentUserVO;
import com.example.zhuffei.ffei.tool.AsyncImageLoader;
import com.example.zhuffei.ffei.tool.ToastHelper;
import com.example.zhuffei.ffei.tool.Tool;
import com.example.zhuffei.ffei.tool.UrlTool;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CommentAdapter extends BaseAdapter {
    private List<CommentUserVO> data;
    private Context mContext;
    private OnDeleteListener onDeletelistener;
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case -1:
                    ToastHelper.showToast("网络异常");
                    break;
                case 0:

            }
        }
    };

    public CommentAdapter(List<CommentUserVO> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    public void setOnDeletelistener(OnDeleteListener onDeletelistener) {
        this.onDeletelistener = onDeletelistener;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, null);
        CircleImageView avator = view.findViewById(R.id.avator);
        TextView commentText = view.findViewById(R.id.commentText);
        TextView userName = view.findViewById(R.id.userName);
        TextView createTime = view.findViewById(R.id.createTime);
        TextView delete = view.findViewById(R.id.delete);
        CommentUserVO commentUserVO = data.get(position);
        if (FfeiApplication.isLogin && FfeiApplication.user.getId() == commentUserVO.getuId()) {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(v->{
                CommentAdapter.this.onDeletelistener.onDelete(commentUserVO.getId());
            });
        }
        AsyncImageLoader asyncImageLoader = new AsyncImageLoader(mContext);
        asyncImageLoader.asyncloadImage(avator, UrlTool.AVATOR + commentUserVO.getAvator());
        commentText.setText(commentUserVO.getComment());
        userName.setText(commentUserVO.getUserName());
        createTime.setText(Tool.parseTime(commentUserVO.getCreateTime()));
        return view;
    }
    public interface OnDeleteListener{
        void onDelete(int id);
    }
}
