package gr.asteras.thinkflash.Features.SeekbarListener;


import android.widget.SeekBar;
import android.widget.TextView;

public class RefreshListener implements SeekBar.OnSeekBarChangeListener{

    int refresh;
    TextView text;

    public RefreshListener(TextView textView) {
        text = textView;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        refresh = i + 1;
        String temp = refresh + " seconds";
        text.setText(temp);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public int retValue() {
        return this.refresh;
    }
}
