package com.application.musicapp.register.forms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.application.musicapp.R;
import com.application.musicapp.basic.BaseFragment;
import com.application.musicapp.register.RegisterActivity;
import com.application.musicapp.register.RegisterViewModel;
import com.application.musicapp.register.RegistrationFragmentChangeListener;
import com.application.musicapp.utils.enums.Month;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class DOBFragment extends BaseFragment {

    public static final String TAG = "DOBFragment";

    private RegisterViewModel viewModel;
    private RegistrationFragmentChangeListener mListener;

    private NumberPicker dayPicker;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private Button btNext;
    private TextInputEditText dateEditText;
    private TextInputLayout dobLayout;

    public DOBFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (requireActivity() instanceof RegisterActivity) {
            RegisterActivity activity = (RegisterActivity) requireActivity();
            activity.setToolbarTitle(2);
        }
        return inflater.inflate(R.layout.fragment_d_o_b, container, false);
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
        // Initialize views
        View view = requireView();
        dayPicker = view.findViewById(R.id.dayPicker);
        monthPicker = view.findViewById(R.id.monthPicker);
        yearPicker = view.findViewById(R.id.yearPicker);
        btNext = view.findViewById(R.id.btNext);
        dateEditText = view.findViewById(R.id.dob);
        dobLayout = view.findViewById(R.id.dobLayout);
    }

    @Override
    public void initObservers() {
        //Setup Pickers
        setupPickers();

        viewModel.getDate().observe(getViewLifecycleOwner(), this::updatePickersFromDate);

        // Set up Submit Button
        btNext.setOnClickListener(v->{
            try {
                String text = dateEditText.getText().toString().trim();
                if (text.isEmpty()){
                    dobLayout.setError("Please select a date");
                }else{
                    saveDateToViewModel();
                    onNext();
                }
            }catch (Exception e){
                Toast.makeText(requireContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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


    private void setupPickers() {

        // Make EditText non-editable
        dateEditText.setEnabled(false);
        dateEditText.setFocusable(false);

        // Set up the year picker
        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(Calendar.getInstance().get(Calendar.YEAR));
        yearPicker.setValue(Calendar.getInstance().get(Calendar.YEAR));

        // Set up the month picker
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        Log.d(TAG, "setupPickers() called" + Arrays.toString(getMonthNames()));
        monthPicker.setDisplayedValues(getMonthNames());
        monthPicker.setValue(Calendar.getInstance().get(Calendar.MONTH)+1);

        // Set up the day picker
        dayPicker.setMinValue(1);
        updateDayPicker();

        // Set the current day as the selected value
        Calendar calendar = Calendar.getInstance();
        dayPicker.setValue(calendar.get(Calendar.DAY_OF_MONTH));

        // Listeners
        dayPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (newVal == 1 && oldVal == picker.getMaxValue()) {
                handleDayScrollDown();
            } else if (newVal == picker.getMaxValue() && oldVal == 1) {
                handleDayScrollUp();
            }
            updateDateEditText();
        });

        monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (newVal == 1 && oldVal == picker.getMaxValue()){
                handleMonthScrollDown();
            } else if (newVal == picker.getMaxValue() && oldVal == 1){
                handleMonthScrollUp();
            }
            updateDayPicker();
            updateDateEditText();
        });

        yearPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            updateDayPicker();
            updateDateEditText();
        });
    }

    private void updateDayPicker() {
        Calendar calendar = Calendar.getInstance();
        int year = yearPicker.getValue();
        int month = monthPicker.getValue() - 1; // Months are 0-based in Calendar
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1); // Start of the month

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        dayPicker.setMaxValue(daysInMonth);
        // Adjust the current value if it exceeds the new max value
        if (dayPicker.getValue() > daysInMonth) {
            dayPicker.setValue(daysInMonth);
        }
    }

    private void handleDayScrollDown() {
        int month = monthPicker.getValue();
        int year = yearPicker.getValue();

        if (month == 12) { // December
            monthPicker.setValue(1); // January
            yearPicker.setValue(year + 1); // Next year
        } else {
            monthPicker.setValue(month + 1); // Next month
        }

        updateDayPicker();
    }

    private void handleDayScrollUp() {
        int month = monthPicker.getValue();
        int year = yearPicker.getValue();

        if (month == 1) { // January
            monthPicker.setValue(12); // December
            yearPicker.setValue(year - 1); // Previous year
        } else {
            monthPicker.setValue(month - 1); // Previous month
        }

        updateDayPicker();
    }

    private void handleMonthScrollUp(){
        int year = yearPicker.getValue();
        yearPicker.setValue(year - 1);
    }

    private void handleMonthScrollDown(){
        int year = yearPicker.getValue();
        yearPicker.setValue(year + 1);
    }

    @SuppressLint("DefaultLocale")
    private void updateDateEditText() {
        int day = dayPicker.getValue();
        int month = monthPicker.getValue();
        int year = yearPicker.getValue();
        String monthName = getMonthNames()[month - 1];
        String dateText = String.format("%s %02d, %d",monthName, day , year);
        dateEditText.setText(dateText);
        dobLayout.setError(null);
    }

    private void saveDateToViewModel() {
        int day = dayPicker.getValue();
        int month = monthPicker.getValue();
        int year = yearPicker.getValue();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day); // Months are 0-based in Calendar
        Date date = calendar.getTime();

        viewModel.setDate(date);
    }

    private void updatePickersFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar
        int year = calendar.get(Calendar.YEAR);

        dayPicker.setValue(day);
        monthPicker.setValue(month);
        yearPicker.setValue(year);

        updateDateEditText();
    }

    private String[] getMonthNames() {
        Month[] months = Month.values();
        String[] monthNames = new String[months.length];
        for (int i = 0; i < months.length; i++) {
            monthNames[i] = months[i].getAbbreviation();
        }
        return monthNames;
    }


    @Override
    public void onNext() {
        mListener.navigateToUserNameFragment();
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