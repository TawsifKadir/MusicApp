package com.application.musicapp.utils;

import android.content.Context;

public class DialogUtils {

    private final Context context;
    private static GenericDialog currentDialog;

    public DialogUtils(Context context){
        this.context = context;
    }

    public void showDialog(String message, String subtitle,GenericDialog.DialogCallback callback) {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismissDialog();
        }
        currentDialog = new GenericDialog(context);
        currentDialog.setMessage(message,subtitle);
        currentDialog.setCallback(callback);
        currentDialog.show();
    }

    public static void dismissGenericDialog() {
        if (currentDialog != null && currentDialog.isShowing()) {
            currentDialog.dismissDialog();
        }
    }
}
