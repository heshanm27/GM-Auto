package com.example.gmauto.Utility;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import com.example.gmauto.R;
import com.google.android.gms.common.internal.service.Common;

import org.checkerframework.checker.units.qual.A;

public class NetworkChangeListner extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!common.isConnectedtoInternet(context)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.check_internet_dialog,null);
            builder.setView(layout_dialog);

            AppCompatButton btnRetry =layout_dialog.findViewById(R.id.btnRetry);

            //Show Dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);

            dialog.getWindow().setGravity(Gravity.CENTER);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    onReceive(context,intent);
                }
            });
        }
    }
}
