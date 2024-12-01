package com.haosen.floating.core

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.haosen.floating.manager.MagnetViewListener

/**
 * FileName: IFloatingView
 * Author: haosen
 * Date: 2024/11/17 20:40
 * Description:
 */
interface IFloatingView {
    fun remove()

    fun show()

    fun hide()

    val view: View?

    fun icon(@DrawableRes resId: Int): IFloatingView?

    fun layoutParams(params: ViewGroup.LayoutParams): IFloatingView?

    fun listener(magnetViewListener: MagnetViewListener): IFloatingView?
}
