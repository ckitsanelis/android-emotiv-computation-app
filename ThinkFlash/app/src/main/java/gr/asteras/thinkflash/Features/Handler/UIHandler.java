package gr.asteras.thinkflash.Features.Handler;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import gr.asteras.thinkflash.Features.Countdown.CountDownDisplay;
import gr.asteras.thinkflash.Features.FlashAndSound;
import gr.asteras.thinkflash.R;


public class UIHandler extends Handler {

    private int duration;
    private Boolean mode, userInput;
    private TextView statusText;
    private TextView countdownText;
    private CountDownDisplay countdown;
    private FlashAndSound fs;
    private Handler cancelHandler;
    private Toast toastError, toastCool, toastConnectLost, toastOpenRepeat, toastCloseRepeat;
    private Runnable cancelRunnable = new Runnable() {
        @Override
        public void run() {
            userOff();
        }
    };


    public UIHandler(int duration, TextView statusText, TextView countdownText, FlashAndSound fs, Button button, Context context) {
        mode = false;
        userInput = true;
        this.fs = fs;
        this.duration = duration;
        this.statusText = statusText;
        this.countdownText = countdownText;
        countdown = new CountDownDisplay(countdownText,duration);
        cancelHandler = new Handler();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode) {
                    userOff();
                    cancelHandler.removeCallbacks(cancelRunnable);
                }
                else {
                    userOn();
                }
            }
        });
        toastCool = Toast.makeText(context,"Connected to broker",Toast.LENGTH_LONG);
        toastConnectLost = Toast.makeText(context,"Lost Connection",Toast.LENGTH_LONG);
        toastOpenRepeat = Toast.makeText(context,R.string.same_opened_message,Toast.LENGTH_SHORT);
        toastCloseRepeat = Toast.makeText(context,R.string.same_closed_message,Toast.LENGTH_SHORT);
        toastError = Toast.makeText(context,R.string.connection_error,Toast.LENGTH_LONG);
        TextView text = (TextView) toastError.getView().findViewById(android.R.id.message);
        if (text != null)
            text.setGravity(Gravity.CENTER);
    }

    public void renew(int duration) {
        this.duration = duration;
        countdown.end();
        countdown = new CountDownDisplay(countdownText,duration);
    }

    @Override
    public void handleMessage(Message msg) {
        cancelHandler.removeCallbacks(cancelRunnable);
        switch(msg.what) {
            //MUSIC AND FLASH ON
            case 0:
                turnBothOn();
                break;
            //MUSIC ON
            case 1:
                turnMusicOn();
                break;
            //FLASH ON
            case 2:
                turnFlashOn();
                break;
            //MUSIC AND FLASH OFF
            case 3:
                turnOff();
                break;
            //NO BROKER CONNECTION
            case 4:
                toastError.show();
                break;
            //CONNECTED
            case 5:
                toastCool.show();
                break;
            //LOST CONNECTION
            case 6:
                toastConnectLost.show();
                break;
            //UNSUB FROM TOPIC
            case 7:
                userOff();
        }
    }


    private void turnMusicOn() {
        countdown.end();
        fs.stop(false,true);
        mode = true;
        statusText.setText(R.string.musicOn);
        countdown.begin();
        fs.startMusic();
        cancelHandler.postDelayed(cancelRunnable,duration);
    }

    private void turnFlashOn() {
        countdown.end();
        fs.stop(true,false);
        mode = true;
        statusText.setText(R.string.flashOn);
        countdown.begin();
        fs.startFlash();
        cancelHandler.postDelayed(cancelRunnable,duration);
    }

    private void turnBothOn() {
        countdown.end();
        if (!mode || userInput)
            statusText.setText(R.string.eyesOpened);
        if (mode) {
            fs.stop(true,true);
            if (duration >= 2000 && !userInput)
                toastOpenRepeat.show();
        }
        else
            mode = true;
        countdown.begin();
        fs.startBoth();
        cancelHandler.postDelayed(cancelRunnable,duration);
        userInput = false;
    }

    private void turnOff() {
        if (mode || userInput)
            statusText.setText(R.string.eyesClosed);
        if (mode) {
            mode = false;
            fs.stop(true, true);
            countdown.end();
        }
        else {
            if (duration >= 2000 && !userInput)
                toastCloseRepeat.show();
        }
        userInput = false;
    }

    private void userOn() {
        mode = true;
        statusText.setText(R.string.bothOn);
        fs.startBoth();
        userInput = true;
    }

    private void userOff() {
        mode = false;
        statusText.setText(R.string.bothOff);
        fs.stop(true,true);
        countdown.end();
        userInput = true;
    }
}
