package com.example.myapplication.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class DialogUtils {

    public static void showDialog(Context context, String time1, String time2, final Listener listener) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        CharSequence items[] = new CharSequence[]{time1, time2};
        adb.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int n) {
                d.dismiss();
                listener.onClickPosition(n);
            }

        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.setTitle("Which one?");
        adb.show();
    }

    public interface Listener {

        void onClickPosition(int position);
    }

}
