package gr.asteras.thinkflash.Features.Dialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import gr.asteras.thinkflash.R;

public class NoWifiDialog {

    public NoWifiDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.wifi_dialog_title);
        builder.setMessage(R.string.wifi_dialog_body);
        builder.setPositiveButton("Do it", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.finishAffinity();
            }
        });

        builder.create().show();
    }
}
