package com.haosen.asmtest.test;

public class JavaSingle {
    private static final JavaSingle instance = new JavaSingle();

    public static JavaSingle getInstance() {
        return instance;
    }

    private JavaSingle() {
    }
}
