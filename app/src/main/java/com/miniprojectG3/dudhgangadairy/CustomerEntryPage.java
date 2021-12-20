package com.miniprojectG3.dudhgangadairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerEntryPage extends AppCompatActivity {


    private DocumentReference documentReference;
    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private EditText ECCemail;
    private EditText ECUpassword;
    private Button BCClogin;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_entry_page);
            progressBar = findViewById(R.id.CEprogressBar);
            BCClogin = findViewById(R.id.CClogin);
            BCClogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ECUpassword = findViewById(R.id.CUpassword);
                    ECCemail = findViewById(R.id.CCemail);
                    progressBar.setVisibility(View.VISIBLE);
                    String num = ECUpassword.getText().toString().trim();
                    String emailID = ECCemail.getText().toString().trim();

                    // validations for input email and password
                    if (TextUtils.isEmpty(num)) {
                        Toast.makeText(getApplicationContext(),
                                "Please enter Unique ID!",
                                Toast.LENGTH_LONG)
                                .show();
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }

                    if (TextUtils.isEmpty(emailID)) {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your Collector mail ID!!",
                                Toast.LENGTH_LONG)
                                .show();
                        progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }
                    int uniqueID = Integer.parseInt(num);
                    documentReference =  mFirebaseDatabase.collection("customers").document(emailID).collection("customers").document(num);

                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {

                                //On Successfully retrieving the document putting values to respective view
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    SharedPreferences preferences = getSharedPreferences("com.miniprojectG3.dudhgangadairy", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putInt("Authentication_Id", uniqueID);
                                    editor.putString("Authentication_email",emailID);
                                    String name = document.getString("name");
                                    editor.putString("Customer_name",name);
                                    editor.apply();


                                    Toast.makeText(getApplicationContext(), "Logged In!", Toast.LENGTH_SHORT).show();
                                    //Staring CustomerHomePage Activity to create profile.
                                    Intent intent = new Intent(CustomerEntryPage.this, CustomerHomePage.class);
                                    startActivity(intent);

                                } else {
                                    Log.d(this.getClass().getName(), "CUSTOMER PROFILE: No such document");
                                    Toast.makeText(getApplicationContext(), "Farmer not found!Make sure that you have entered correct Credential", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                Log.d(this.getClass().getName(), " CUSTOMER PROFILE:  get failed with ", task.getException());
                                Toast.makeText(getApplicationContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
    }
}