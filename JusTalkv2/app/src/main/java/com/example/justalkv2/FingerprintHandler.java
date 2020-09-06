package com.example.justalkv2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

@TargetApi(Build.VERSION_CODES.M)
class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;


    public FingerprintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){

        android.os.CancellationSignal cancellationSignal = new android.os.CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {

        this.update("There was an Auth Error. " + errString, false);

    }

    @Override
    public void onAuthenticationFailed() {

        this.update("Auth Failed. ", false);

    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {

        this.update("Error: " + helpString, false);

    }
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        TextView paraLabel = (TextView) ((Activity)context).findViewById(R.id.paraLabel);
        ImageView imageView = (ImageView) ((Activity)context).findViewById(R.id.fingerprintImage);
        paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        paraLabel.setText("Fingerprint Authenticated");
        imageView.setImageResource(R.mipmap.action_done);

        Intent i = new Intent(context,MainActivity.class);
        context.startActivity(i);

    }

    private void update(String s, boolean b) {
        TextView paraLabel = (TextView) ((Activity)context).findViewById(R.id.paraLabel);
        ImageView imageView = (ImageView) ((Activity)context).findViewById(R.id.fingerprintImage);

        paraLabel.setText(s);

        if(b == false){

            paraLabel.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));

        }
    }
}
