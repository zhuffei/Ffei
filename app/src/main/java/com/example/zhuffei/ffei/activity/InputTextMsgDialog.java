package com.example.zhuffei.ffei.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.tool.ToastHelper;
/**
 * @author zhuffei
 * @version 1.0
 * @date 2020/4/2 10:40
 */
public class InputTextMsgDialog extends AppCompatDialog {
    private Context mContext;
    private InputMethodManager imm;
    private EditText messageTextView;
    private TextView confirmBtn;
    private RelativeLayout rlDlg;
    private int mLastDiff = 0;
    private TextView tvNumber;
    private int maxNumber = 200;

    public interface OnTextSendListener {

        void onTextSend(String msg);
    }


    private OnTextSendListener mOnTextSendListener;

    public InputTextMsgDialog(@NonNull Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        this.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
        init();
        setLayout();
    }

    /**
     * 最大输入字数  默认200
     */
    @SuppressLint("SetTextI18n")
    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
        tvNumber.setText("0/" + maxNumber);
    }

    /**
     * 设置输入提示文字
     */
    public void setHint(String text) {
        messageTextView.setHint(text);
    }

    /**
     * 设置按钮的文字  默认为：发送
     */
    public void setBtnText(String text) {
        confirmBtn.setText(text);
    }

    private void init() {
        setContentView(R.layout.dialog_input_text_msg);
        messageTextView = findViewById(R.id.et_input_message);
        tvNumber = findViewById(R.id.tv_test);
        final LinearLayout rldlgview = findViewById(R.id.rl_inputdlg_view);
        messageTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvNumber.setText(editable.length() + "/" + maxNumber);
                if (editable.length() > maxNumber) {
/*dot_hong颜色值：#E73111,text_bottom_comment颜色值：#9B9B9B*/
                    tvNumber.setTextColor(mContext.getResources().getColor(R.color.red));
                } else {
                    tvNumber.setTextColor(mContext.getResources().getColor(R.color.black_overlay));
                }
                if (editable.length() == 0) {
                    confirmBtn.setBackgroundResource(R.drawable.btn_send_normal);
                } else {
                    confirmBtn.setBackgroundResource(R.drawable.btn_send_pressed);
                }
            }
        });

        confirmBtn = findViewById(R.id.confrim_btn);
        LinearLayout ll_tv = findViewById(R.id.ll_tv);
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        ll_tv.setOnClickListener(view -> {
            String msg = messageTextView.getText().toString().trim();
            if (msg.length() > maxNumber) {
                ToastHelper.showToast("超过最大字数限制");
                return;
            }
            if (!TextUtils.isEmpty(msg)) {
                mOnTextSendListener.onTextSend(msg);
                imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                messageTextView.setText("");
                dismiss();
            } else {
               ToastHelper.showToast("请输入文字");
            }
            messageTextView.setText(null);
        });

        messageTextView.setOnEditorActionListener((v, actionId, event) -> {
            switch (actionId) {
                case KeyEvent.KEYCODE_ENDCALL:
                case KeyEvent.KEYCODE_ENTER:
                    if (messageTextView.getText().length() > maxNumber) {
                        ToastHelper.showToast("超过最大字数限制");
                        return true;
                    }
                    if (messageTextView.getText().length() > 0) {
                        imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                        dismiss();
                    } else {
                        ToastHelper.showToast("请输入文字");
                    }
                    return true;
                case KeyEvent.KEYCODE_BACK:
                    dismiss();
                    return false;
                default:
                    return false;
            }
        });

        messageTextView.setOnKeyListener((view, i, keyEvent) -> false);

        rlDlg = findViewById(R.id.rl_outside_view);
        rlDlg.setOnClickListener(v -> {
            if (v.getId() != R.id.rl_inputdlg_view)
                dismiss();
        });

        rldlgview.addOnLayoutChangeListener((view, i, i1, i2, i3, i4, i5, i6, i7) -> {
            Rect r = new Rect();
            //获取当前界面可视部分
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //获取屏幕的高度
            int screenHeight = getWindow().getDecorView().getRootView().getHeight();
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
            int heightDifference = screenHeight - r.bottom;

            if (heightDifference <= 0 && mLastDiff > 0) {
                dismiss();
            }
            mLastDiff = heightDifference;
        });
        rldlgview.setOnClickListener(v -> {
            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
            dismiss();
        });

        setOnKeyListener((dialogInterface, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0)
                dismiss();
            return false;
        });
    }

    private void setLayout() {
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = WindowManager.LayoutParams.MATCH_PARENT;
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(p);
        setCancelable(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
    }

    @Override
    public void show() {
        super.show();
    }
}