package acom.example.myapplication.scheduler;


import android.os.Handler;

public class HandlerScheduler extends Scheduler {

    final Handler handler;

    public HandlerScheduler(Handler handler) {
        this.handler = handler;
    }


    @Override
    public Worker createWorker() {
        return new HandlerWorker(handler);
    }

    static final class HandlerWorker implements  Worker {

        final Handler handler;

        HandlerWorker(Handler handler) {
            this.handler = handler;
        }


        @Override
        public void schedule(Runnable runnable) {
            handler.post(runnable);
        }
    }
}
