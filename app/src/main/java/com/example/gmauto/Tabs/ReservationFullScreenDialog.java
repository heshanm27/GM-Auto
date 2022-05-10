package com.example.gmauto.Tabs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.helpers.DateValidatorsweekDays;
import com.example.gmauto.models.orders;
import com.example.gmauto.models.reservation;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class ReservationFullScreenDialog extends DialogFragment implements View.OnClickListener {

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
    String ID;
    ImageButton fullscreen_dialog_close;
    public ReservationFullScreenDialog() {
        // Required empty public constructor
    }

    static ReservationFullScreenDialog newInstance() {
        return new ReservationFullScreenDialog();
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
        return inflater.inflate(R.layout.fragment_reservation_full_screen_dialog, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        TimearrayAdapter = new ArrayAdapter<>(getContext(),R.layout.autocomplete_list_item,times);
        ServiceArrayAdapter = new ArrayAdapter<>(getContext(),R.layout.autocomplete_list_item,ServiceTypes);

        Time.setAdapter(TimearrayAdapter);
        ServiceType.setAdapter(ServiceArrayAdapter);
    }

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
        fullscreen_dialog_close= view.findViewById(R.id.fullscreen_dialog_close);
        fullscreen_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Bundle bundle = getArguments();
        if(bundle != null){

            ID = bundle.getString("FirebaseID");
            reservation model = bundle.getParcelable("model");

            Date.setText(model.getPreferedDate());
            Time.setText(model.getPrefferedTime());
            ServiceType.setText(model.getServiceType());
            Title.setText(model.getTitle());
            name.setText(model.getFullName());
            Email.setText(model.getEmail());
            contact.setText(model.getContactNumber());
            RegNo.setText(model.getVehicleRegistrat());

        }


        //progress dilogue
        progress = new ProgressDialog(getContext());
        progress.setContentView(R.layout.loading_dialog);
        progress.setCancelable(false);

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

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(getActivity(), view);
                if(validate(Title,TitleLayout)&& validate(name,name_layout)&& validEmailAddress(Email,EmaillLayout) &&
                        validPhoneNo(contact,contactlayout)  && validateAutoComplete(ServiceType,ServiceTypelayout) && validate(Date,Datelayout)&& validateAutoComplete(Time,TimeLayout) && validate(RegNo,RegNoLayout)){

                    progress.show();
                    update(ID);

                }
            }
        });

    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fullscreen_dialog_close) {
            dismiss();
        }
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

    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
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

    public void update(String ID){

        Map<String,Object> map = new HashMap<>();
        map.put("Title",Title.getText().toString());
        map.put("ServiceType",ServieType);
        map.put("PrefferedTime",PreferedTime);
        map.put("PreferedDate",Date.getText().toString());
        map.put("FullName",name.getText().toString());
        map.put("Email",Email.getText().toString() );
        map.put("ContactNumber",contact.getText().toString());
        map.put("VehicleRegistrat",RegNo.getText().toString());
        map.put("userID", FirebaseAuth.getInstance().getUid());
        map.put("Timestamp",new Timestamp(new Date()));
        map.put("Status","Pending");

        ArrayList<TextInputEditText> editTexts = new ArrayList<>();
        editTexts.add(Title);
        editTexts.add(Date);
        editTexts.add(name);
        editTexts.add(Email);
        editTexts.add(contact);
        editTexts.add(RegNo);

        db.collection("OnlineReservation").document(ID).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progress.dismiss();
                clearEditText(editTexts);
                dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error Occured",Toast.LENGTH_SHORT).show();
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


    @Override
    public void onStop() {
        super.onStop();
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismiss();
    }
}