package com.application.musicapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.application.musicapp.R;

public class LoadingDialogHelper {

    private Dialog dialog;

    public LoadingDialogHelper(Context context) {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT; // Or a specific size like 80% of the screen width
        dialog.getWindow().setAttributes(params);
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

