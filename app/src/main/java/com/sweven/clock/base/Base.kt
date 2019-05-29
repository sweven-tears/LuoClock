package com.sweven.clock.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.sweven.clock.R
import com.sweven.clock.utils.LogUtil
import com.sweven.clock.utils.ToastUtil

/**
 * Created by Sweven on 2019/3/3.
 * Email:sweventears@Foxmail.com
 */
@SuppressLint("Registered")
class BaseActivity : AppCompatActivity(), View.OnClickListener {

    /**
     * 标记标题左右两边的类型:文字
     */
    protected val BTN_TYPE_TEXT = 0
    /**
     * 标记标题左右两边的类型:图片
     */
    protected val BTN_TYPE_IMG = 1


    var activity: Activity? = null

    /**
     * 左边按键的父组件
     */
    private var layoutLeft: RelativeLayout? = null
    /**
     * 右边按键的父组件
     */
    private var layoutRight: RelativeLayout? = null

    protected val TAG = this.localClassName

    /**
     * Dialog提示框
     */
    private var mDialog: Dialog? = null
    /**
     * Dialog TextView
     */
    private var dialogLoadText: TextView? = null
    /**
     * Dialog imageView
     */
    private var dialogLoadImage: ImageView? = null
    /**
     * Dialog进度条
     */
    private var dialogLoadProgress: ProgressBar? = null
    /**
     * Alert提示框
     */
    protected var mAlert: AlertDialog.Builder? = null
    protected var log: LogUtil? = null
    protected var toast: ToastUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        init()
    }

    /**
     * 绑定id
     */
    protected fun bindViewId() {}

    /**
     * 初始化数据
     */
    protected fun initData() {}

    /**
     * BaseActivity初始配置
     */
    private fun init() {

        log = LogUtil(TAG)
        toast = ToastUtil(activity)

        // 设置字体大小不随系统字体大小的改变而改变
        val res = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)

        // 设置自定义 actionbar
        val actionBar = supportActionBar
        actionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setCustomView(R.layout.actionbar_activity_base)

        layoutLeft = findViewById(R.id.layout_title_left)
        layoutRight = findViewById(R.id.layout_title_right)
        leftDo()
        rightDo()

        // 默认左右两边的按键都不显示
        hiddenLeftButton()
        hiddenRightButton()

        // 初始化左右按键
        setCustomerActionBar(KeyActionBarButtonKind.ACTIONBAR_LEFT, BTN_TYPE_IMG, R.drawable.ic_back_left_white_48dp)
        setCustomerActionBar(KeyActionBarButtonKind.ACTIONBAR_RIGHT, BTN_TYPE_TEXT, "完成")

        initDialog()
    }

    /**
     * [设置标题]
     *
     * @param title 标题
     */
    protected fun setActionBarTitle(title: Any) {
        val textTitle = findViewById<TextView>(R.id.text_title)
        if (title is String && !TextUtils.isEmpty(title)) {
            textTitle.text = title
        } else if (title is Int) {
            textTitle.setText(title)
        }

    }

    /**
     * @param btnType 类型
     * (Image、Text)
     * @param object  内容
     */
    protected fun setCustomerActionBar(kind: KeyActionBarButtonKind, btnType: Int, `object`: Any) {
        var textView = TextView(activity)
        var imageView = ImageView(activity)
        if (kind == KeyActionBarButtonKind.ACTIONBAR_LEFT) {
            textView = findViewById(R.id.text_title_left)
            imageView = findViewById(R.id.image_title_left)
        } else if (kind == KeyActionBarButtonKind.ACTIONBAR_RIGHT) {
            textView = findViewById(R.id.text_title_right)
            imageView = findViewById(R.id.image_title_right)
        }
        if (btnType == BTN_TYPE_IMG) {
            textView.visibility = View.INVISIBLE
            imageView.visibility = View.VISIBLE

            if (`object` is Bitmap) {
                imageView.setImageBitmap(`object`)
            } else if (`object` is Int) {
                imageView.setImageResource(`object`)
            }
        } else {
            textView.visibility = View.VISIBLE
            imageView.visibility = View.INVISIBLE

            if (`object` is String) {
                textView.text = `object`
            } else if (`object` is Int) {
                textView.setText(`object`)
            }
        }
    }

    /**
     * 显示actionbar左边按键
     */
    protected fun showLeftButton() {
        layoutLeft!!.visibility = View.VISIBLE
    }

    /**
     * 显示actionbar右边按键
     */
    protected fun showRightButton() {
        layoutRight!!.visibility = View.VISIBLE
    }

    /**
     * 隐藏左边的按键
     */
    protected fun hiddenLeftButton() {
        layoutLeft!!.visibility = View.INVISIBLE
    }

    /**
     * 隐藏右边的按键
     */
    protected fun hiddenRightButton() {
        layoutRight!!.visibility = View.INVISIBLE
    }

    /**
     * 点击左边按键的监听器
     */
    private fun leftDo() {
        layoutLeft!!.setOnClickListener { view -> leftDoWhat() }
    }

    /**
     * 左边按键的监听事件
     */
    protected fun leftDoWhat() {
        this@BaseActivity.finish()
    }

    /**
     * 点击右边按键的监听器
     */
    private fun rightDo() {
        layoutRight!!.setOnClickListener { view -> rightDoWhat() }
    }

    /**
     * 右边按键的监听事件
     */
    protected fun rightDoWhat() {

    }

    fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        super.startActivity(intent)
    }

    fun startActivity(cls: Class<*>, bundle: Bundle) {
        val intent = Intent(this, cls)
        intent.putExtras(bundle)
        super.startActivity(intent)
    }

    fun startActivityForResult(cls: Class<*>, requestCode: Int) {
        val intent = Intent(this, cls)
        super.startActivityForResult(intent, requestCode)
    }

    /**
     * 初始化Dialog
     */
    private fun initDialog() {
        mDialog = Dialog(this, R.style.Theme_AppCompat_Dialog)
        val mDialogContentView = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)
        dialogLoadText = mDialogContentView.findViewById(R.id.load_text)
        dialogLoadImage = mDialogContentView.findViewById(R.id.load_image)
        dialogLoadProgress = mDialogContentView.findViewById(R.id.load_progress)
        mDialog!!.setCanceledOnTouchOutside(false)
        mDialog!!.setContentView(mDialogContentView)
        val window = mDialog!!.window
        window?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL)
    }

    /**
     * 显示Dialog
     */
    fun showProgressDialog() {
        if (mDialog != null && !mDialog!!.isShowing) {
            dialogLoadProgress!!.visibility = View.VISIBLE
            dialogLoadText!!.visibility = View.VISIBLE
            dialogLoadImage!!.visibility = View.GONE
            dialogLoadText!!.text = "加载中……"
            mDialog!!.show()
        }
    }

    /**
     * 关闭Dialog
     */
    fun dismissDialog() {
        if (mDialog!!.isShowing) {
            mDialog!!.dismiss()
        }
    }

    /**
     * [初始设置Alert]
     *
     * @param iconId   图标
     * @param title    标题
     * @param message  显示信息
     * @param isCancel 是否设置取消
     */
    protected fun initAlert(iconId: Int, title: String, message: String, isCancel: Boolean) {
        mAlert = AlertDialog.Builder(activity)
        mAlert!!.setIcon(iconId)
        mAlert!!.setTitle(title)
        mAlert!!.setMessage(message)

        mAlert!!.setCancelable(isCancel)
        mAlert!!.create()
    }

    /**
     * [初始设置Alert]
     *
     * @param title    标题
     * @param message  显示信息
     * @param isCancel 是否设置取消
     */
    protected fun initAlert(title: String, message: String, isCancel: Boolean) {
        mAlert = AlertDialog.Builder(activity)
        mAlert!!.setTitle(title)
        mAlert!!.setMessage(message)

        mAlert!!.setCancelable(isCancel)
        mAlert!!.create()
    }

    /**
     * 显示Alert
     */
    protected fun showAlert() {
        if (mAlert != null) {
            mAlert!!.show()
        }
    }

    override fun onClick(view: View) {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            onBack()
        }
        return super.onKeyDown(keyCode, event)
    }

    protected fun onBack() {

    }

    //---------------------------------------枚举----------------------------------------//
    /**
     * 枚举：左右按钮
     */
    protected enum class KeyActionBarButtonKind {
        /**
         * 标记左边的按键
         */
        ACTIONBAR_LEFT,
        /**
         * 标记右边的按键
         */
        ACTIONBAR_RIGHT
    }


}
