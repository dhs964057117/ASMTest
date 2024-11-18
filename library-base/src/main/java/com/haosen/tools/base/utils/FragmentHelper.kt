package com.haosen.tools.base.utils

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.haosen.tools.base.R

object FragmentHelper {

    fun replace(
        manager: FragmentManager,
        resId: Int,
        clazz: Class<out Fragment>,
        bundle: Bundle?,
        addToBackStack: Boolean
    ): Fragment? {
        val transaction = manager.beginTransaction()
        val tag = clazz.simpleName
        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            try {
                fragment = clazz.newInstance()
            } catch (e: Exception) {
                Log.e(this.javaClass.name, e.message.toString())
            }
        }
        if (fragment != null) {
            if (bundle != null && !bundle.isEmpty) {
                val args = fragment.arguments
                if (args != null) {
                    args.putAll(bundle)
                } else {
                    fragment.arguments = bundle
                }
            }
            transaction.replace(resId, fragment, tag)
            if (addToBackStack) {
                transaction.addToBackStack(tag)
            }
            transaction.commitAllowingStateLoss()
        }
        return fragment
    }

    fun switcher(
        manager: FragmentManager,
        resId: Int,
        currClazz: Class<out Fragment>?,
        switchClazz: Class<out Fragment>,
        bundle: Bundle?,
        addToBackStack: Boolean
    ): Class<out Fragment> {
        val transaction = manager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_out_left,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
        if (currClazz != null) {
            manager.findFragmentByTag(currClazz.simpleName)?.let {
                transaction.hide(it)
                transaction.setMaxLifecycle(it, Lifecycle.State.STARTED)
            }
        }
        val tag = switchClazz.simpleName
        var switchFragment = manager.findFragmentByTag(tag)
        if (switchFragment == null) {
            try {
                switchFragment = switchClazz.newInstance()
                transaction.add(resId, switchFragment, tag)
                transaction.setMaxLifecycle(switchFragment, Lifecycle.State.RESUMED)
            } catch (e: Exception) {
                Log.e(this.javaClass.name, e.message.toString())
            }
        } else {
            transaction.show(switchFragment)
            transaction.setMaxLifecycle(switchFragment, Lifecycle.State.RESUMED)
        }
        if (switchFragment != null) {
            if (bundle != null) {
                val arguments = switchFragment.arguments
                if (arguments != null) {
                    arguments.putAll(bundle)
                } else {
                    switchFragment.arguments = bundle
                }
            }
            if (addToBackStack) {
                transaction.addToBackStack(tag)
            }
            transaction.commitAllowingStateLoss()
        }
        return switchClazz
    }

    fun pop(manager: FragmentManager, clazz: Class<out Fragment>): Class<out Fragment> {
        manager.popBackStackImmediate(clazz.simpleName, 0)
        return clazz
    }

}