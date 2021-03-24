package com.atguigu.test;

public class LsatDemo {

    public static void main(String[] args){
        LastThread lastThread = new LastThread();

        new Thread(() -> {
            for (int i = 0;i <= 10;i++){
                try {
                    lastThread.add();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0;i <= 10;i++){
                try {
                    lastThread.sub();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
