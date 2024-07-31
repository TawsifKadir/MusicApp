package com.application.musicapp.register.forms;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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


public class UsernameFragment extends BaseFragment {

    private RegisterViewModel viewModel;
    private RegistrationFragmentChangeListener mListener;
    private TextInputLayout userNameLayout;
    private TextInputEditText userName;
    private Button btNext;

    public UsernameFragment() {
        // Required empty public constructor
    }
    

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (requireActivity() instanceof RegisterActivity) {
            RegisterActivity activity = (RegisterActivity) requireActivity();
            activity.setToolbarTitle(3);
        }
        return inflater.inflate(R.layout.fragment_username, container, false);
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
        userNameLayout = view.findViewById(R.id.userNameLayout);
        userName = view.findViewById(R.id.userName);
        btNext = view.findViewById(R.id.btNext);
    }

    @Override
    public void initObservers() {
        // Observe ViewModel and bind data to UI components
        viewModel.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userName.setText(s);
            }
        });

        btNext.setOnClickListener(v->{
            String text = userName.getText().toString().trim();
            if (text.isEmpty()){
                userNameLayout.setError("Please enter your name");
            }else{
                viewModel.setUsername(text);
                onNext();
            }
        });

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Remove error
                userNameLayout.setError(null);
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
        mListener.navigateToPasswordFragment();
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