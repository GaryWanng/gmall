package com.atguigu.test;

public class demo2 {

    public static void main(String[] args) {
        /*只有一个TestThread对象*/
        TestThread t = new TestThread();
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);

        t1.start();
        t2.start();

    }
}
