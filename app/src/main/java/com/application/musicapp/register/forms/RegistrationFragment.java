package com.application.musicapp.register.forms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.application.musicapp.R;
import com.application.musicapp.basic.BaseFragment;
import com.application.musicapp.register.RegisterViewModel;
import com.application.musicapp.utils.FireBaseHelper;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationFragment extends BaseFragment {

    private RegisterViewModel viewModel;
    private FirebaseAuth mAuth;
    private FireBaseHelper fireBaseHelper;

    private EditText fullNameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText phoneNumberEditText;
    private Button registerButton;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        initInitial();
        setupViews(view);
        initObservers();
        return view;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_registration;
    }

    @Override
    protected void setupViews(View view) {
        fullNameEditText = view.findViewById(R.id.fullName);
        usernameEditText = view.findViewById(R.id.username);
        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);
        confirmPasswordEditText = view.findViewById(R.id.confirmPassword);
        phoneNumberEditText = view.findViewById(R.id.phoneNumber);
        registerButton = view.findViewById(R.id.register_button);
    }


    @Override
    public void initInitial() {
        viewModel = new ViewModelProvider(requireActivity()).get(RegisterViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        fireBaseHelper = new FireBaseHelper();
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initObservers() {
        registerButton.setOnClickListener(v -> registerUser());

        // Observe ViewModel and bind data to UI components
        viewModel.getFullName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                fullNameEditText.setText(s);
            }
        });
        viewModel.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                usernameEditText.setText(s);
            }
        });
        viewModel.getEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                emailEditText.setText(s);
            }
        });
        viewModel.getPassword().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                passwordEditText.setText(s);
            }
        });
        viewModel.getConfirmPassword().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                confirmPasswordEditText.setText(s);
            }
        });
        viewModel.getPhoneNumber().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                phoneNumberEditText.setText(s);
            }
        });
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

    private void registerUser() {
        String fullName = fullNameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading();
        // Register user with Firebase
        fireBaseHelper.registerUser(fullName, username, email, password, confirmPassword, phoneNumber, new FireBaseHelper.RegistrationCallback() {
            @Override
            public void onSuccess() {
                hideLoading();
                Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                // Navigate to next screen or update UI
            }

            @Override
            public void onFailure(String errorMessage) {
                hideLoading();
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}