package com.application.musicapp.register.forms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.application.musicapp.R;
import com.application.musicapp.basic.BaseFragment;
import com.application.musicapp.register.RegisterActivity;
import com.application.musicapp.register.RegisterViewModel;
import com.application.musicapp.register.RegistrationFragmentChangeListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class EmailVerificationFragment extends BaseFragment {

    private RegisterViewModel viewModel;
    private RegistrationFragmentChangeListener mListener;

    private TextView etEmail;
    private TextView btResend;
    private boolean timerRunning = false;
    private CountDownTimer resendTimer;

    private static final long RESEND_TIMEOUT = 60 * 2000; // 2 minute

    public EmailVerificationFragment() {
        // Required empty public constructor
    }
    

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (requireActivity() instanceof RegisterActivity) {
            RegisterActivity activity = (RegisterActivity) requireActivity();
            activity.setToolbarTitle(5);
        }
        return inflater.inflate(R.layout.fragment_email_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initInitial();
        initViews();
        initObservers();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void initInitial() {
        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
    }

    @Override
    public void initViews() {
        View view = requireView();
        btResend = view.findViewById(R.id.btResend);
        etEmail = view.findViewById(R.id.etEmail);
    }

    @Override
    public void initObservers() {

        viewModel.getEmail().observe(getViewLifecycleOwner(), email -> {
            etEmail.setText(email);
        });

        startResendTimer();

        btResend.setOnClickListener(v-> {
            if (!timerRunning) {
                mListener.resendEmailVerification();
                startResendTimer();
            }
        });


    }

    private void startResendTimer() {
        disableResend();
        timerRunning = true;
        resendTimer = new CountDownTimer(RESEND_TIMEOUT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTimerText(millisUntilFinished);
                mListener.verifyEmailAndCreateUser();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                enableResend();
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    private void updateTimerText(long millisUntilFinished) {
        long secondsLeft = millisUntilFinished / 1000;
        btResend.setText("Time left: " + secondsLeft);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationFragmentChangeListener) {
            mListener = (RegistrationFragmentChangeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        if (resendTimer != null) {
            resendTimer.cancel();
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null){
            boolean isAccountCreated = Boolean.TRUE.equals(viewModel.getIsAccountCreated().getValue());
            if (!isAccountCreated) {
                mListener.deleteUser();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void enableResend(){
        btResend.setEnabled(true);
        btResend.setTextColor(getResources().getColor(R.color.colorAccent));
        btResend.setText("Resend Code");
    }

    private void disableResend(){
        // Set the TextView to the disabled style
        btResend.setEnabled(false);
        btResend.setTextColor(getResources().getColor(R.color.ash));
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onBack() {

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void navigateToActivity(Class<?> targetActivity) {

    }
}