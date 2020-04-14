package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.tool.FileUtil;
import com.example.zhuffei.ffei.tool.Tool;
import com.github.cropbitmap.LikeQQCropView;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/11 16:17
 */
public class CropActivity extends AppCompatActivity {
    private LikeQQCropView likeView;

    private Button confirm,cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        setContentView(R.layout.activity_crop);
        likeView = findViewById(R.id.likeView);
        likeView.setBitmapForWidth(FileUtil.getPath(this,uri), Tool.getScreenWidth(this));
//        likeView.setMaskColor(Color.parseColor("#DDDDDD"));
        likeView.setRadius(Tool.getScreenWidth(this)/2);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tool.bitmap = likeView.clip();
//                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
//                Intent intent1 = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("bitmap",bitmap);
//                intent1.putExtras(bundle);
                setResult(2,intent);
                finish();
            }
        });
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
