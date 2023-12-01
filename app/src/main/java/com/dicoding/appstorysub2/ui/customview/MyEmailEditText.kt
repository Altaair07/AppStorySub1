package com.dicoding.appstorysub2.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dicoding.appstorysub2.R

class MyEmailEditText : AppCompatEditText {

    private var drawableIconStart: Drawable? = null
    private var drawableBackground: Drawable? = null
    private var hintText: String = ""

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        hintText = resources.getString(R.string.hint_email)
        drawableIconStart = ContextCompat.getDrawable(context, R.drawable.ic_email)
        drawableBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text)

        addTextChangedListener(
            onTextChanged = { text, _, _, _ ->
                error = if (!isEmailValid(text)) resources.getString(R.string.email_error) else null
            }
        )

        maxLines = 1
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
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

    private fun isEmailValid(emailText: CharSequence?): Boolean {
        return emailText.toString().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailText.toString()).matches()
    }
}