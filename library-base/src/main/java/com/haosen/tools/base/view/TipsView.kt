package com.haosen.tools.base.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import com.haosen.tools.base.R

class TipsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SlideLayout(context, attrs, defStyleAttr) {

    private var close: ImageView
    private var message: TextView

    init {
        inflate(context, R.layout.view_tips, this)
        message = findViewById(R.id.message)
        close = findViewById(R.id.close)
        close.setOnClickListener {
            dismiss()
        }
    }

    fun setMessage(text: String) {
        message.text = text
    }

}