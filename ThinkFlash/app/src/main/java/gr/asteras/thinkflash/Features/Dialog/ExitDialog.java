package gr.asteras.thinkflash.Features.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import gr.asteras.thinkflash.Features.Runnable.PublisherRunnable;
import gr.asteras.thinkflash.R;

public class ExitDialog {

    public ExitDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_body);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activity.finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }
}