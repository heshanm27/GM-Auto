package com.example.gmauto.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gmauto.Dashbord;
import com.example.gmauto.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmailValue = binding.Email.getText().toString();
                String passwordValue= binding.password.getText().toString();
                Log.d("pass",passwordValue);
                Log.d("pass",EmailValue);
                   fAuth.signInWithEmailAndPassword(EmailValue,passwordValue).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                       @Override
                       public void onSuccess(AuthResult authResult) {
                            checkAccessLevel(authResult.getUser().getUid());

                           Intent intent = new Intent(getApplicationContext(), Dashbord.class);
                           startActivity(intent);
                           finish();



//                           startActivity( new Intent(getApplicationContext(),Dashbord.class));
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(login.this,"LoginFaild",Toast.LENGTH_LONG).show();
                       }
                   });
            }
        });//onbutton
    }
    public void checkAccessLevel(String uid){
        DocumentReference docRef = fStore.collection("Users").document(uid);
        //get alue from doumment
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
              if(documentSnapshot.getBoolean("isAdmin")){
                  Toast.makeText(login.this,"LoginAdmin",Toast.LENGTH_LONG).show();
              }else{
                  Toast.makeText(login.this,"LoginUSer",Toast.LENGTH_LONG).show();
              }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(login.this,"LoginFaild",Toast.LENGTH_LONG).show();
            }
        });
    }
}