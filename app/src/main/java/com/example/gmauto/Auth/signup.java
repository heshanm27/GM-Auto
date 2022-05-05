package com.example.gmauto.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.gmauto.Dashbord;
import com.example.gmauto.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //firebase setup

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        binding.confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String password = binding.password.getText().toString();
                String confirmpassword = binding.confirmpassword.getText().toString();
                if(password.equals(confirmpassword)){
                    binding.confirmpasswordlayout.setEndIconVisible(true);
                    binding.confirmpasswordlayout.setError(null);
                }else{
                    binding.confirmpasswordlayout.setEndIconVisible(false);
                    binding.confirmpasswordlayout.setError("Password and Confirm Password Doesnt Match");
                }
            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              if(validTextInput(binding.fullname,binding.fullnamelayout) && validEmailAddress(binding.email) && validPassword(binding.password,binding.passwordLayout)){

                  String emailValue = binding.email.getText().toString();
                  String passwordValue = binding.password.getText().toString();
                  fAuth.createUserWithEmailAndPassword(emailValue,passwordValue).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                      @Override
                      public void onSuccess(AuthResult authResult) {
                          //get currunt user
                          FirebaseUser user  = fAuth.getCurrentUser();

                          //add new document in users colletion using user id that got from fAuth
                          DocumentReference docRef = fStore.collection("Users").document(user.getUid());

                          Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("FullName",binding.fullname.getText().toString());
                            //user role
                          userInfo.put("isAdmin",false);
                          //save the document to firestore
                          docRef.set(userInfo);
                          startActivity(new Intent(getApplicationContext(), Dashbord.class));
                          finish();
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(signup.this,"Faild to Create Account",Toast.LENGTH_SHORT).show();
                      }
                  });


              }
            }
        });


        //login intent
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(signup.this,login.class);
                startActivity(login);
            }
        });
    }

    //validate message
    private boolean validEmailAddress(TextInputEditText email){
        String emailInput = email.getText().toString();
        if(!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
            binding.emailLayout.setError(null);
            return true;
        }else{

            binding.emailLayout.setError("Please Enter Valid Email");
            return false;
        }
    }


    //validate inputs
    private  boolean validPassword(TextInputEditText input, TextInputLayout layout){
        String validInput = input.getText().toString();

        if(validInput.isEmpty()){
            layout.setError("Please Enter Password");
            return false;
        }else if(validInput.length() < 7){
            layout.setError("Password must be grater than 7 characters");
            return false;
        }
        else{
            layout.setError(null);
            return  true;
        }
    }

    //validate inputs
    private  boolean validTextInput(TextInputEditText input, TextInputLayout layout){
        String validInput = input.getText().toString();

        if(validInput.isEmpty()){
            layout.setError("Please Fill The InputBox");
            return false;
        }else{
            layout.setError(null);
            return  true;
        }
    }
}

