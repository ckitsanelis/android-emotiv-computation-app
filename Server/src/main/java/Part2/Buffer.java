package Part2;


import java.util.ArrayList;
import java.util.List;

public class Buffer {

    private List<String> buffer;

    public Buffer() {
        buffer = new ArrayList<>();
    }

    public String pull() {
        String message;
        synchronized (this) {
            if(!buffer.isEmpty()) {
                message = String.valueOf(buffer.get(0));
                buffer.remove(0);
            }
            else {
                message = "Empty";
            }
        }
        return message;
    }

    public void push(String message) {
        synchronized (this) {
            if(message != null)
                buffer.add(message);
        }
    }

    public Boolean isEmpty() {
        return buffer.isEmpty();
    }


    public void empty() {
        synchronized (this) {
            for (int i = buffer.size() - 1 ; i >= 0 ; i--)
                buffer.remove(i);
        }
    }
}

