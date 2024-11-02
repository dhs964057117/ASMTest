package com.haosen.asmtest.test

class KotlinStaticInnerSingle private constructor() {

    companion object {
        var instance: KotlinStaticInnerSingle? = null
            get() {
                if (field == null)
                    KotlinStaticInnerSingleHolder.holder
                return field
            }

    }

    private object KotlinStaticInnerSingleHolder {
        val holder = KotlinStaticInnerSingle()
    }
}