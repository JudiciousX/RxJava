package acom.example.myapplication.scheduler;

public abstract class Scheduler {

    public abstract Worker createWorker();


    public interface Worker{
        void schedule(Runnable runnable);
    }
}
