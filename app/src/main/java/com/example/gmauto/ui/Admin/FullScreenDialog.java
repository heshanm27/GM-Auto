package com.example.gmauto.ui.Admin;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.ACTION_PICK;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gmauto.R;

import java.io.IOException;

public class FullScreenDialog extends DialogFragment implements View.OnClickListener {

    EditText editText;
    Button Submit;
    TextView apptitle;
    ImageView sparepartimage;
    ProgressDialog progress;
    ActivityResultLauncher<Intent> mGetContent;
    ActivityResultLauncher<String> mGetGallaryContent;
    private final int REQUEST_IMAGE_CODE = 20004;

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
        editText = view.findViewById(R.id.sparepartTitleEditText);
        Submit = view.findViewById(R.id.Submit);
        sparepartimage = view.findViewById(R.id.sparepartimage);
        apptitle = view.findViewById(R.id.apptitle);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = new ProgressDialog(getContext());
                progress.show();
                progress.setContentView(R.layout.loading_dialog);
                progress.setCancelable(false);
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
            String Id = bundle.getString("FirebaseID");
            editText.setText(Id);
            apptitle.setText("Edit");
        }
        apptitle.setText("Add");
        //apbar close button
        ImageButton close = view.findViewById(R.id.fullscreen_dialog_close);
        close.setOnClickListener(this);

        //start intent
        mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    sparepartimage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));
                    Log.d("tag", "clicked2");
                }
            }
        });
        mGetGallaryContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), result);
                    sparepartimage.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            mGetContent.launch(intent);
            Log.d("tag", "clicked3");

        });
        builder.setNegativeButton("Gallary", (dialogInterface, i) -> {
            dialogInterface.dismiss();

            mGetGallaryContent.launch("image/*");

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
