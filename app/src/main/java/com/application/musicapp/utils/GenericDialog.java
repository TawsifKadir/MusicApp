package com.application.musicapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.application.musicapp.R;

public class GenericDialog extends Dialog {

    private String message;
    private String subtitle;
    private DialogCallback callback;

    public GenericDialog(@NonNull Context context) {
        super(context);
    }

    public void setMessage(String message,String subTitle) {
        this.message = message;
        this.subtitle = subTitle;
    }

    public void setCallback(DialogCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_generic);

        TextView dialogMessage = findViewById(R.id.dialogMessage);
        TextView subMessage = findViewById(R.id.subtitleMessage);
        dialogMessage.setText(message);
        subMessage.setText(subtitle);

        Button okButton = findViewById(R.id.dialogOkButton);
        okButton.setOnClickListener(v -> {
            if (callback != null) {
                callback.onOkClicked();
            }
            dismiss();
        });

        Button cancelButton = findViewById(R.id.dialogCancelButton);
        cancelButton.setOnClickListener(v -> dismiss());
    }

    @Override
    public void show() {
        super.show();
        // Set the dialog width to 80% of the screen width
        if (getWindow() != null) {
            getWindow().setLayout((int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.8),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    // Method to dismiss the dialog
    public void dismissDialog() {
        if (isShowing()) {
            dismiss();
        }
    }

    public interface DialogCallback {
        void onOkClicked();
    }
}
