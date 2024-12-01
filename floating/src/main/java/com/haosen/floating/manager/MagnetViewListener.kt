package com.haosen.floating.manager

import com.haosen.floating.core.FloatingView

/**
 * FileName: MagnetViewListener
 * Author: haosen
 * Date: 2024/11/17 20:45
 * Description:
 */
interface MagnetViewListener {
    fun onRemove(magnetView: FloatingView)

    fun onClick(magnetView: FloatingView)
}
