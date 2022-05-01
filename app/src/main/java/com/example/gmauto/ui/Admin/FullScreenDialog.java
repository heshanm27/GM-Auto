package com.example.gmauto.ui.Admin;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_PICK;

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
import android.widget.EditText;
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

import com.example.gmauto.R;
import com.example.gmauto.models.sparepart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
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

    //itemid
    String Id;

    private Uri imageUri;
    private static String ImageURL = "https://firebasestorage.googleapis.com/v0/b/gmauto-6c556.appspot.com/o/Placeholder%2Fplaceholder_images.png?alt=media&token=7d8c880e-2eec-456f-99aa-72472c8682c5";
    public static final int CAMERA_PERM_CODE = 101;

    String currentPhotoPath;

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
        //progress dilogue
        progress = new ProgressDialog(getContext());
        progress.setContentView(R.layout.loading_dialog);
        progress.setCancelable(false);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add();


            }
        });
        sparepartimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                imgDialog(builder);
                Log.d("tag", "clicked");
            }
        });
        Bundle bundle = getArguments();

        if (bundle != null) {
            Id = bundle.getString("FirebaseID");
            sparepart  model = bundle.getParcelable("model");
//            String title = bundle.getString("productName");
//            String disc = bundle.getString("productDiscription");
//            Double prie = bundle.getDouble("productPrice");
//            String img = bundle.getString("imgUrl");
            sparepartTitleEditText.setText(model.getProductName());
            itemdiscription.setText(model.getProductDiscription());
            priceedittext.setText(model.getProductPrice().toString());
            ImageURL = model.getImg();
            Picasso.get().load(model.getImg()).placeholder(R.drawable.clearicon).into(sparepartimage);
            apptitle.setText("Edit");
            Update.setVisibility(View.VISIBLE);
        }else{
            apptitle.setText("Add");
            Submit.setVisibility(View.VISIBLE);
        }

        //apbar close button
        ImageButton close = view.findViewById(R.id.fullscreen_dialog_close);
        close.setOnClickListener(this);

        //start intent
        mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                    File f = new File(currentPhotoPath);
                    sparepartimage.setImageURI(Uri.fromFile(f));
                    Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                    Log.d("tag", "clicked2");
                }
            }
        });

        //Gallary intent
        mGetGallaryContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageUri = result;
                sparepartimage.setImageURI(result);
                if (imageUri != null) {
                    handleUpload(imageUri);
                }
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(Id);
            }
        });
        return view;
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


    public void imgDialog(AlertDialog.Builder builder) {

        builder.setTitle("Select Image");
        builder.setMessage("Choose Your Option?");
        builder.setPositiveButton("Camara", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            askCameraPermissions();
            Log.d("tag", "clicked3");

        });
        builder.setNegativeButton("Gallary", (dialogInterface, i) -> {
            dialogInterface.dismiss();

            mGetGallaryContent.launch("image/*");

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void add() {



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
            map.put("Timestamp",new Timestamp(new Date()));

            db.collection("SpareParts").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast toast = Toast.makeText(getContext(),"Posted",Toast.LENGTH_SHORT);
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        PackageManager pm = getActivity().getPackageManager();
        if (takePictureIntent.resolveActivity(pm) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "net.smallacademy.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                mGetContent.launch(takePictureIntent);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;


    }

}
