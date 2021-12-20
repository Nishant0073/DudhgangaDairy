package com.miniprojectG3.dudhgangadairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddUpdateRates extends AppCompatActivity {

    //Declaring the required variables.
    private DocumentReference documentReference;
    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private EditText EURFat;
    private EditText EURSnf;
    private EditText EURRate;
    private TextView TCurrentRate;
    private Button BSubmitAddRates;
    public String milkType = "Cow";
    private ProgressBar progressBar;
    private LinearLayout LLinearLayoutRate;
    float fFat;
    float fSnf;
    float fRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_rates);
        progressBar = findViewById(R.id.addUpdateRecordProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
        TCurrentRate = findViewById(R.id.CurrentRate);
        LLinearLayoutRate = findViewById(R.id.LinearLayoutRate);
        LLinearLayoutRate.setVisibility(View.GONE);


        //For email ID initializing the FireAuth instance.
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        EURFat = findViewById(R.id.URFat);
        EURSnf = findViewById(R.id.URSnf);
        EURRate = findViewById(R.id.URSnf);
        EURFat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getRecord();
            }
        });

        EURSnf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getRecord();
            }
        });


        BSubmitAddRates = findViewById(R.id.SubmitAddRates);
        BSubmitAddRates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EURFat = findViewById(R.id.URFat);
                EURSnf = findViewById(R.id.URSnf);
                EURRate = findViewById(R.id.URRate);
                // validations for input email and password


                if (TextUtils.isEmpty(EURFat.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter Fat!",
                            Toast.LENGTH_LONG)
                            .show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(EURSnf.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter SNF!",
                            Toast.LENGTH_LONG)
                            .show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (TextUtils.isEmpty(EURRate.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter Rate!",
                            Toast.LENGTH_LONG)
                            .show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                    fFat = Float.parseFloat(EURFat.getText().toString());
                    fSnf = Float.parseFloat(EURSnf.getText().toString());
                    fRate = Float.parseFloat(EURRate.getText().toString());
                String sFat = String.format("%.1f", fFat);
                String sSnf = String.format("%.1f", fSnf);
                String sRate = String.format("%.1f", fRate);
                if (sFat.isEmpty() || sSnf.isEmpty() || sRate.isEmpty()) {
                    return;
                }

                Rate rate = new Rate(fFat, fSnf, fRate);

                documentReference.set(rate)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (TCurrentRate.isShown()) {
                                    Toast.makeText(getApplicationContext(), "Rate Updated Successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Rate Added Successfully!", Toast.LENGTH_SHORT).show();
                                }

                                //Finishing current Activity
                                finish();
                                //Staring the same Activity Again.
                                Intent intent = new Intent(getApplicationContext(), AddUpdateRates.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(this.getClass().getName(), "Error writing document", e);
                            }
                        });

            }
        });
    }


    public void getRecord() {
        EURFat = findViewById(R.id.URFat);
        EURSnf = findViewById(R.id.URSnf);
        EURRate = findViewById(R.id.URRate);
        if (!EURFat.getText().toString().isEmpty())
            fFat = Float.parseFloat(EURFat.getText().toString());
        if (!EURSnf.getText().toString().isEmpty())
            fSnf = Float.parseFloat(EURSnf.getText().toString());
        //if (!EURRate.getText().toString().isEmpty())
          //  fRate = Float.parseFloat(EURRate.getText().toString());
        String sFat = String.format("%.1f", fFat);
        String sSnf = String.format("%.1f", fSnf);
        //String sRate = String.format("%.1f", fRate);
        if (sFat.isEmpty() || sSnf.isEmpty()) {
            return;
        }

        //For email ID initializing the FireAuth instance.
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String email = user.getEmail();
//        /milkRates/user@mail.com/milkType/Cow/3.2/8.3

        documentReference = mFirebaseDatabase.collection("milkRates").document(email).collection("milkType").document(milkType).collection(sFat).document(sSnf);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    //On Successfully retrieving the document putting values to respective view
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double dRate = document.getDouble("rate");
                        String sRate = dRate.toString();
                        //TCurrentRate = findViewById(R.id.CurrentRate);

                        TCurrentRate.setText(sRate);
                        Toast.makeText(getApplicationContext(), "Current Rate: "+ sRate, Toast.LENGTH_SHORT).show();
                        LLinearLayoutRate.setVisibility(View.VISIBLE);

                    } else {
                        Log.d(this.getClass().getName(), "ADD/UPDATE RATES: No such document");
                        LLinearLayoutRate.setVisibility(View.GONE);
                    }
                } else {
                    Log.d(this.getClass().getName(), "ADD/UPDATE RATES:  get failed with ", task.getException());
                    LLinearLayoutRate.setVisibility(View.GONE);
                }
            }
        });

    }



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_cow:
                if (checked)
                    milkType = "Cow";
                break;
            case R.id.radio_buffalo:
                if (checked)
                    milkType = "Buffalo";
                break;
            default:
                milkType = "Cow";
        }
    }

}