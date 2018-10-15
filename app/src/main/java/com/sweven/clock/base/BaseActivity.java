package com.sweven.clock.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sweven.clock.R;
import com.sweven.clock.utils.LogUtil;

/**
 * Created by Sweven on 2018/10/12.
 * Email:sweventears@Foxmail.com
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 标记标题左右两边的类型:文字
     */
    protected final int BTN_TYPE_TEXT = 0;
    /**
     * 标记标题左右两边的类型:图片
     */
    protected final int BTN_TYPE_IMG = 1;

    /**
     * 标记左边按键的显示状态
     */
    private boolean leftButton = true;
    /**
     * 标记右边按键的显示状态
     */
    private boolean rightButton = true;


    public Activity activity;

    private RelativeLayout layoutLeft;
    private RelativeLayout layoutRight;

    /**
     * 枚举：左右按钮
     */
    protected enum KeyKind {
        /**
         * 标记左边的按键
         */
        ACTIONBAR_LEFT,
        /**
         * 标记右边的按键
         */
        ACTIONBAR_RIGHT
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        init();
    }

    /**
     * 绑定id
     */
    protected void bindViewId() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * BaseActivity初始配置
     */
    private void init() {

        new LogUtil(this.toString());

        // 设置字体大小不随系统字体大小的改变而改变
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

        // 设置自定义 actionbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_activity_base);

        layoutLeft = findViewById(R.id.layout_title_left);
        layoutRight = findViewById(R.id.layout_title_right);
        leftDo();
        rightDo();

        // 默认左右两边的按键都不显示
        hiddenLeftButton();
        hiddenRightButton();
    }

    /**
     * [设置标题]
     *
     * @param title 标题
     */
    protected void setActionBarTitle(Object title) {
        TextView textTitle = findViewById(R.id.text_title);
        if (title instanceof String && !TextUtils.isEmpty((String) title)) {
            textTitle.setText((String) title);
        } else if (title instanceof Integer) {
            textTitle.setText((Integer) title);
        }

    }

    /**
     * @param btnType   类型
     *               (Image、Text)
     * @param object 内容
     */
    protected void setCustomerActionBar(KeyKind kind, int btnType, Object object) {
        TextView textView = new TextView(activity);
        ImageView imageView = new ImageView(activity);
        if (kind == KeyKind.ACTIONBAR_LEFT) {
            textView = findViewById(R.id.text_title_left);
            imageView = findViewById(R.id.image_title_left);
        } else if (kind == KeyKind.ACTIONBAR_RIGHT) {
            textView = findViewById(R.id.text_title_right);
            imageView = findViewById(R.id.image_title_right);
        }
        if (btnType == BTN_TYPE_IMG) {
            textView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);

            if (object instanceof Bitmap) {
                imageView.setImageBitmap((Bitmap) object);
            } else if (object instanceof Integer) {
                imageView.setImageResource((Integer) object);
            }
        } else {
            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);

            if (object instanceof String) {
                textView.setText((String) object);
            } else if (object instanceof Integer) {
                textView.setText((Integer) object);
            }
        }
    }

    protected void showLeftButton() {
        if (!leftButton) {
            leftButton = true;
            layoutLeft.setVisibility(View.VISIBLE);
            setCustomerActionBar(KeyKind.ACTIONBAR_LEFT, BTN_TYPE_IMG, R.drawable.ic_back_left_white_48dp);
        }
    }

    protected void showRightButton() {
        if (!rightButton) {
            rightButton = true;
            layoutRight.setVisibility(View.VISIBLE);
            setCustomerActionBar(KeyKind.ACTIONBAR_RIGHT, BTN_TYPE_TEXT, "完成");
        }
    }

    protected void hiddenLeftButton() {
        if (leftButton) {
            leftButton = false;
            layoutLeft.setVisibility(View.INVISIBLE);
        }
    }

    protected void hiddenRightButton() {
        if (rightButton) {
            rightButton = false;
            layoutRight.setVisibility(View.INVISIBLE);
        }
    }

    private void leftDo() {
        layoutLeft.setOnClickListener(view -> leftDoWhat());
    }

    protected void leftDoWhat() {
        BaseActivity.this.finish();
    }

    private void rightDo() {
        layoutRight.setOnClickListener(view -> rightDoWhat());
    }

    protected void rightDoWhat() {

    }

    public boolean isLeftButton() {
        return leftButton;
    }

    public void setLeftButton(boolean leftButton) {
        this.leftButton = leftButton;
    }

    public boolean isRightButton() {
        return rightButton;
    }

    public void setRightButton(boolean rightButton) {
        this.rightButton = rightButton;
    }

    @Override
    public void onClick(View view) {

    }
}
