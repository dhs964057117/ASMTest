package com.haosen.floating.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.collection.ArrayMap
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.haosen.floating.R
import com.haosen.floating.manager.ApplicationLifecycle
import com.haosen.floating.manager.Config
import com.haosen.floating.utils.Utils.isOnBackgroundThread
import com.haosen.floating.utils.Utils.isOnMainThread

/**
 * FileName: Floating
 * Author: haosen
 * Date: 2024/11/17 20:50
 * Description: 对外暴露的Builder配置项
 */
class Floating {
    companion object {
        private val DEFAULT_CONFIG = Config(layoutRes = R.layout.en_floating_view)
        private val tempViewToSupportFragment = ArrayMap<View, Fragment>()

        @Volatile
        private var applicationManager: FloatingManager? = null
        fun config(config: Config = DEFAULT_CONFIG): Builder {
            return Builder(config)
        }
    }


    class Builder(private val config: Config) {

        fun with(context: Context): FloatingManager {
            return get(context, config)
        }

        fun with(activity: Activity): FloatingManager {
            return get(activity, config)
        }

        fun with(fragmentActivity: FragmentActivity): FloatingManager {
            return get(fragmentActivity, config)
        }

        fun with(fragment: Fragment): FloatingManager {
            return get(fragment, config)
        }

        fun with(view: View): FloatingManager {
            return get(view, config)
        }

        private fun get(activity: Activity, config: Config): FloatingManager {
            return get(activity.applicationContext, config)
        }

        private fun get(fragmentActivity: FragmentActivity, config: Config): FloatingManager {
            return LifecycleManagerRetriever.getOrCreate(
                fragmentActivity,
                fragmentActivity.lifecycle,
                config
            )
        }

        private fun get(fragment: Fragment, config: Config): FloatingManager {
            return LifecycleManagerRetriever.getOrCreate(
                fragment.context,
                fragment.lifecycle,
                config
            )
        }

        private fun get(context: Context, config: Config): FloatingManager {/*~~neegrt~~*/
            // Only unwrap a ContextWrapper if the baseContext has a non-null application context.
            // Context#createPackageContext may return a Context without an Application instance,
            // in which case a ContextWrapper may be used to attach one.
            if (isOnMainThread() && context !is Application) {
                if (context is FragmentActivity) {
                    return get((context as FragmentActivity), config)
                } else if (context is ContextWrapper // Only unwrap a ContextWrapper if the baseContext has a non-null application context.
                    // Context#createPackageContext may return a Context without an Application instance,
                    // in which case a ContextWrapper may be used to attach one.
                    && (context as ContextWrapper).baseContext.applicationContext != null
                ) {
                    return get((context as ContextWrapper).baseContext, config)
                }
            }

            return getApplicationManager(context)
        }

        @SuppressLint("RestrictedApi")
        private fun get(view: View, config: Config): FloatingManager {
            if (isOnBackgroundThread()) {
                return get(view.context.applicationContext, config)
            }

            Preconditions.checkNotNull(view)
            Preconditions.checkNotNull(
                view.context, "Unable to obtain a request manager for a view without a Context"
            )
            val activity =
                findActivity(view.context) ?: return get(view.context.applicationContext, config)
            // The view might be somewhere else, like a service.

            // Support Fragments.
            // Although the user might have non-support Fragments attached to FragmentActivity, searching
            // for non-support Fragments is so expensive pre O and that should be rare enough that we
            // prefer to just fall back to the Activity directly.
            if (activity is FragmentActivity) {
                val fragment = findSupportFragment(view, activity)
                return if (fragment != null) get(fragment, config) else get(activity, config)
            }

            // Standard Fragments.
            return get(view.context.applicationContext, config)
        }

        private fun findAllSupportFragmentsWithViews(
            topLevelFragments: Collection<Fragment>?, result: MutableMap<View?, Fragment>
        ) {
            if (topLevelFragments == null) {
                return
            }
            for (fragment in topLevelFragments) {
                // getFragment()s in the support FragmentManager may contain null values, see #1991.
                if (fragment.view == null) {
                    continue
                }
                result[fragment.view] = fragment
                findAllSupportFragmentsWithViews(fragment.childFragmentManager.fragments, result)
            }
        }

        private fun findSupportFragment(target: View, activity: FragmentActivity): Fragment? {
            tempViewToSupportFragment.clear()
            findAllSupportFragmentsWithViews(
                activity.supportFragmentManager.fragments, tempViewToSupportFragment
            )
            var result: Fragment? = null
            val activityRoot = activity.findViewById<View>(android.R.id.content)
            var current = target
            while (current != activityRoot) {
                result = tempViewToSupportFragment[current]
                if (result != null) {
                    break
                }
                if (current.parent is View) {
                    current = current.parent as View
                } else {
                    break
                }
            }

            tempViewToSupportFragment.clear()
            return result
        }

        private fun findActivity(context: Context): Activity? {
            return if (context is Activity) {
                context as Activity
            } else if (context is ContextWrapper) {
                findActivity((context as ContextWrapper).baseContext)
            } else {
                null
            }
        }

        private fun getApplicationManager(context: Context): FloatingManager {
            // Either an application context or we're on a background thread.
            if (applicationManager == null) {
                synchronized(this) {
                    if (applicationManager == null) {
                        // Normally pause/resume is taken care of by the fragment we add to the fragment or
                        // activity. However, in this case since the manager attached to the application will not
                        // receive lifecycle events, we must force the manager to start resumed using
                        // ApplicationLifecycle.
                        applicationManager = FloatingManager.Builder().build(context, ApplicationLifecycle(), config)
                    }
                }
            }

            return applicationManager!!
        }
    }
}

