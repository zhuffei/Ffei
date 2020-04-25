package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.zhuffei.ffei.R;

/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/3/28 15:57
 */
public class PlusActivity extends BaseActivity {

    private Button issueGoods;

    private Button issueBuy;

    private ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus_layout);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlusActivity.this.finish();
            }
        });
        issueGoods = findViewById(R.id.issueGoods);
        issueGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlusActivity.this,IssueActivity.class);
                startActivity(intent);
            }
        });
        issueBuy = findViewById(R.id.issueBuy);
        issueBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlusActivity.this,IssueActivity2.class);
                startActivity(intent);
            }
        });
    }
}