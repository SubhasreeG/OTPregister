package com.example.bank.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bank.databinding.FragmentOtpBinding;
import com.example.bank.viewmodels.AuthViewModel;


public class OtpFragment extends Fragment {
    private FragmentOtpBinding binding;
    private AuthViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOtpBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mViewModel.triggerOTP(requireActivity());

        binding.otpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.cirLoginButton.setEnabled(!editable.toString().isEmpty());
            }
        });

        binding.cirLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mViewModel.verifyCode(binding.otpET.getText().toString(),requireActivity());
            }
        });

        binding.cirResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.triggerOTP(requireActivity());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}