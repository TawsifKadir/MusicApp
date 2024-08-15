package com.application.musicapp.register.forms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.application.musicapp.R;
import com.application.musicapp.basic.BaseFragment;
import com.application.musicapp.register.RegisterActivity;
import com.application.musicapp.register.RegisterViewModel;
import com.application.musicapp.register.RegistrationFragmentChangeListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


public class PhoneNumberVerificationFragment extends BaseFragment {

    private RegisterViewModel viewModel;
    private RegistrationFragmentChangeListener mListener;
    private TextInputEditText[] otpInputs = new TextInputEditText[6];
    private TextView helperText;
    private String phoneNumber;
    private Button btNext;
    private TextView btResend;

    public PhoneNumberVerificationFragment() {
        // Required empty public constructor
    }
    

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (requireActivity() instanceof RegisterActivity) {
            RegisterActivity activity = (RegisterActivity) requireActivity();
            activity.setToolbarTitle(6);
        }
        return inflater.inflate(R.layout.fragment_phone_number_verification, container, false);
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
        phoneNumber = viewModel.getCountryCode().getValue() + viewModel.getPhoneNumber().getValue();
    }

    @Override
    public void initViews() {
        View view = requireView();
        helperText = view.findViewById(R.id.textView4);
        otpInputs[0] = view.findViewById(R.id.otp_input_1);
        otpInputs[1] = view.findViewById(R.id.otp_input_2);
        otpInputs[2] = view.findViewById(R.id.otp_input_3);
        otpInputs[3] = view.findViewById(R.id.otp_input_4);
        otpInputs[4] = view.findViewById(R.id.otp_input_5);
        otpInputs[5] = view.findViewById(R.id.otp_input_6);
        btNext = view.findViewById(R.id.btNext);
        btResend = view.findViewById(R.id.btResend);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initObservers() {

        for (int i = 0; i < otpInputs.length; i++) {
            final int index = i;
            otpInputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1) {
                        if (index < otpInputs.length - 1) {
                            otpInputs[index + 1].requestFocus();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0 && index > 0) {
                        otpInputs[index - 1].requestFocus();
                    }
                }
            });
        }

        helperText.setText(getResources().getString(R.string.have_sent_code) + " " + phoneNumber);

        btResend.setOnClickListener(v-> {
            mListener.resendOTP();
        });

        btNext.setOnClickListener(v -> {
            verifyCode();
        });

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

    private String getOtp(){
        StringBuilder OTP = new StringBuilder();

        for (int i= 0; i< otpInputs.length; i++){
            if (otpInputs[i].getText().toString().isEmpty()){
                return null;
            }else{
                OTP.append(otpInputs[i].getText().toString().trim());
            }
        }
        return OTP.toString();
    }

    private void verifyCode(){
        String OTP = getOtp();
        String verificationCode = mListener.getVerificationId();
        if (OTP == null || OTP.isEmpty()){
            showToast("Please enter OTP");
        } else if (verificationCode == null || verificationCode.isEmpty()){
            showToast("No Verification ID");
        } else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, OTP);
            mListener.updatePhoneNumber(credential);
        }
    }

}