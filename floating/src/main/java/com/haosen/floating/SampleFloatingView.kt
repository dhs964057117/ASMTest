package com.haosen.floating

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes

/**
 * FileName: EnFloatingView
 * Author: haosen
 * Date: 2024/11/16 20:21
 * Description:
 */
class SampleFloatingView @JvmOverloads constructor(
    context: Context,
    @LayoutRes resource: Int = R.layout.en_floating_view
) : FloatingView(context, null) {
    private val mIcon: ImageView

    init {
        inflate(context, resource, this)
        mIcon = findViewById(R.id.icon)
    }

    fun setIconImage(@DrawableRes resId: Int) {
        mIcon.setImageResource(resId)
    }
}
