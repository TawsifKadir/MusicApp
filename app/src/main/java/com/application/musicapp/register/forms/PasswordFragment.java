package com.application.musicapp.register.forms;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.application.musicapp.R;
import com.application.musicapp.basic.BaseFragment;
import com.application.musicapp.register.RegisterActivity;
import com.application.musicapp.register.RegisterViewModel;
import com.application.musicapp.register.RegistrationFragmentChangeListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class PasswordFragment extends BaseFragment {

    public static final String TAG = "PasswordFragment";

    private RegisterViewModel viewModel;
    private RegistrationFragmentChangeListener mListener;
    private TextInputLayout passwordLayout;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private TextInputLayout confirmPasswordLayout;
    private Button btNext;

    public PasswordFragment() {
        // Required empty public constructor
    }
    

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (requireActivity() instanceof RegisterActivity) {
            RegisterActivity activity = (RegisterActivity) requireActivity();
            activity.setToolbarTitle(4);
        }
        return inflater.inflate(R.layout.fragment_password, container, false);
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
        passwordLayout = view.findViewById(R.id.passwordLayout);
        confirmPasswordLayout =view.findViewById(R.id.confirmPasswordLayout);
        password = view.findViewById(R.id.etPassword);
        confirmPassword = view.findViewById(R.id.etConfirmPassword);
        btNext = view.findViewById(R.id.btNext);
    }

    @Override
    public void initObservers() {
        viewModel.getPassword().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                password.setText(s);
            }
        });

        viewModel.getConfirmPassword().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                confirmPassword.setText(s);
            }
        });

        btNext.setOnClickListener(v->{

            String pass = password.getText().toString().trim();
            String confirm = confirmPassword.getText().toString().trim();

            if (pass.isEmpty() || confirm.isEmpty()){
                passwordLayout.setError("Please enter password");
                confirmPasswordLayout.setError("");
                Log.d(TAG, "initObservers() called pass empty");
            }else if (!pass.equals(confirm)){
                passwordLayout.setError("Passwords don't match");
                confirmPasswordLayout.setError("");
                Log.d(TAG, "initObservers() called pass don't match");
            }else {
                viewModel.setPassword(pass);
                viewModel.setConfirmPassword(confirm);
                onNext();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordLayout.setError(null);
                confirmPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordLayout.setError(null);
                confirmPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        mListener.navigateToEmailFragment();
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