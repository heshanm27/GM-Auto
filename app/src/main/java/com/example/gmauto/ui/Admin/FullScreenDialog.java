package com.example.gmauto.ui.Admin;

import static android.app.Activity.RESULT_OK;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.example.gmauto.Notification.FcmNotificationsSender;
import com.example.gmauto.R;
import com.example.gmauto.models.sparepart;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FullScreenDialog extends DialogFragment implements View.OnClickListener {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextInputEditText sparepartTitleEditText, itemdiscription, priceedittext;
    TextInputLayout sparepartTitleLayput, discriptionLauout, pricelayout;
    Button Submit,Update;
    TextView apptitle;
    ImageView sparepartimage;
    ProgressDialog progress;
    ActivityResultLauncher<Intent> mGetContent;
    ActivityResultLauncher<String> mGetGallaryContent;
    ProgressBar imgPogress;
    FloatingActionButton fabuploadbtn;
    CheckBox notification;
    Boolean NotifcationToggle = false;
    //itemid
    String Id;

    private static String ImageURL = "https://firebasestorage.googleapis.com/v0/b/gmauto-6c556.appspot.com/o/Placeholder%2Fplaceholder_images.png?alt=media&token=7d8c880e-2eec-456f-99aa-72472c8682c5";


    static FullScreenDialog newInstance() {
        return new FullScreenDialog();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialogTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String myInt = bundle.getString("FirebaseID");
            Log.d("btn", myInt);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_sreendialog_sparepart, container, false);
        sparepartTitleEditText = view.findViewById(R.id.sparepartTitleEditText);
        sparepartTitleLayput = view.findViewById(R.id.sparepartTitleLayput);
        discriptionLauout = view.findViewById(R.id.discriptionLauout);
        itemdiscription = view.findViewById(R.id.itemdiscription);
        priceedittext = view.findViewById(R.id.priceedittext);
        pricelayout = view.findViewById(R.id.pricelayout);
        Submit = view.findViewById(R.id.Submit);
        sparepartimage = view.findViewById(R.id.sparepartimage);
        apptitle = view.findViewById(R.id.apptitle);
        imgPogress = view.findViewById(R.id.imgPogress);
        Update= view.findViewById(R.id.Update);
        notification= view.findViewById(R.id.notification);
        //progress dilogue
        progress = new ProgressDialog(getContext());
        progress.setContentView(R.layout.loading_dialog);
        progress.setCancelable(false);



        Submit.setOnClickListener(view1 -> add());
        fabuploadbtn = view.findViewById(R.id.fabuploadbtn);
        fabuploadbtn.setOnClickListener(view12 -> {

            ImagePicker.with(this)
                    .crop()	    			//Crop image(Optional), Check Customization for more option
                    .compress(1024)			//Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });
        Picasso.get().load(ImageURL).placeholder(R.drawable.clearicon).into(sparepartimage, new Callback() {
            @Override
            public void onSuccess() {
                imgPogress.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });


        Bundle bundle = getArguments();

        if (bundle != null) {
            Id = bundle.getString("FirebaseID");
            sparepart  model = bundle.getParcelable("model");
            sparepartTitleEditText.setText(model.getProductName());
            itemdiscription.setText(model.getProductDiscription());
            priceedittext.setText(model.getProductPrice().toString());
            ImageURL = model.getImg();
            Picasso.get().load(model.getImg()).placeholder(R.drawable.clearicon).into(sparepartimage, new Callback() {
                @Override
                public void onSuccess() {
                    imgPogress.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                }
            });
            apptitle.setText("Edit");
            Update.setVisibility(View.VISIBLE);
        }else{
            apptitle.setText("Add");
            Submit.setVisibility(View.VISIBLE);
        }

        //apbar close button
        ImageButton close = view.findViewById(R.id.fullscreen_dialog_close);
        close.setOnClickListener(this);

            //Notification
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        Update.setOnClickListener(view13 -> update(Id));
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.fullscreen_dialog_close) {
            dismiss();
        }

    }



    public void add() {



        if (validate(sparepartTitleEditText, sparepartTitleLayput) && validate(itemdiscription, discriptionLauout) && validate(priceedittext, pricelayout)) {



            progress.show();
            String title =sparepartTitleEditText.getText().toString();
            String disc =itemdiscription.getText().toString();
            double price = Double.parseDouble(priceedittext.getText().toString());

            Map<String,Object> map = new HashMap<>();
            map.put("img",ImageURL);
            map.put("productDiscription",disc);
            map.put("productPrice",price);
            map.put("productName",title);
            map.put("rateavg",0.0);
            map.put("Timestamp",new Timestamp(new Date()));
            map.put("SearchKey",title.toLowerCase());

            db.collection("SpareParts").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast toast = Toast.makeText(getContext(),"Posted",Toast.LENGTH_SHORT);
                    toast.show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(notification.isChecked()){
                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all","New SparePart Added CheckOut",title,getContext(),getActivity());
                                notificationsSender.SendNotifications();
                            }
                            // Do something after 5s = 5000ms

                            progress.dismiss();
                            dismiss();
                        }
                    }, 1000);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast toast = Toast.makeText(getContext(),"Error Occured Try Again",Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    public void update(String ID){
        if (validate(sparepartTitleEditText, sparepartTitleLayput) && validate(itemdiscription, discriptionLauout) && validate(priceedittext, pricelayout)) {
            progress.show();
            Toast toast = Toast.makeText(getContext(), "Submited", Toast.LENGTH_SHORT);
            String title =sparepartTitleEditText.getText().toString();
            String disc =itemdiscription.getText().toString();
            double price = Double.parseDouble(priceedittext.getText().toString());

            Map<String,Object> map = new HashMap<>();
            map.put("img",ImageURL);
            map.put("productDiscription",disc);
            map.put("productPrice",price);
            map.put("productName",title);
            map.put("rateavg",0.0);
            map.put("SearchKey",title.toLowerCase());

            db.collection("SpareParts").document(ID).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast toast = Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_LONG);
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
                    Toast toast = Toast.makeText(getContext(), "Error Occur When Udpated", Toast.LENGTH_LONG);
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

    public void handleUpload(Uri uri) {

        StorageReference reference = FirebaseStorage.getInstance().getReference().child("SparePartsImages").child(System.currentTimeMillis() + "." + getFilExtention(uri));
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(reference);
                imgPogress.setVisibility(View.GONE);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            sparepartimage.setImageURI(uri);
            handleUpload(uri);
        }
    }



}
