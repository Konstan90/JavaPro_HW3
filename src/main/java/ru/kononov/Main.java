package ru.kononov;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CustomThreadPool customThreadPool = new CustomThreadPool(5);
        for(int i=0;i<=10000;i++) {
            int taskNumber = i;
            customThreadPool.execute(()->{
                System.out.println("I'm a new task " + taskNumber);
            });
        }
        Thread.sleep(1000);
        customThreadPool.shutdown();

    }
}
