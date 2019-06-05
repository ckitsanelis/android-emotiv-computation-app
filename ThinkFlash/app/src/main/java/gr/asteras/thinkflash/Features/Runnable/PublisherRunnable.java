package gr.asteras.thinkflash.Features.Runnable;


import gr.asteras.thinkflash.Features.MqttPublisher;

public class PublisherRunnable implements Runnable{

    private String server;
    private String message;
    private String id;

    public PublisherRunnable(String server, String message, String id) {
        this.server = server;
        this.message = message;
        this.id = id;
    }

    @Override
    public void run() {
        MqttPublisher publisher = new MqttPublisher(server,id);
        publisher.sendMessage(message);
    }
}
