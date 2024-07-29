package com.application.musicapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.application.musicapp.basic.BaseActivity;
import com.application.musicapp.login.LoginActivity;
import com.application.musicapp.utils.PrefHelper;

public class MainActivity extends BaseActivity {
    private Button btBack;
    private PrefHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInitial();
        initViews();
        initObservers();
    }

    @Override
    public void initInitial() {
        prefHelper = new PrefHelper(this);
    }

    @Override
    public void initViews() {
        btBack = findViewById(R.id.btBack);
    }

    @Override
    public void initObservers() {
        btBack.setOnClickListener(v -> {
            prefHelper.clearPreferences();
            navigateToActivity(LoginActivity.class);
        });
    }

    @Override
    public void onNext() {

    }
}