package gr.asteras.thinkflash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;

import gr.asteras.thinkflash.Features.Dialog.NoWifiDialog;
import gr.asteras.thinkflash.Features.FlashAndSound;
import gr.asteras.thinkflash.Features.Dialog.ExitDialog;
import gr.asteras.thinkflash.Features.Dialog.NoFlashDialog;
import gr.asteras.thinkflash.Features.Runnable.ConnectionRunnable;
import gr.asteras.thinkflash.Features.Runnable.PublisherRunnable;
import gr.asteras.thinkflash.Features.Runnable.SubscriberRunnable;
import gr.asteras.thinkflash.Features.Handler.UIHandler;


public class AppMainActivity extends AppCompatActivity {

    private int duration;
    private String server;
    private String uniqueID;
    private static final int SETTINGS_REQUEST = 13;

    private Button toggleButton;
    private TextView timeView;
    private TextView flashView;

    private MediaPlayer mp;
    private FlashAndSound fs;
    private UIHandler uiHandler;
    private SubscriberRunnable subscriberRunnable;
    private ConnectionRunnable connectionRunnable;
    private Thread subscriberThread;
    private Thread connectionThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setLogo(R.drawable.light_man);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

//        if(!getApplicationContext().getPackageManager()
//                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
//            NoFlashDialog dialog = new NoFlashDialog(this);
//        }

        duration = 6000;
        uniqueID = UUID.randomUUID().toString();
        server = "tcp://" + getResources().getString(R.string.initialIP) + ":" + getResources().getString(R.string.initialPort);
        flashView = (TextView) findViewById(R.id.flashText);
        timeView = (TextView) findViewById(R.id.timerText);
        toggleButton = (Button) findViewById(R.id.toggleButton);
        mp = MediaPlayer.create(AppMainActivity.this,R.raw.x_files);
        mp.setLooping(false);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            fs = new FlashAndSound(mp,camManager);
        }
        else {
            fs = new FlashAndSound(mp);
        }
        ConnectivityManager conManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        uiHandler = new UIHandler(duration,flashView,timeView,fs,toggleButton,this);
        @SuppressLint("HandlerLeak") Handler connHandler = new Handler() {
            public void handleMessage(Message msg) {
                new NoWifiDialog(AppMainActivity.this);
            }
        };
        connectionRunnable = new ConnectionRunnable(conManager,connHandler);
        connectionThread = new Thread(connectionRunnable);
        connectionThread.start();
        subscriberRunnable = new SubscriberRunnable(server,uiHandler,uniqueID);
        subscriberThread = new Thread(subscriberRunnable);
        subscriberThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        connectionRunnable.destroy();
        subscriberRunnable.destroy();
        fs.stop(true,true);
        mp.release();
        mp = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivityForResult(settingsIntent, SETTINGS_REQUEST);
                return true;

            case R.id.action_retry:
                retryConnection();
                return true;

            case R.id.action_exit:
                finishAffinity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SETTINGS_REQUEST) {
            if(resultCode == RESULT_OK) {
                Boolean exitBool = data.getBooleanExtra("closeApp",false);
                if(exitBool) {
                    finishAffinity();
                    return;
                }
                int newDuration = data.getIntExtra("duration",6) * 1000;
                String newServer = "tcp://" + data.getStringExtra("ip") + ":" + data.getStringExtra("port");
                checkChanges(newDuration,newServer);
            }
        }
    }

    @Override
    public void onBackPressed() {
        new ExitDialog(this);
    }


    private void checkChanges(int newDuration, String newServer) {
        if(!newServer.equals(server)) {
            server = newServer;
            retryConnection();
        }
        if(newDuration != duration) {
            duration = newDuration;
            uiHandler.renew(duration);
            PublisherRunnable publisherRunnable = new PublisherRunnable(server,String.valueOf(duration),uniqueID);
            Thread publisherThread = new Thread(publisherRunnable);
            publisherThread.start();
        }
    }

    private void retryConnection() {
        subscriberRunnable.destroy();
        subscriberThread = null;
        subscriberRunnable = null;
        subscriberRunnable = new SubscriberRunnable(server,uiHandler,uniqueID);
        subscriberThread = new Thread(subscriberRunnable);
        subscriberThread.start();
    }
}