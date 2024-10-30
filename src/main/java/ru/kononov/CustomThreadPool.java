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
        tasksList.add(task);
    }

    private void daemon() {
        Thread daemonThread = new Thread(()->{
            while (true) {
                if(isOff) break;
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
        for (CustomThread thread : threads) {
            thread.setOff(true);
        }
    }
}
