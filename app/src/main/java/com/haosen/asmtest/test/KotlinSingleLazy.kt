package com.haosen.asmtest.test

class KotlinSingleLazy private constructor() {

    companion object {
        private val instance: KotlinSingleLazy? = null
            get() {
                return field ?: KotlinSingleLazy()
            }

        fun instance() = instance
    }
}