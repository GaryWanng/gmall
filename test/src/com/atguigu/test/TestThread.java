package com.atguigu.test;

/**
 * 要求：一个线程 +1  一个线程 -1   默认初始值为0      循环10次   输出：01010101
 *
 * 错误代码如下
 * 现象：程序无法结束，并不是2个线程交互进行。而是在争夺CPU资源
 * 原因：死锁，两个不同线程争抢资源同时进入锁中无法出去
 */

class TestThread implements Runnable {
    int i = 1;
    int count = 1;
    boolean b = true;

    @Override
    public void run() {
        while (b) {
            if (count <= 10){
                synchronized (this) {
                    notify();
                    try {
                        //Thread.currentThread();
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i == 0) {
                        i++;
                        System.out.print(i);
                        count++;
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        i--;
                        System.out.print(i);
                        count++;
                    }
                }
            }else {
                break;
            }
        }
    }
}