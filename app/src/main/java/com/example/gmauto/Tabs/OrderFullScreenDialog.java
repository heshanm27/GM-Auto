package com.example.gmauto.Tabs;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.models.orders;
import com.example.gmauto.models.vehicle;
import com.example.gmauto.ui.Admin.vehicleFullScrrenDialog;
import com.example.gmauto.ui.spareParts.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class OrderFullScreenDialog extends DialogFragment implements View.OnClickListener {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String ID;
    TextInputLayout NameLayout,EmaillLayout,PhoneLayout,AddressLayout,messageLayout;
    TextInputEditText Name,Email,Phone,address,message;
    Float Total;
    ImageView itemimg;
    Button Update;
    TextView price,total,itemname;
    Spinner qutSpinner;
    Integer Quantity;
    ProgressDialog progress;
    ImageButton fullscreen_dialog_close;
    double Price=0.0;

    public OrderFullScreenDialog() {
        // Required empty public constructor
    }
    static OrderFullScreenDialog newInstance() {
        return new OrderFullScreenDialog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_full_screen_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qutSpinner = view.findViewById(R.id.qutSpinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.Quanity, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qutSpinner.setAdapter(arrayAdapter);

        fullscreen_dialog_close =view.findViewById(R.id.fullscreen_dialog_close);
        price = view.findViewById(R.id.price);
        total = view.findViewById(R.id.total);
        itemimg = view.findViewById(R.id.itemimg);
        itemname = view.findViewById(R.id.itemname);

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
        Update = view.findViewById(R.id.Update);



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
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        Bundle bundle = getArguments();

        if(bundle != null){
            ID = bundle.getString("FirebaseID");
            orders model = bundle.getParcelable("model");
            Name.setText(model.getItemName());
            Email.setText(model.getEmail());
            Phone.setText(model.getPhoneno());
            address.setText(model.getAddress());
            message.setText(model.getAddress());
//            Total = Float.parseFloat(model.getTotal().toString());
//            String tot = getString(R.string.Total,Total);
//            total.setText(tot);

            setTotatl(model.getQuantity());
            Integer pos = arrayAdapter.getPosition(model.getQuantity().toString());
            
            qutSpinner.setSelection(pos);


         System.out.println(pos);
         System.out.println(ID);
         System.out.println(total.getText());
         System.out.println(model.getQuantity());
         getProduct(model.getItemID());

        }
        fullscreen_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });






        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate(Name,NameLayout)&& validEmailAddress(Email,EmaillLayout)&& validPhoneNo(Phone,PhoneLayout)  &&validate(address,AddressLayout) && validate(message,messageLayout)  ){
                    Update(ID);
                    progress.show();
                }
            }
        });

    }


    public void getProduct(String ID){
        db.collection("SpareParts").document(ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                Price = snapshot.getDouble("productPrice");
                price.setText(snapshot.getDouble("productPrice").toString());
                Picasso.get().load(snapshot.getString("img")).placeholder(R.drawable.clearicon).into(itemimg);
                itemname.setText(snapshot.getString("productDiscription"));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Update(String ID){

        Map<String,Object> map = new HashMap<>();
        map.put("ItemId",ID);
        map.put("ItemName",itemname.getText().toString());
        map.put("Total",Total);
        map.put("Quantity",Quantity);
        map.put("CustomerName",Name.getText().toString());
        map.put("Email",Email.getText().toString());
        map.put("Phoneno",Phone.getText().toString());
        map.put("Address",address.getText().toString());
        map.put("ExtraDetails",message.getText().toString());

        db.collection("OnlineReservation").document(ID).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progress.dismiss();
                dismiss();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    public void setTotatl(Integer qty){
        Total = Float.parseFloat(String.valueOf(Price * qty));
        String tot = getString(R.string.Total,Total);
        total.setText(tot);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fullscreen_dialog_close) {
            dismiss();
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

    @Override
    public void onStop() {
        super.onStop();
        dismiss();
    }


}