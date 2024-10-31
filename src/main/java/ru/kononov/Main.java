package ru.kononov;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CustomThreadPool customThreadPool = new CustomThreadPool(5);
        for(int i=0;i<=100;i++) {
            int taskNumber = i;
            customThreadPool.execute(()->{
                System.out.println("I'm a new task " + taskNumber);
            });
        }
        customThreadPool.shutdown();
        if(customThreadPool.awaitTermination())
        {
            System.out.println("----------------");
            System.out.println("Все задания выполнены! Работа потоков завершена.");
            customThreadPool.printThreadStates();
        }
    }
}
