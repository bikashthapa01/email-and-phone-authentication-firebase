package co.smallacademy.fullauthentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends Fragment {
    public static final String TAG = "TAG";
    EditText personFullName,personEmailAddress,personPass,personConfPass,phoneCountryCode,phoneNumber;
    Button regsiterAccountBtn;
    Boolean isDataValid = false;
    FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        personFullName = v.findViewById(R.id.registerFullName);
        personEmailAddress = v.findViewById(R.id.registerEmail);
        personPass = v.findViewById(R.id.regsiterPass);
        personConfPass = v.findViewById(R.id.retypePass);
        phoneCountryCode = v.findViewById(R.id.countryCode);
        phoneNumber = v.findViewById(R.id.registerPhoneNumber);

        fAuth = FirebaseAuth.getInstance();

        // validating the data
        validateData(personFullName);
        validateData(personEmailAddress);
        validateData(personPass);
        validateData(personConfPass);
        validateData(phoneCountryCode);
        validateData(phoneNumber);

        if(!personPass.getText().toString().equals(personConfPass.getText().toString())){
            isDataValid = false;
            personConfPass.setError("Password Do not Match");
        }else {
            isDataValid = true;
        }

        if(isDataValid){
            // proceed with the registration of the user
            fAuth.createUserWithEmailAndPassword(personEmailAddress.getText().toString(),personPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(getActivity(), "User Account is Created.", Toast.LENGTH_SHORT).show();
                    // send the user to verify the phone
                    Intent phone = new Intent(getActivity(),VerifyPhone.class);
                    phone.putExtra("phone","+"+phoneCountryCode.getText().toString()+phoneNumber.getText().toString());
                    startActivity(phone);
                    Log.d(TAG, "onSuccess: "+"+"+phoneCountryCode.getText().toString()+phoneNumber.getText().toString() );
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error !" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        return v;
    }

    public void validateData(EditText field){
        if(field.getText().toString().isEmpty()){
            isDataValid = false;
            field.setError("Required Field.");
        }else {
            isDataValid = true;
        }
    }
}