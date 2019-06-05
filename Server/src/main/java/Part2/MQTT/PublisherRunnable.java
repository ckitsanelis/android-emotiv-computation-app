package Part2.MQTT;


import Part2.Buffer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PublisherRunnable implements Runnable{

    private Boolean cont;
    private int sleepDuration;
    private Buffer buffer;
    private MQTTPublisher publisher;
    private List<String> users;

    public PublisherRunnable(int duration, Buffer buffer) {
        cont = true;
        sleepDuration = duration;
        this.buffer = buffer;
        publisher = new MQTTPublisher();
        users = new ArrayList<>();
    }

    @Override
    public void run() {
        String fileName = "Files/Output/Android_Stats.txt";
        PrintWriter writer = null;
        try {
            File file = new File(fileName);
            file.delete();
            file.createNewFile();
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            writer = new PrintWriter(bw);
            writer.println("LABEL\t\t\tPREDICTION\t\tRESULT\n");
            writer.close();
        } catch(IOException ioe) {
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
        }
        while(cont) {
            if (users.size() > 0) {
                String bufferString = buffer.pull();
                if (!bufferString.equals("Empty")) {
                    writeToFile(bufferString,writer,fileName);
                    String command = bufferString.substring(0,19);
                    publisher.sendMessage(command);
                }
                try {
                    System.out.println("Publisher:Sleeping for " + sleepDuration + " after sending a message");
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    System.out.println("Publisher Thread was interrupted!");
                }
            }
            else {
                publisher.sendMessage("NEED USER");
                try {
                    System.out.println("Publisher:Sleeping for " + sleepDuration + " because there are no users connected");
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException e) {
                    System.out.println("Publisher Thread was interrupted!");
                }
            }
        }
        publisher.disconnect();
    }

    public void changeSleep(int duration) {
        sleepDuration = duration;
    }

    public void terminate() {
        cont = false;
        System.out.println("Killing publisher");
    }

    public void addUser(String user) {
        if (!users.contains(user))
            users.add(user);
    }

    public void removeUser(String user) {
        users.remove(user);
    }

    private void writeToFile(String bufferString, PrintWriter writer, String fileName) {
        String result     = bufferString.substring(19);
        String prediction = bufferString.substring(8,19);
        File file = new File(fileName);
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            writer = new PrintWriter(bw);
        } catch(IOException ioe) {
            System.out.println("Exception occurred:");
            ioe.printStackTrace();
        }
        if (result.equals("Success"))
            writer.println(prediction + "\t\t" + prediction + "\t\t" + result);
        else {
            if (prediction.contains("Opened"))
                writer.println("Eyes Closed\t\t" + prediction + "\t\t" + result);
            else
                writer.println("Eyes Opened\t\t" + prediction + "\t\t" + result);
        }
        writer.close();
    }
}
