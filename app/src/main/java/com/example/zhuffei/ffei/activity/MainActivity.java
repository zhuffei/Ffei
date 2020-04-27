package com.example.zhuffei.ffei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.tool.ToastHelper;


/**
 * @author zhuffei
 */
public class MainActivity extends BaseActivity {

    private Button homePage;

    private Button button2;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setEvent();
    }

    public void findViews() {
        textView = findViewById(R.id.register);
        homePage = findViewById(R.id.homePage);
        button2 = findViewById(R.id.login);
    }



    public void setEvent() {
        homePage.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivityForResult(intent, 1);
        });
        textView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivityForResult(intent, 1);

        });
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivityForResult(intent, 1);

        });
    }






    long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                ToastHelper.showToast("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finishAffinity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
