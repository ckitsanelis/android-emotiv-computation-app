package gr.asteras.thinkflash.Features.Runnable;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import static java.lang.Thread.sleep;

public class ConnectionRunnable implements Runnable{

    private Boolean on;
    private int sleepTime;
    private Handler handler;
    private ConnectivityManager cm;

    public ConnectionRunnable(ConnectivityManager cm,Handler handler) {
        on = true;
        sleepTime = 5000;
        this.cm = cm;
        this.handler = handler;
    }

    @Override
    public void run() {
        while(on) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            boolean isWifi = isConnected && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI);
            boolean is3G = isConnected && (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
            if (!isWifi && !is3G) {
                handler.sendEmptyMessage(0);
                try {
                    sleep(sleepTime * 4);
                } catch (Exception e) {
                }
            }
            else{
                try {
                    sleep(sleepTime);
                } catch (Exception e) { }
            }
        }
    }

    public void destroy() {
        on = false;
    }
}