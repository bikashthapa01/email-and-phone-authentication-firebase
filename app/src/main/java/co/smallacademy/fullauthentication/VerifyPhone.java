package co.smallacademy.fullauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {
    EditText otpNumberOne,getOtpNumberTwo,getOtpNumberThree,getOtpNumberFour,getOtpNumberFive,otpNumberSix;
    Button verifyPhone,resendOTP;
    Boolean otpValid = true;
    FirebaseAuth firebaseAuth;
    PhoneAuthCredential phoneAuthCredential;
    PhoneAuthProvider.ForceResendingToken token;
    String verificationId;
    String  phone;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        Intent data = getIntent();
        phone = data.getStringExtra("phone");


        firebaseAuth = FirebaseAuth.getInstance();

        otpNumberOne = findViewById(R.id.otpNumberOne);
        getOtpNumberTwo = findViewById(R.id.optNumberTwo);
        getOtpNumberThree = findViewById(R.id.otpNumberThree);
        getOtpNumberFour = findViewById(R.id.otpNumberFour);
        getOtpNumberFive = findViewById(R.id.otpNumberFive);
        otpNumberSix = findViewById(R.id.optNumberSix);

        verifyPhone = findViewById(R.id.verifyPhoneBTn);
        resendOTP = findViewById(R.id.resendOTP);

       verifyPhone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               validateField(otpNumberOne);
               validateField(getOtpNumberTwo);
               validateField(getOtpNumberThree);
               validateField(getOtpNumberFour);
               validateField(getOtpNumberFive);
               validateField(otpNumberSix);

               if(otpValid){
                   // send otp to the user
                   String otp = otpNumberOne.getText().toString()+getOtpNumberTwo.getText().toString()+getOtpNumberThree.getText().toString()+getOtpNumberFour.getText().toString()+
                           getOtpNumberFive.getText().toString()+otpNumberSix.getText().toString();

                   PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,otp);

                   verifyAuthentication(credential);

               }
           }
       });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verificationId = s;
                token = forceResendingToken;
                resendOTP.setVisibility(View.GONE);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                resendOTP.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                verifyAuthentication(phoneAuthCredential);
                resendOTP.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(VerifyPhone.this, "OTP Verification Failed.", Toast.LENGTH_SHORT).show();
            }
        };

        sendOTP(phone);


        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendOTP(phone);
            }
        });

    }

    public void sendOTP(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this,mCallbacks);
    }

    public void resendOTP(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this,mCallbacks,token);
    }


    public void validateField(EditText field){
        if(field.getText().toString().isEmpty()){
            field.setError("Required");
            otpValid = false;
        }else {
            otpValid = true;
        }
    }

    public void verifyAuthentication(PhoneAuthCredential credential){
        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(VerifyPhone.this, "Acccount Created and Linked.", Toast.LENGTH_SHORT).show();
                // send to dashboard.
            }
        });
    }
}