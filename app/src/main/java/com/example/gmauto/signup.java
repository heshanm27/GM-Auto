package com.example.gmauto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.databinding.ActivitySignupBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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


              if(validTextInput(binding.fullname,binding.fullnamelayout) && validEmailAddress(binding.email) && validPassword(binding.password,binding.emailLayout)){
                  Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
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
        }else if(validInput.length() >= 7){
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

