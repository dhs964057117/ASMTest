package com.haosen.asmtest.test;

import java.util.Arrays;

public class JavaSafeSingleLazy {

    private volatile static JavaSafeSingleLazy instance;

    private JavaSafeSingleLazy() {
    }

    public JavaSafeSingleLazy getInstance() {
            if (instance == null) {
                synchronized (JavaSafeSingleLazy.class) {
                    instance = new JavaSafeSingleLazy();
                }
        }
        return instance;
    }
}
