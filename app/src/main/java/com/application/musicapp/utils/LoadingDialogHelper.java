package com.application.musicapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.application.musicapp.R;

public class LoadingDialogHelper {

    private Dialog dialog;

    public LoadingDialogHelper(Context context) {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(false);
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

