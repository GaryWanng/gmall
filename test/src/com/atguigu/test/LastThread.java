package com.atguigu.test;

public class LastThread {

    private int num = 0;


    public synchronized void add() throws InterruptedException {
        //判断
        while (num != 0){
            this.wait();
        }
        //操作
        System.out.print(num);

        num++;
        //通知唤醒
        this.notifyAll();
    }


    public synchronized void sub() throws InterruptedException {
        //判断
        while (num == 0){
            this.wait();
        }
        //操作
        System.out.print(num);

        num--;
        //通知唤醒
        this.notifyAll();
    }
}
