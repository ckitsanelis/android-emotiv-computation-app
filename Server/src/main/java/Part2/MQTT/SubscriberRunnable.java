package Part2.MQTT;


public class SubscriberRunnable implements Runnable{

    private PublisherRunnable publisherRunnable;
    private final Object obj;

    public SubscriberRunnable(PublisherRunnable publisherRunnable) {
        obj = new Object();
        this.publisherRunnable = publisherRunnable;
    }

    @Override
    public void run() {
        MQTTSubscriber subscriber = new MQTTSubscriber(publisherRunnable);
//        publisherRunnable = null;
        synchronized (obj) {
            try {
                obj.wait();
            } catch (InterruptedException e) {}
        }
        subscriber.disconnect();
    }

    public void terminate() {
        synchronized (obj) {
            obj.notify();
        }
        System.out.println("Killing subscriber");
    }
}
