package com.example.gmauto.ui.ContactUs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.gmauto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ContactUs extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputLayout NameLayout,EmaillLayout,MobileLayout,messageLayout;
    TextInputEditText Name,Email,Mobile,message;
    Button add;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Contact us view define
        NameLayout = view.findViewById(R.id.NameLayout);
        EmaillLayout = view.findViewById(R.id.EmaillLayout);
        MobileLayout = view.findViewById(R.id.MobileLayout);
        messageLayout = view.findViewById(R.id.messageLayout);
        Name = view.findViewById(R.id.Name);
        Email = view.findViewById(R.id.Email);
        Mobile = view.findViewById(R.id.Mobile);
        message = view.findViewById(R.id.message);
        add = view.findViewById(R.id.add);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate(Name,NameLayout) && validEmailAddress(Email,EmaillLayout) && validPhoneNo(Mobile,MobileLayout) && validate(message,messageLayout)){
                    adds();
                }
            }
        });
    }





    public void adds(){

        Map<String,Object> map = new HashMap<>();
        map.put("CustomerName",Name.getText().toString());
        map.put("Email",Email.getText().toString());
        map.put("Phoneno",Mobile.getText().toString());
        map.put("ExtraDetails",message.getText().toString());
        map.put("TimeStamp",new Timestamp(new Date()));
        map.put("UserId", FirebaseAuth.getInstance().getUid());

        db.collection("ContactUs").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(),"Sumbited",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error Occur",Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Validators
    public boolean validate(TextInputEditText editText, TextInputLayout layout) {

        String value = editText.getText().toString();

        if (value.isEmpty()) {
            layout.setError("This Filed is Required");
            editText.requestFocus();
            return false;
        } else {
            Log.d("review", "valid");
            layout.setError(null);
            return true;
        }
    }

    //validate Email
    private boolean validEmailAddress(TextInputEditText email, TextInputLayout layout){
        String emailInput = email.getText().toString();
        if(!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            layout.setError(null);
            return true;
        }else{

            layout.setError("Please Enter Valid Email");
            return false;
        }
    }




    //validate PhoneNo
    private boolean validPhoneNo(TextInputEditText phone, TextInputLayout layout){
        String phoneInput = phone.getText().toString();
        if(!phoneInput.isEmpty() && phoneInput.length() == 10){
            layout.setError(null);
            return true;
        }else{

            layout.setError("Please Enter Valid MobileNo");
            return false;
        }
    }
}