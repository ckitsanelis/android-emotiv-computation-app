package gr.asteras.thinkflash.Features.Runnable;


import android.os.Handler;

import gr.asteras.thinkflash.Features.MqttSubscriber;

public class SubscriberRunnable implements Runnable{

    private String server;
    private String id;
    private Handler handler;
    private MqttSubscriber subscriber;
    private final Object obj;

    public SubscriberRunnable(String server, Handler handler, String id) {
        this.server = server;
        this.handler = handler;
        this.id = id;
        obj = new Object();
    }

    @Override
    public void run() {
        subscriber = new MqttSubscriber(server,handler,id);
        synchronized (obj) {
            try {
                obj.wait();
            } catch (InterruptedException e) {}
        }
        subscriber.unsubscribe();
    }

    public void destroy() {
        synchronized (obj) {
            obj.notify();
        }
    }
}