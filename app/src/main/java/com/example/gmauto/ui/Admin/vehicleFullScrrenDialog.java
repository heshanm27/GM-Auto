package com.example.gmauto.ui.Admin;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmauto.R;
import com.example.gmauto.models.sparepart;
import com.example.gmauto.models.vehicle;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link vehicleFullScrrenDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class vehicleFullScrrenDialog extends DialogFragment implements View.OnClickListener {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progress;
    EditText chipedit;
    ImageButton deletebtn;
    ChipGroup chipGrup;
    String Id, Transmissiontype, Fueltype, userid;
    ImageView sparepartimage;
    RadioGroup TransmissionType, FuelType;
    Button Submit,Update;
    ProgressBar imgPogress;
    String currentPhotoPath;
    FloatingActionButton fabuploadbtn;
    private static String ImageURL = "https://firebasestorage.googleapis.com/v0/b/gmauto-6c556.appspot.com/o/Placeholder%2Fplaceholder_images.png?alt=media&token=7d8c880e-2eec-456f-99aa-72472c8682c5";
    TextInputLayout vehcileTitleLayput, discriptionLauout, pricelayout, ManufacturingYearLayout, MileageLayout, CapacityLayout, ColorLayout;
    TextInputEditText vehicleTitleEditText, itemdiscription, priceedittext, ManufacturingYear, Mileage, Capacity, Color;
    RadioButton Manual, Automatic, Continuously, petrol,disel;
    TextView apptitle;
        List<String> Amenities ;
    static vehicleFullScrrenDialog newInstance() {
        return new vehicleFullScrrenDialog();
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
        return inflater.inflate(R.layout.full_sreendialog_vehcile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deletebtn = view.findViewById(R.id.deleteBtn);
        chipGrup = view.findViewById(R.id.chipGrup);
        chipedit = view.findViewById(R.id.chipedit);

                //image
        sparepartimage = view.findViewById(R.id.sparepartimage);
        fabuploadbtn = view.findViewById(R.id.fabuploadbtn);
        fabuploadbtn.setOnClickListener(view12 -> {

            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        vehcileTitleLayput = view.findViewById(R.id.vehcileTitleLayput);
        discriptionLauout = view.findViewById(R.id.discriptionLauout);
        pricelayout = view.findViewById(R.id.pricelayout);
        ManufacturingYearLayout = view.findViewById(R.id.ManufacturingYearLayout);
        MileageLayout = view.findViewById(R.id.MileageLayout);
        CapacityLayout = view.findViewById(R.id.CapacityLayout);
        ColorLayout = view.findViewById(R.id.ColorLayout);
        vehicleTitleEditText = view.findViewById(R.id.vehicleTitleEditText);
        itemdiscription = view.findViewById(R.id.itemdiscription);
        priceedittext = view.findViewById(R.id.priceedittext);
        ManufacturingYear = view.findViewById(R.id.ManufacturingYear);
        Mileage = view.findViewById(R.id.Mileage);
        Capacity = view.findViewById(R.id.Capacity);
        Color = view.findViewById(R.id.Color);
        apptitle =view.findViewById(R.id.apptitle);
        TransmissionType = view.findViewById(R.id.TransmissionType);
        Manual = view.findViewById(R.id.Manual);
        Automatic = view.findViewById(R.id.Automatic);
        Continuously = view.findViewById(R.id.Continuously);
        FuelType = view.findViewById(R.id.FuelType);
        imgPogress = view.findViewById(R.id.imgPogress);

        TransmissionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.Manual:
                        Transmissiontype = Manual.getText().toString();
                        break;
                    case R.id.Automatic:
                        Transmissiontype = Automatic.getText().toString();
                        break;
                    case R.id.Continuously:
                        Transmissiontype = Continuously.getText().toString();
                }
            }
        });
        petrol = view.findViewById(R.id.petrol);
        disel = view.findViewById(R.id.disel);

        Submit = view.findViewById(R.id.Submit);
        Update = view.findViewById(R.id.Update);

        Amenities = new ArrayList<>();
        Bundle bundle = getArguments();

        if (bundle != null) {
            Id = bundle.getString("FirebaseID");
            vehicle model = bundle.getParcelable("model");

            //set values
            vehicleTitleEditText.setText(model.getTitle());
            itemdiscription.setText(model.getDiscription());
            priceedittext.setText(Double.toString(model.getPrice()));
            Map<String,Object> map = model.getDetails();
            Picasso.get().load(model.getImg()).placeholder(R.drawable.clearicon).into(sparepartimage);
            ManufacturingYear.setText(map.get("ManufacturingYear").toString());
            Mileage.setText(map.get("Mileage").toString());
            Capacity.setText(map.get("EngineCapacity").toString());
            Color.setText(map.get("Color").toString());
            Update.setVisibility(View.VISIBLE);

           List<String> amenitiesArray = model.getAmenities();

            for (String i:amenitiesArray) {
                addChip(i);
            }
        }else{
            Submit.setVisibility(View.VISIBLE);
            apptitle.setText("Add Vehicle");

        }

        //progress dilogue
        progress = new ProgressDialog(getContext());
        progress.setContentView(R.layout.loading_dialog);
        progress.setCancelable(false);

        FuelType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.petrol:
                        Fueltype = petrol.getText().toString();
                    case  R.id.disel:
                        Fueltype = disel.getText().toString();
                }
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = chipedit.getText().toString();
                addChip(text);
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });
        //apbar close button
        ImageButton close = view.findViewById(R.id.fullscreen_dialog_close);
        close.setOnClickListener(this);


    }



    public void handleUpload(Uri uri) {

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("VehiclesImges").child(System.currentTimeMillis() + "." + getFilExtention(uri));
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(reference);

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                imgPogress.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imgPogress.setVisibility(View.VISIBLE);
            }
        });

    }

    private String getFilExtention(Uri muri) {

        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));

    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("url", uri.toString());
                imgPogress.setVisibility(View.GONE);
                Toast toast = Toast.makeText(getContext(), "Image Upload Successfully", Toast.LENGTH_LONG);
                toast.show();
                Log.d("imageUrlFirst",ImageURL);
                ImageURL = uri.toString();
                Log.d("imageUrlFirst",ImageURL);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imgPogress.setVisibility(View.GONE);
                Toast toast = Toast.makeText(getContext(), "Image Upload Faild", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void addChip(String text) {
        Log.d("chip", "added");
        Amenities.add(text);
        Chip chips = new Chip(getContext());
        chips.setText(text);
        chips.setChipBackgroundColorResource(R.color.blue);
        chips.setCloseIconVisible(true);
        chips.setTextColor(getResources().getColor(R.color.white));
        chips.setCloseIconTintResource(R.color.white);
        chips.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amenities.remove(text);
                chipGrup.removeView(view);
                displaylist();
            }
        });
        displaylist();
        chipGrup.addView(chips);


    }


    public void displaylist(){

        for (String i:Amenities) {
            System.out.println(i);
        }
    }

    public void add() {

        if (validate(vehicleTitleEditText, vehcileTitleLayput) && validate(itemdiscription, discriptionLauout) && validate(priceedittext, pricelayout)
                && validate(ManufacturingYear, ManufacturingYearLayout) && validate(Mileage, MileageLayout) && validate(Capacity, CapacityLayout) && validate(Color, ColorLayout)
        ) {
            progress.show();
            Toast toast = Toast.makeText(getContext(), "Submited", Toast.LENGTH_SHORT);
            String title = vehicleTitleEditText.getText().toString();
            String disc = itemdiscription.getText().toString();
            double price = Double.parseDouble(priceedittext.getText().toString());

            Map<String, Object> details = new HashMap<>();
            details.put("Color", Color.getText().toString());
            details.put("EngineCapacity", Capacity.getText().toString());
            details.put("FuleType",Fueltype);
            details.put("ManufacturingYear", ManufacturingYear.getText().toString());
            details.put("Mileage", Mileage.getText().toString());
            details.put("TransmissionType", Transmissiontype);

            Map<String, Object> map = new HashMap<>();
            map.put("img", ImageURL);
            map.put("Title", title);
            map.put("Price", price);
            map.put("Amenities", Amenities);
            map.put("details", details);
            map.put("Timestamp", new Timestamp(new Date()));
            map.put("Discription",disc);
            db.collection("Vehicles").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast toast = Toast.makeText(getContext(), "Posted", Toast.LENGTH_SHORT);
                    toast.show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            progress.dismiss();
                            dismiss();
                        }
                    }, 1000);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast toast = Toast.makeText(getContext(), "Error Occured Try Again", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }


    public void Update() {

        if (validate(vehicleTitleEditText, vehcileTitleLayput) && validate(itemdiscription, discriptionLauout) && validate(priceedittext, pricelayout)
                && validate(ManufacturingYear, ManufacturingYearLayout) && validate(Mileage, MileageLayout) && validate(Capacity, CapacityLayout) && validate(Color, ColorLayout)
        ) {
            progress.show();
            Toast toast = Toast.makeText(getContext(), "Submited", Toast.LENGTH_SHORT);
            String title = vehicleTitleEditText.getText().toString();
            String disc = itemdiscription.getText().toString();
            double price = Double.parseDouble(priceedittext.getText().toString());

            Map<String, Object> details = new HashMap<>();
            details.put("Color", Color.getText().toString());
            details.put("EngineCapacity", Capacity.getText().toString());
            details.put("FuleType",Fueltype);
            details.put("ManufacturingYear", ManufacturingYear.getText().toString());
            details.put("Mileage", Mileage.getText().toString());
            details.put("TransmissionType", Transmissiontype);

            Map<String, Object> map = new HashMap<>();
            map.put("img", ImageURL);
            map.put("Title", title);
            map.put("Price", price);
            map.put("Amenities", Amenities);
            map.put("details", details);
            map.put("Timestamp", new Timestamp(new Date()));
            map.put("Discription",disc);
            db.collection("Vehicles").document(Id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast toast = Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT);
                    toast.show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            progress.dismiss();
                            dismiss();
                        }
                    }, 1000);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast toast = Toast.makeText(getContext(), "Error Occured Try Again", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }








    public boolean validate(TextInputEditText editText, TextInputLayout layout) {

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            sparepartimage.setImageURI(uri);
            handleUpload(uri);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.fullscreen_dialog_close:
                dismiss();
                break;


        }

    }
}