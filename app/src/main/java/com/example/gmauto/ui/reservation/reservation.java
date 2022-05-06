package com.example.gmauto.ui.reservation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.helpers.DateValidatorsweekDays;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;


public class reservation extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
TextInputLayout Datelayout,TimeLayout,ServiceTypelayout,TitleLayout,name_layout,EmaillLayout,contactlayout,RegNoLayout;
TextInputEditText Date,Title,name,Email,contact,RegNo;
AutoCompleteTextView Time,ServiceType;
ArrayAdapter<String> TimearrayAdapter,ServiceArrayAdapter;
Button Submit;
String[] times = {"8.00-12.00:AM","13.00-18.00:PM"};
String[] ServiceTypes = {"Free Service","General Service","Warranty Repair","Other"};
String PreferedTime,ServieType;
ProgressDialog progress;
Dialog thxDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservation, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        TimearrayAdapter = new ArrayAdapter<>(getContext(),R.layout.autocomplete_list_item,times);
        ServiceArrayAdapter = new ArrayAdapter<>(getContext(),R.layout.autocomplete_list_item,ServiceTypes);

        Time.setAdapter(TimearrayAdapter);
        ServiceType.setAdapter(ServiceArrayAdapter);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Datelayout = view.findViewById(R.id.Datelayout);
        Date = view.findViewById(R.id.Date);
        Time =view.findViewById(R.id.Time);
        TimeLayout =view.findViewById(R.id.TimeLayout);
        ServiceTypelayout =view.findViewById(R.id.ServiceTypelayout);
        ServiceType =view.findViewById(R.id.ServiceType);
        Submit = view.findViewById(R.id.Submit);
        TitleLayout = view.findViewById(R.id.TitleLayout);
        name_layout = view.findViewById(R.id.name_layout);
        EmaillLayout = view.findViewById(R.id.EmaillLayout);
        contactlayout = view.findViewById(R.id.contactlayout);
        RegNoLayout = view.findViewById(R.id.RegNoLayout);
        Title = view.findViewById(R.id.Title);
        name = view.findViewById(R.id.name);
        Email = view.findViewById(R.id.Email);
        contact = view.findViewById(R.id.contact);
        RegNo = view.findViewById(R.id.RegNo);
        Submit =view.findViewById(R.id.Submit);



        //progress dilogue
        progress = new ProgressDialog(getContext());
        progress.setContentView(R.layout.loading_dialog);
        progress.setCancelable(false);

        //Thx dilogue
        thxDialog = new Dialog(getContext());
        thxDialog.setContentView(R.layout.thx_progress_dialog);
        thxDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity(), view);
                if(validate(Title,TitleLayout)&& validate(name,name_layout)&& validEmailAddress(Email,EmaillLayout) &&
                        validPhoneNo(contact,contactlayout)  && validateAutoComplete(ServiceType,ServiceTypelayout) && validate(Date,Datelayout)&& validateAutoComplete(Time,TimeLayout) && validate(RegNo,RegNoLayout)){

                    progress.show();
                    insert();

                }
            }
        });



        Time.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PreferedTime = adapterView.getItemAtPosition(i).toString();
            }
        });
        ServiceType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ServieType= adapterView.getItemAtPosition(i).toString();
            }
        });
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialog();
            }
        });


    }

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
    public void dateDialog() {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        long todayh = MaterialDatePicker.todayInUtcMilliseconds()+1;

        //set weekday validator and pastdate
        CalendarConstraints.Builder consBuilder  =new CalendarConstraints.Builder();
        CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.now();
        consBuilder.setValidator(new DateValidatorsweekDays());
        consBuilder.setValidator(dateValidator);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(todayh);
        builder.setTitleText("Select Prefer Date");
        builder.setCalendarConstraints(consBuilder.build());
        final MaterialDatePicker materialDatePicker = builder.build();
        materialDatePicker.show(getActivity().getSupportFragmentManager(),"datePicker");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
            Date.setText(materialDatePicker.getHeaderText());
            }
        });
    }

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

    public boolean validateAutoComplete(AutoCompleteTextView editText, TextInputLayout layout) {

        String value = editText.getText().toString();

        if (value.isEmpty()) {
            layout.setError("This Filed is Required");
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
    public void insert(){

        String userID = FirebaseAuth.getInstance().getUid();

        Map<String,Object> map = new HashMap<>();
        map.put("Title",Title.getText().toString());
        map.put("ServiceType",ServieType);
        map.put("PrefferedTime",PreferedTime);
        map.put("PreferedDate",Date.getText().toString());
        map.put("FullName",name.getText().toString());
        map.put("Email",Email.getText().toString() );
        map.put("ContactNumber",contact.getText().toString());
        map.put("VehicleRegistrat",RegNo.getText().toString());
        map.put("userID",userID);
        map.put("Timestamp",new Timestamp(new Date()));
        map.put("Status","Pending");

        ArrayList<TextInputEditText> editTexts = new ArrayList<>();
        editTexts.add(Title);
        editTexts.add(Date);
        editTexts.add(name);
        editTexts.add(Email);
        editTexts.add(contact);
        editTexts.add(RegNo);

        db.collection("OnlineReservation").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progress.dismiss();
                clearEditText(editTexts);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                thxDialog.show();
                Toast.makeText(getContext(),"Valid",Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void clearEditText(ArrayList<TextInputEditText> editText){

        for (TextInputEditText textInputEditText : editText) {
            textInputEditText.getText().clear();
        }
        ServieType="";
        PreferedTime="";
        Time.getText().clear();
        ServiceType.getText().clear();
    }

}