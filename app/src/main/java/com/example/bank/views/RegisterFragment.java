package com.example.bank.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bank.R;
import com.example.bank.databinding.FragmentRegisterBinding;
import com.example.bank.viewmodels.AuthViewModel;

public class RegisterFragment extends Fragment {
   private FragmentRegisterBinding binding;
   private AuthViewModel mViewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        binding.cirLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.authModel.setPhoneNumber(binding.editTextMobile.getText().toString());
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_otpFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}