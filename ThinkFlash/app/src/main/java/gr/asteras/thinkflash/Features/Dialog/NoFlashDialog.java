package gr.asteras.thinkflash.Features.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import gr.asteras.thinkflash.R;

public class NoFlashDialog {

    public NoFlashDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.error_dialog_title);
        builder.setMessage(R.string.error_dialog_body);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.finishAffinity();
            }
        });

        builder.create().show();
    }
}