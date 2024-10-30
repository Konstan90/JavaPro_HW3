package ru.kononov;

import java.util.LinkedList;

public class CustomThread extends Thread{
    public Runnable task=null;
    LinkedList<Runnable> tasksList;
    private Thread daemonThread;
    boolean isOff = false;
    public CustomThread(LinkedList<Runnable> tasks) {
        tasksList = tasks;
    }

    public void setOff(boolean off) {
        isOff = off;
    }

    @Override
    public void run() {
        while(true) {
            if(this.task != null) {
                System.out.println("Запуск задания на потоке "+ this.getName());
                this.task.run();
                this.task=null;
            }
            try {
                Thread.sleep(1);
                if(isOff) break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void setTask(Runnable t) {
        this.task = t;
    }

}
