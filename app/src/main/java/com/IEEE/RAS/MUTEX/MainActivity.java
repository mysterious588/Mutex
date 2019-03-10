package com.IEEE.RAS.MUTEX;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.shashank.sony.fancytoastlib.FancyToast;

import androidx.annotation.NonNull;

public class MainActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@android.support.annotation.NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(result.getText()).exists())
                                    FancyToast.makeText(MainActivity.this, "This user isn't rigistered", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                                else {
                                    if (dataSnapshot.child(result.getText()).hasChild("attended")) {
                                        FancyToast.makeText(MainActivity.this, "User has already attended", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
                                    } else {
                                        rootRef.child(result.getText()).child("attended").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@android.support.annotation.NonNull Task<Void> task) {
                                                FancyToast.makeText(MainActivity.this, "User has been registered successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                            }
                                        });
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@android.support.annotation.NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


}