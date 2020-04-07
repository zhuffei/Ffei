package com.example.zhuffei.ffei.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.FocusActivity;
import com.example.zhuffei.ffei.activity.MyGoodsActivity;
import com.example.zhuffei.ffei.tool.Tool;
import com.leon.lib.settingview.LSettingItem;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;


/**
 * 我的Fragment
 */
public class WDFragment extends BaseFragment {

    private Context mContext;

    private ImageView profile_image;
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;



    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    LSettingItem itemOne,itemTwo,itemThree,itemFour;

    LinearLayout focus,fans;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
        profile_image = view.findViewById(R.id.profile_image);
        mContext = this.getActivity();
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext, R.style.BottomDialogStyle);
                View view = View.inflate(mContext, R.layout.activity_avator, null);
                TextView cancel = view.findViewById(R.id.cancle);
                TextView local = view.findViewById(R.id.local);
                TextView camera = view.findViewById(R.id.camera);
                dialog.setContentView(view);
                dialog.setCanceledOnTouchOutside(true);
                view.setMinimumHeight((int) (Tool.getScreenHeight(mContext) * 0.2f));
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = (int) (Tool.getScreenWidth(mContext));
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.BOTTOM;
                dialogWindow.setAttributes(lp);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                local.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        choseHeadImageFromGallery();
                    }
                });
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        choseHeadImageFromCameraCapture();
                    }
                });
                dialog.show();
            }
        });
        itemOne = view.findViewById(R.id.item_one);
        itemOne.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mContext, MyGoodsActivity.class);
                intent.putExtra("position",MyGoodsActivity.RELEASE);
                startActivity(intent);
            }
        });
        itemTwo = view.findViewById(R.id.item_two);
        itemTwo.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mContext, MyGoodsActivity.class);
                intent.putExtra("position",MyGoodsActivity.COLLECTION);
                startActivity(intent);
            }
        });
        itemThree = view.findViewById(R.id.item_three);
        itemThree.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mContext, MyGoodsActivity.class);
                intent.putExtra("position",MyGoodsActivity.BUY);
                startActivity(intent);
            }
        });
        itemFour = view.findViewById(R.id.item_four);
        itemFour.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                Intent intent = new Intent(mContext, MyGoodsActivity.class);
                intent.putExtra("position",MyGoodsActivity.SELL);
                startActivity(intent);
            }
        });

        focus = view .findViewById(R.id.focus);
        focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FocusActivity.class);
                intent.putExtra("position",FocusActivity.FOCUS);
                startActivity(intent);
            }
        });
        fans = view .findViewById(R.id.fans);
        fans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FocusActivity.class);
                intent.putExtra("position",FocusActivity.FANS);
                startActivity(intent);
            }
        });
        return view;
    }

    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(mContext, "操作取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(mContext, "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            profile_image.setImageBitmap(photo);
        }
    }


    @Override
    protected void setTitle(TextView tvTitle) {

    }
}
