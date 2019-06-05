package gr.asteras.thinkflash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import gr.asteras.thinkflash.Features.SeekbarListener.RefreshListener;


public class SettingsActivity extends AppCompatActivity {

    private int val;
    private Boolean closeApp;
    private TextView durationValue;
    private SeekBar durationBar;
    private EditText ipText;
    private EditText portText;
    private Button exitButton;
    static final int CODE_REQUEST = 13;
    private RefreshListener refreshListener;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        val = 6;
        closeApp = false;
        String displayText = val + " seconds";
        durationValue = (TextView)findViewById(R.id.durationValue);
        durationValue.setText(displayText);
        refreshListener = new RefreshListener(durationValue);
        durationBar = (SeekBar)findViewById(R.id.durationBar);
        durationBar.setProgress(5);
        durationBar.setOnSeekBarChangeListener(refreshListener);
        ipText = (EditText) findViewById(R.id.ipEdit);
        ipText.setText(R.string.initialIP);
        portText = (EditText) findViewById(R.id.portEdit);
        portText.setText(R.string.initialPort);
        mPreferences = getPreferences(MODE_PRIVATE);
        exitButton = (Button)findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeApp = true;
                returnToMain();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        val = mPreferences.getInt("duration",6);
        String displayText = val + " seconds";
        durationValue.setText(displayText);
        durationBar.setProgress(val - 1);
        ipText.setText(mPreferences.getString("ip",getResources().getString(R.string.initialIP)));
        portText.setText(mPreferences.getString("port",getResources().getString(R.string.initialPort)));
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("duration",val);
        editor.putString("ip",ipText.getText().toString());
        editor.putString("port",portText.getText().toString());
        editor.apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                returnToMain();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        returnToMain();
    }

    public void returnToMain() {
        val = refreshListener.retValue();
        Intent returnToMain = new Intent();
        returnToMain.putExtra("duration",val);
        returnToMain.putExtra("ip",ipText.getText().toString());
        returnToMain.putExtra("port",portText.getText().toString());
        returnToMain.putExtra("closeApp",closeApp);
        setResult(Activity.RESULT_OK,returnToMain);
        finish();
    }
}
