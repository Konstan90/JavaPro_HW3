package ru.kononov;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CustomThreadPool {
    private LinkedList<Runnable> tasksList;
    private List<CustomThread> threads;
    private boolean isOff = false;
    private Thread dThread;

    public CustomThreadPool(int size) {
        if(size<1) throw new IllegalArgumentException();
        this.tasksList = new LinkedList<>();
        threads = new ArrayList<>();
        for(int i=0;i<size;i++) {
            CustomThread newThread = new CustomThread(tasksList);
            threads.add(newThread);
            newThread.start();
        }
        daemon();
    }

    public void execute(Runnable task) {
        if(isOff) throw new IllegalStateException();
        else tasksList.add(task);
    }

    private void daemon() {
        Thread daemonThread = new Thread(()->{
            while (true) {
                for (int i=0;i<tasksList.size();i++)
                {
                    for (CustomThread thread : threads) {
                        if(thread.task==null) {
                            thread.setTask(tasksList.get(i));
                            tasksList.remove(i);
                            break;
                        }
                    }
                }

                if(tasksList.size()==0 && isOff) break;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        daemonThread.isDaemon();
        daemonThread.start();
        dThread = daemonThread;
    }

    public void shutdown() {
        isOff = true;
//        Если нужно перевести потоки в TERMINATED
//        while (true){
//            if(tasksList.size()==0){
//                for (CustomThread thread : threads) {
//                    thread.setOff(true);
//                }
//                dThread.interrupt();
//            }
//        }
    }

    public boolean awaitTermination() throws InterruptedException {
        while (true){
            if(tasksList.size()==0){ //Дожидаемся когда все задания будут выполнены
                for (CustomThread thread : threads) {
                    System.out.println("Останавливаю поток " + thread.getName());
                    thread.setOff(true);
                    while (true) {
                        if(thread.getState() == Thread.State.TERMINATED) break;
                    }
                }
                dThread.interrupt();
                return true;
            }
            Thread.sleep(100);
        }
    }
    public void printThreadStates() {
        System.out.println("----------------");
        System.out.println("Статусы потоков:");
        for (CustomThread thread : threads) {
            System.out.println(thread.getName() + ": " + thread.getState());
        }
    }
}
