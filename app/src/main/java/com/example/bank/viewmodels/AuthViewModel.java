package com.example.bank.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.bank.AuthModel;
import com.example.bank.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class AuthViewModel extends AndroidViewModel {
 private static final String TAG = "AAAA";
 public AuthModel authModel = new AuthModel("","");
 FirebaseAuth mAuth=FirebaseAuth.getInstance();
 String mVerificationId;
 boolean isCodeSent=false;
 PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
  @Override
  public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
   final String code = credential.getSmsCode();
   if (code != null) {
    Log.d(TAG, "onVerificationCompleted:" + credential.getSmsCode());
   }
  }

  @Override
  public void onVerificationFailed(FirebaseException e) {
   Log.w(TAG, "onVerificationFailed", e);

   if (e instanceof FirebaseAuthInvalidCredentialsException) {
    Toast.makeText(getApplication(),"Invalid Credentials", Toast.LENGTH_SHORT).show();
   } else if (e instanceof FirebaseTooManyRequestsException) {
     e.toString();
   }

  }

  @Override
  public void onCodeSent(@NonNull String verificationId,
          @NonNull PhoneAuthProvider.ForceResendingToken token) {
   Log.d(TAG, "onCodeSent:" + token.toString());

   mVerificationId = verificationId;
   isCodeSent=true;
  }
 };

 public void verifyCode(String code, Activity activity) {
   if(isCodeSent) {
    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
    signInWithPhoneAuthCredential(credential, activity);
   }else{
    Toast.makeText(getApplication(), "Code Not sent yet", Toast.LENGTH_LONG).show();
   }
 }

 public AuthViewModel(@NonNull Application application) {
  super(application);
 }

 public void triggerOTP(Activity activity){
  PhoneAuthOptions options =
          PhoneAuthOptions.newBuilder(mAuth)
                  .setPhoneNumber("+91"+authModel.getPhoneNumber())// Phone number to verify
                  .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                  .setActivity(activity)                 // Activity (for callback binding)
                  .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                  .build();
  PhoneAuthProvider.verifyPhoneNumber(options);
 }

 private void signInWithPhoneAuthCredential(PhoneAuthCredential credential,Activity activity) {
  mAuth.signInWithCredential(credential)
          .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
             Log.d(TAG, "signInWithCredential:success");
             Intent intent = new Intent(activity, MainActivity.class);
             activity.startActivity(intent);
            } else {
             Log.w(TAG, "signInWithCredential:failure", task.getException());
             Toast.makeText(getApplication(),"Incorrect OTP",Toast.LENGTH_LONG).show();
             if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
              Log.w(TAG, "invalid", task.getException());
             }
            }
           }
          });
 }

}
