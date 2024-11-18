package com.haosen.floating

import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes

/**
 * FileName: IFloatingView
 * Author: haosen
 * Date: 2024/11/17 20:40
 * Description:
 */
interface IFloatingView {
    fun remove(): Floating?

    fun show(): Floating?

    fun hide(): Floating?

    val view: FloatingView?

    fun icon(@DrawableRes resId: Int): Floating?

    fun customView(viewGroup: FloatingView?): Floating?

    fun customView(@LayoutRes resource: Int): Floating?

    fun layoutParams(params: ViewGroup.LayoutParams): Floating?

    fun listener(magnetViewListener: MagnetViewListener): Floating?
}
