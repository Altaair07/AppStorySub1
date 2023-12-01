package com.dicoding.appstorysub2.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dicoding.appstorysub2.R

@SuppressLint("AppCompatCustomView")
class MyPasswordEditText : EditText {

    private var drawableIconStart: Drawable? = null
    private var drawableBackground: Drawable? = null
    private var minimumPasswordLength = 8
    private var hintText: String = ""

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        hintText = resources.getString(R.string.hint_password)
        drawableIconStart = ContextCompat.getDrawable(context, R.drawable.ic_lock)
        drawableBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text)

        addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                error = if (!isPasswordValid(text)) resources.getString(R.string.password_error) else null
            }
        )

        maxLines = 1
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        hint = hintText

        setDrawableIcon(
            left = drawableIconStart
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = drawableBackground
    }

    private fun setDrawableIcon(
        left: Drawable? = null,
        top: Drawable? = null,
        right: Drawable? = null,
        bottom: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            left, top, right, bottom
        )
        compoundDrawablePadding = 16
    }

    private fun isPasswordValid(passwordText: CharSequence?): Boolean {
        return passwordText.toString().isNotEmpty() && passwordText.toString().length >= minimumPasswordLength
    }
}