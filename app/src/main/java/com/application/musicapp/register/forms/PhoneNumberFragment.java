package com.application.musicapp.register.forms;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
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
import com.application.musicapp.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;


public class PhoneNumberFragment extends BaseFragment {

    private static final String TAG = "PhoneNumberFragment";

    private RegisterViewModel viewModel;
    private RegistrationFragmentChangeListener mListener;
    private TextView btSkip;
    private AutoCompleteTextView spCountryCode;
    private TextInputEditText etPhone;
    private TextInputLayout phoneLayout;
    private Button btNext;

    public PhoneNumberFragment() {
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
        return inflater.inflate(R.layout.fragment_phone_number, container, false);
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
        btSkip = view.findViewById(R.id.btSkip);
        spCountryCode = view.findViewById(R.id.autoCompleteCountryCode);
        etPhone = view.findViewById(R.id.phone);
        btNext = view.findViewById(R.id.btNext);
        phoneLayout = view.findViewById(R.id.phoneLayout);
    }

    @Override
    public void initObservers() {

        bindAutoCompleteTextView(spCountryCode,getResources().getStringArray(R.array.country_codes));

        btSkip.setOnClickListener(v->{
            requireActivity().finish();
        });

        viewModel.getCountryCode().observe(getViewLifecycleOwner(), countryCode -> {
            spCountryCode.setText(countryCode);
        });

        viewModel.getPhoneNumber().observe(getViewLifecycleOwner(), phone -> {
            etPhone.setText(phone);
        });

        btNext.setOnClickListener(v->{
            String phone = etPhone.getText().toString().trim();
            String countryCode = spCountryCode.getText().toString().trim();

            if (phone.isEmpty()){
                phoneLayout.setError("Please enter your phone number");
            }else if (!ValidationUtils.validatePhoneNumber(phone)){
                phoneLayout.setError("Please enter valid phone number");
            }else{
                viewModel.setCountryCode(countryCode);
                viewModel.setPhoneNumber(phone);
                mListener.verifyPhoneNumber(countryCode+phone);
            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String mail = s.toString().trim();
                if (mail.isEmpty()){
                    phoneLayout.setError(null);
                }else if (ValidationUtils.validatePhoneNumber(mail)){
                    phoneLayout.setError(null);
                }else {
                    phoneLayout.setError("Please enter valid phone number");
                }
            }
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
        mListener.navigateToPhoneNumberVerificationFragment();
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