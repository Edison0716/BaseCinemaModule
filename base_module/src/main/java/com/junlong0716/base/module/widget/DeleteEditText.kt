package com.junlong0716.base.module.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.EditText
import com.junlong0716.base.module.R

import java.lang.reflect.Method

/**
 * Created by ${巴黎没有摩天轮Li} on 2017/7/31.
 */

@SuppressLint("AppCompatCustomView")
class DeleteEditText(context: Context, attrs: AttributeSet?, defStyle: Int) : EditText(context, attrs, defStyle), View.OnFocusChangeListener, TextWatcher {

    /**
     * 删除按钮的引用
     */
    private var mClearDrawable: Drawable? = null

    /**
     * 控件是否有焦点
     */
    private var hasFoucs: Boolean = false

    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.editTextStyle) {
        init()
    }

    //这里构造方法也很重要，不加这个很多属性不能再XML里面定义

    init {
        init()
    }

    private fun init() {

        if (isInEditMode) {
            return
        }
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            //throw new NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = resources.getDrawable(R.mipmap.common_ic_edit_delete)
        }

        val a = dp2px(context, 17)
        /*        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());*/
        mClearDrawable!!.setBounds(0, 0, a, a)

        //默认设置隐藏图标
        setClearIconVisible(false)
        //设置焦点改变的监听
        onFocusChangeListener = this
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this)
    }

    fun setDeleteSrc(drawable: Drawable){
        mClearDrawable = compoundDrawables[2]
        mClearDrawable = drawable
        val a = dp2px(context, 17)
        /*        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
                mClearDrawable.getIntrinsicHeight());*/
        mClearDrawable!!.setBounds(0, 0, a, a)
    }

    private fun dp2px(context: Context, dip: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }


    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        clearIsClick(event)

        return super.onTouchEvent(event)
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        this.hasFoucs = hasFocus
        if (hasFocus) {
            setClearIconVisible(text.length > 0)
        } else {
            setClearIconVisible(false)
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     */
    protected fun setClearIconVisible(visible: Boolean) {
        var visible = visible
        if (!isEnabled) {
            visible = false
        }
        val right = if (visible) mClearDrawable else null
        setCompoundDrawables(compoundDrawables[0],
                compoundDrawables[1], right, compoundDrawables[3])
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        if (!enabled) {
            setClearIconVisible(false)
        }
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    override fun onTextChanged(s: CharSequence, start: Int, count: Int,
                               after: Int) {
        if (hasFoucs) {
            setClearIconVisible(s.length > 0)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                   after: Int) {

    }

    override fun afterTextChanged(s: Editable) {

    }


    /**
     * 阻止复制粘贴的功能
     */
    /*@Override
    public boolean onTextContextMenuItem(int id) {
        return true;
    }*/

    /**
     * 设置晃动动画
     */
    fun setShakeAnimation() {
        this.animation = shakeAnimation(5)
    }

    fun clearIsClick(event: MotionEvent): Boolean {
        var ret = false
        if (isEnabled && event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {

                val touchable = event.x > width - totalPaddingRight && event.x < width - paddingRight

                if (touchable) {
                    this.setText("")
                    ret = true
                }
            }
        }
        return ret
    }

    companion object {

        /**
         * 晃动动画
         *
         * @param counts 1秒钟晃动多少下
         * @return
         */
        fun shakeAnimation(counts: Int): Animation {
            val translateAnimation = TranslateAnimation(0f, 10f, 0f, 0f)
            translateAnimation.interpolator = CycleInterpolator(counts.toFloat())
            translateAnimation.duration = 1000
            return translateAnimation
        }

        /**
         * 禁止Edittext弹出软键盘，光标依然正常显示。
         */
        fun disableShowSoftInput(editText: EditText) {
            editText.setTextIsSelectable(false)
            if (android.os.Build.VERSION.SDK_INT <= 10) {
                editText.inputType = InputType.TYPE_NULL
            } else {
                val cls = EditText::class.java
                var method: Method
                try {
                    method = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
                    method.isAccessible = true
                    method.invoke(editText, false)
                } catch (e: Exception) {
                    // TODO: handle exception
                }

                try {
                    method = cls.getMethod("setSoftInputShownOnFocus", Boolean::class.javaPrimitiveType)
                    method.isAccessible = true
                    method.invoke(editText, false)
                } catch (e: Exception) {
                    // TODO: handle exception
                }

            }
        }
    }
}
