package gr.asteras.thinkflash.Features.Countdown;


import android.os.CountDownTimer;
import android.widget.TextView;


public class CountDownDisplay {

    private CountDownTimer timer;
    private TextView text;

    public CountDownDisplay(final TextView textview, int time) {
        text = textview;
        timer = new CountDownTimer(time, 100) {
            public void onTick(long millisUntilFinished) {
                double secLeft = (double)millisUntilFinished / 1000.0;
                String timeString = secLeft + " seconds left";
                text.setText(timeString);
            }

            public void onFinish() {
                text.setText("");
            }
        };
    }

    public void begin() {
        timer.start();
    }

    public void end() {
        timer.cancel();
        text.setText("");
    }

}
