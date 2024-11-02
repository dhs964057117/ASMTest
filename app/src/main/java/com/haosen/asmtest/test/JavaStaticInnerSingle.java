package com.haosen.asmtest.test;

public class JavaStaticInnerSingle {

    private JavaStaticInnerSingle() {
    }

    private static class JavaStaticInnerSingleHolder {
        public static JavaStaticInnerSingle instance = new JavaStaticInnerSingle();
    }

    public JavaStaticInnerSingle getInstance() {
        return JavaStaticInnerSingleHolder.instance;
    }
}
