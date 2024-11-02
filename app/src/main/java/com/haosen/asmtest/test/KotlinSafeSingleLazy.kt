package com.haosen.asmtest.test

class KotlinSafeSingleLazy private constructor() {

    companion object {
        val instance: KotlinSafeSingleLazy by lazy { KotlinSafeSingleLazy() }
    }
}