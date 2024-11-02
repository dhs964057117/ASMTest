package com.haosen.asmtest.test;

public class JavaSingleLazy {
    private static JavaSingleLazy instance;

    private JavaSingleLazy() {
    }

    public static JavaSingleLazy getInstance() {
        if (instance == null) {
            instance = new JavaSingleLazy();
        }
        return instance;
    }
}
