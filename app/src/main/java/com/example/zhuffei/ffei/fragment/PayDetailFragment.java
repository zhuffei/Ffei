package com.example.zhuffei.ffei.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.activity.BuyActivity;
import com.example.zhuffei.ffei.entity.Goods;


/**
 * 底部弹窗Fragment
 */
public class PayDetailFragment extends DialogFragment {
    private RelativeLayout rePayWay, rePayDetail, reBalance;
    private LinearLayout LinPayWay, linPass;
    private ListView lv;
    private Button btnPay;
    private EditText gridPasswordView;
    private ImageView imageCloseOne, imageCloseTwo, imageCloseThree;
    private BuyActivity buyActivity;
    private Goods goods;
    private TextView price1, price2;
    private TextView goodsName;

    public PayDetailFragment(Goods goods, BuyActivity buyActivity) {
        this.goods = goods;
        this.buyActivity = buyActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_pay_detail);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        // 设置宽度为屏宽, 靠近屏幕底部。
        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AnimBottom);
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = getActivity().getWindowManager().getDefaultDisplay().getHeight() * 3 / 5;
        window.setAttributes(lp);

        initView(dialog);


        if (getDialog() != null) {
            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface anInterface, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (!TextUtils.isEmpty(gridPasswordView.getText().toString().trim())) {
                            if ("123456".equals(gridPasswordView.getText().toString().trim())) {
                                //TODO 跳转支付宝支付
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }


        return dialog;
    }

    private void initView(Dialog dialog) {
        price1 = dialog.findViewById(R.id.price1);
        price2 = dialog.findViewById(R.id.price2);
        price1.setText(String.format("%.2f", goods.getPrice()));
        price2.setText(String.format("%.2f", goods.getPrice()));
        goodsName = dialog.findViewById(R.id.goodsName);
        goodsName.setText(goods.getName());
        rePayWay = dialog.findViewById(R.id.re_pay_way);
        rePayDetail = dialog.findViewById(R.id.re_pay_detail);//付款详情
        LinPayWay = dialog.findViewById(R.id.lin_pay_way);//付款方式
        lv = dialog.findViewById(R.id.lv_bank);//付款方式（银行卡列表）
        reBalance = dialog.findViewById(R.id.re_balance);//付款方式（余额）
        btnPay = dialog.findViewById(R.id.btn_confirm_pay);
        gridPasswordView = dialog.findViewById(R.id.pass_view);
        linPass = dialog.findViewById(R.id.lin_pass);
        imageCloseOne = dialog.findViewById(R.id.close_one);
        imageCloseTwo = dialog.findViewById(R.id.close_two);
        imageCloseThree = dialog.findViewById(R.id.close_three);
        rePayWay.setOnClickListener(listener);
        reBalance.setOnClickListener(listener);
        btnPay.setOnClickListener(listener);
        imageCloseOne.setOnClickListener(listener);
        imageCloseTwo.setOnClickListener(listener);
        imageCloseThree.setOnClickListener(listener);


    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Animation slide_left_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_left);
            Animation slide_right_to_left = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_right_to_left);
            Animation slide_left_to_right = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_right);
            Animation slide_left_to_left_in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_left_to_left_in);
            switch (view.getId()) {
                case R.id.re_pay_way://选择方式
                    rePayDetail.startAnimation(slide_left_to_left);
                    rePayDetail.setVisibility(View.GONE);
                    LinPayWay.startAnimation(slide_right_to_left);
                    LinPayWay.setVisibility(View.VISIBLE);
                    break;
                case R.id.re_balance:
                    rePayDetail.startAnimation(slide_left_to_left_in);
                    rePayDetail.setVisibility(View.VISIBLE);
                    LinPayWay.startAnimation(slide_left_to_right);
                    LinPayWay.setVisibility(View.GONE);
                    break;
                case R.id.btn_confirm_pay://确认付款
                    getDialog().dismiss();
                    buyActivity.intoPwdFragment();
                    break;
                case R.id.close_one:
                    getDialog().dismiss();
                    break;
                case R.id.close_two:
                    getDialog().dismiss();
                    break;
                case R.id.close_three:
                    getDialog().dismiss();
                    break;
                default:
                    break;
            }
        }
    };
}
