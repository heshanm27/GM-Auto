package com.example.gmauto.ui.spareParts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.PluralsRes;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.gmauto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Order extends Fragment {

    Spinner qutSpinner;
    double Price=0.0;
    TextView price,total,itemname;
    Float Total;
    ImageView itemimg;
    Integer Quantity;
    String ItemID;
    Button OrderItem,Accept,Cancel;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputLayout NameLayout,EmaillLayout,PhoneLayout,AddressLayout,messageLayout;
    TextInputEditText  Name,Email,Phone,address,message;
    NavController navController;
    ProgressDialog progress;
    AlertDialog alertDialog;

    public Order() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        // Inflate the layout for this fragment

        price = view.findViewById(R.id.price);
        total = view.findViewById(R.id.total);
        itemimg = view.findViewById(R.id.itemimg);
        itemname = view.findViewById(R.id.itemname);




        Bundle b = getArguments();
        String imgUrl = b.getString("img");
        Price = b.getDouble("price");
        Picasso.get().load(imgUrl).placeholder(R.drawable.clearicon).into(itemimg);
        itemname.setText(b.getString("Title"));
        ItemID = b.getString("FirebaseID");
        String Prices = getString(R.string.Price,Price);
        price.setText(Prices);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qutSpinner = view.findViewById(R.id.qutSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.Quanity, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qutSpinner.setAdapter(arrayAdapter);

        navController = Navigation.findNavController(view);
        //progress dilogue
        progress = new ProgressDialog(getContext());
        progress.setContentView(R.layout.loading_dialog);
        progress.setCancelable(false);



        qutSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                switch (text) {
                    case "1":
                        setTotatl(1);
                        Quantity=1;
                        break;
                    case "5":
                        setTotatl(5);
                        Quantity=5;
                        break;
                    case "10":
                        setTotatl(10);
                        Quantity=10;
                        break;
                    case "50(Max)":
                        setTotatl(50);
                        Quantity=50;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Layout and EditTexts
        NameLayout = view.findViewById(R.id.NameLayout);
        Name = view.findViewById(R.id.Name);
        EmaillLayout = view.findViewById(R.id.EmaillLayout);
        Email = view.findViewById(R.id.Email);
        PhoneLayout = view.findViewById(R.id.PhoneLayout);
        Phone = view.findViewById(R.id.Phone);
        AddressLayout = view.findViewById(R.id.AddressLayout);
        address = view.findViewById(R.id.address);
        messageLayout = view.findViewById(R.id.messageLayout);
        message = view.findViewById(R.id.message);
        OrderItem = view.findViewById(R.id.OrderItem);



        OrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validate(Name,NameLayout)&& validEmailAddress(Email,EmaillLayout)&& validPhoneNo(Phone,PhoneLayout)  &&validate(address,AddressLayout) && validate(message,messageLayout)  ){
                    addOrder();
                    progress.show();
                    ShowLAlertDialog();
                }

            }
        });


    }


    public void setTotatl(Integer qty){
        Total = Float.parseFloat(String.valueOf(Price * qty));
        String tot = getString(R.string.Total,Total);
        total.setText(tot);
    }

    //firebase

    public void addOrder(){
        Log.d("review", "order");
        Map<String,Object> map = new HashMap<>();
        map.put("ItemId",ItemID);
        map.put("ItemName",itemname.getText().toString());
        map.put("Total",Total);
        map.put("Quantity",Quantity);
        map.put("CustomerName",Name.getText().toString());
        map.put("Email",Email.getText().toString());
        map.put("Phoneno",Phone.getText().toString());
        map.put("Address",address.getText().toString());
        map.put("ExtraDetails",message.getText().toString());
        map.put("TimeStamp",new Timestamp(new Date()));
        map.put("UserId", FirebaseAuth.getInstance().getUid());
        map.put("Status", "Pending");
        db.collection("Orders").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progress.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
//                ShowLAlertDialog();
            }
        });
    }


    //alert Dialog

    public void ShowLAlertDialog(){

        LayoutInflater inflater =LayoutInflater.from(getContext());
        View view =inflater.inflate(R.layout.order_confirmation_dialog,null);

        alertDialog = new AlertDialog.Builder(getContext()).setView(view).create();
        alertDialog.show();

        Accept = view.findViewById(R.id.Accept);
        Cancel = view.findViewById(R.id.Cancel);
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                navController.navigateUp();
//                navController =Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
//                if(navController.getCurrentDestination().getId() ==R.id.order2){
//
//                    NavDirections action = OrderDirections.actionOrder2ToSparePartsHome();
//                    NavOptions options = new NavOptions.Builder().setPopUpTo(R.id.order2,true).build();
//                    navController.navigate(action,options);
//                }


            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        if(alertDialog != null){
            alertDialog.dismiss();
        }
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