package com.application.musicapp.basic;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.musicapp.utils.LoadingDialogHelper;

public abstract class BaseFragment extends Fragment implements BaseInterface {

    private final static String TAG = "BaseFragment";
    private LoadingDialogHelper loadingDialogHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            setTouchListener(view);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTouchListener(view);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loadingDialogHelper = new LoadingDialogHelper(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListener(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Activity activity = requireActivity();
                    View currentFocus = activity.getCurrentFocus();
                    if (currentFocus instanceof EditText) {
                        int[] scrcoords = new int[2];
                        currentFocus.getLocationOnScreen(scrcoords);
                        float x = event.getRawX() + currentFocus.getLeft() - scrcoords[0];
                        float y = event.getRawY() + currentFocus.getTop() - scrcoords[1];
                        if (x < currentFocus.getLeft() || x > currentFocus.getRight() || y < currentFocus.getTop() || y > currentFocus.getBottom()) {
                            hideKeyboard(currentFocus);
                            currentFocus.clearFocus();
                        }
                    }
                }
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setTouchListener(innerView);
            }
        }
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    // Method to show the progress dialog
    protected void showLoading() {
        if (loadingDialogHelper != null) {
            loadingDialogHelper.show();
        }
    }

    // Method to hide the progress dialog
    protected void hideLoading() {
        if (loadingDialogHelper != null) {
            loadingDialogHelper.hide();
        }
    }

    protected void bindAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView, String[] array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, array);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setText(adapter.getItem(0), false);

        autoCompleteTextView.setOnClickListener(v -> autoCompleteTextView.showDropDown());

        autoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                autoCompleteTextView.showDropDown();
            }
        });
    }

}
