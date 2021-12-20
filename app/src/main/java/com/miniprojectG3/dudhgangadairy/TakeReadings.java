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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TakeReadings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinT;
    private Spinner spinMT;
    private EditText ECID;
    private TextView TViewCName;
    private EditText EID;
    private EditText EFat;
    private EditText ESnf;
    private EditText EWeight;
    private Button BSubmitRecord;
    private ProgressBar progressBar;
    private String valTime = "Morning";
    private String valMilkType = "Cow ";
    private float rate;
    private DocumentReference documentReference;
    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    public String cName;
    public String Fdate;
    public int id;
    public float fat;
    public float snf;
    public float weight;
    public String emailID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_readings);
        //Providing the option to spinner.
        String[] time = {TakeReadings.this.getString(R.string.Morning), getResources().getString(R.string.Night)};
        String[] milkType = {TakeReadings.this.getString(R.string.Cow), getResources().getString(R.string.Buffalo)};

        progressBar = findViewById(R.id.takeProgress);
        progressBar.setVisibility(View.INVISIBLE);
        //Spinner for provide the time options to select.
        spinT = (Spinner) findViewById(R.id.LTime);
        spinT.setOnItemSelectedListener(this);

        //Spinner for provide the milkType options to select.
        spinMT = (Spinner) findViewById(R.id.LMilkTYpe);
        spinMT.setOnItemSelectedListener(this);

        //Setting the ArrayAdapter data on the Spinner of time
        ArrayAdapter Atime = new ArrayAdapter(this, android.R.layout.simple_spinner_item, time);
        Atime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinT.setAdapter(Atime);

        //Setting the ArrayAdapter data on the Spinner to milk type
        ArrayAdapter Amilktype = new ArrayAdapter(this, android.R.layout.simple_spinner_item, milkType);
        Amilktype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinMT.setAdapter(Amilktype);

        //Retrieving the ID to show the name of customer.
        ECID = findViewById(R.id.RCID);

        //Using TextWatcher to get the name of customer when number changes.
        ECID.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                ECID = findViewById(R.id.RCID);
                String sUniqueId = ECID.getText().toString();
                TViewCName = findViewById(R.id.RCustomerName);

                if (!sUniqueId.isEmpty()) {

                    //Using the FirebaseAuth to get email id of customer.
                    mAuth = FirebaseAuth.getInstance();
                    user = mAuth.getCurrentUser();
                    emailID = user.getEmail();


                    //Referring to the location where Profile of customer is stored.
                    documentReference = mFirebaseDatabase.collection("customers").document(emailID).collection("customers").document(sUniqueId);

                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                //Setting the customer name.
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    cName = document.getString("name");
                                    TViewCName.setText(cName);

                                } else {
                                    Log.d(this.getClass().getName(), "TAKE READINGS: No such document");
                                    Toast.makeText(getApplicationContext(), "Enter Valid customer ID", Toast.LENGTH_SHORT).show();
                                    ECID.setText("");
                                    TViewCName.setText("Customer Name");
                                    return;
                                }
                            } else {
                                Log.d(this.getClass().getName(), "TAKE READING:  get failed with ", task.getException());
                            }
                        }
                    });
                }

            }
        });

        //Setting the value of time and milkType to variable when time changes
        spinT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                valTime = time[i];
                if(i==0)
                {
                    valTime = "Morning";
                }
                else
                {
                    valTime ="Evening";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Setting the value of time and milkType to variable when milkType changes
        spinMT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                valMilkType = milkType[i];
                if(i==0)
                {
                    valMilkType = "Cow";
                }
                else
                {
                    valMilkType = "Buffalo";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        BSubmitRecord = findViewById(R.id.RSubmitRecord);

        //When submit button is clicked then call the submitRecord function.
        BSubmitRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRecord();
            }
        });


    }

    private void submitRecord() {

        progressBar.setVisibility(View.VISIBLE);
        //Accessing the Views from the activity_take_readings.xml
        EID = findViewById(R.id.RCID);
        EFat = findViewById(R.id.RFat);
        ESnf = findViewById(R.id.RSnf);
        EWeight = findViewById(R.id.RWeight);


        String sEID = EID.getText().toString();
        String sFat = EFat.getText().toString();
        String sSnf = ESnf.getText().toString();
        String sWeight = EWeight.getText().toString();


        if (TextUtils.isEmpty(sEID)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter ID of Customer!",
                    Toast.LENGTH_LONG)
                    .show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        if (TextUtils.isEmpty(sFat)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter Fat!",
                    Toast.LENGTH_LONG)
                    .show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (TextUtils.isEmpty(sSnf)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter SNF!",
                    Toast.LENGTH_LONG)
                    .show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        if (TextUtils.isEmpty(sWeight)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter Weight!",
                    Toast.LENGTH_LONG)
                    .show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        //Validating the input is not empty.
        if (sEID.isEmpty() || sFat.isEmpty() || sSnf.isEmpty() || sWeight.isEmpty()) {
            return;
        }


        //Coveting the strings to the respective datatype to store as object.
        float fat = Float.parseFloat(sFat);
        float snf = Float.parseFloat(sSnf);

        //Calling function getRate to get the rate based on fat and snf
        getRate(fat, snf, valMilkType);
    }

    //Created the setter function to set values from the interface class
    public void setRate(float rate) {
        String sRate = String.format("%.1f", rate);
        rate = Float.parseFloat(sRate);
        this.rate = rate;
    }

    private void getRate(float fat, float snf, String milkType) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        //Getting the user email id for the finding the rate of respective user
        String email = user.getEmail();
        String sFat = String.format("%.1f", fat);
        String sSnf = String.format("%.1f", snf);


        //Referring the document where rates are stored
        documentReference = mFirebaseDatabase.collection("milkRates")
                .document(email)
                .collection("milkType")
                .document(valMilkType)
                .collection(sFat)
                .document(sSnf);

        //Accessing the document
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Setting the rate.
                        Double temp = document.getDouble("rate");
                        float tRate = temp.floatValue();
                        setRate(tRate);
                        submitRecordAfterRates();

                    } else {
                        Log.d(this.getClass().getName(), "ERROR TAKING READING PROFILE: No such document");
                        //Giving the message to user.
                        Toast.makeText(getApplicationContext(), "Please Enter valid FAT and SNF!", Toast.LENGTH_SHORT).show();
                        //Finishing current Activity
                        finish();
                        //Staring the same Activity Again.
                        Intent intent = new Intent(TakeReadings.this, TakeReadings.class);
                        startActivity(intent);
                    }
                } else {
                    Log.d(this.getClass().getName(), "ERROR TAKING READING:  get failed with ", task.getException());
                }
            }
        });
    }

    private void submitRecordAfterRates() {

        //Accessing the Views from the activity_take_readings.xml
        EID = findViewById(R.id.RCID);
        EFat = findViewById(R.id.RFat);
        ESnf = findViewById(R.id.RSnf);
        EWeight = findViewById(R.id.RWeight);


        //Converting to string with 1 decimal format.
        String sEID = EID.getText().toString();
        String sFat = EFat.getText().toString();
        String sSnf = ESnf.getText().toString();
        String sWeight = EWeight.getText().toString();


        //Coveting the strings to the respective datatype to store as object.
        id = Integer.parseInt(sEID);
        fat = Float.parseFloat(sFat);
        snf = Float.parseFloat(sSnf);
        weight = Float.parseFloat(sWeight);

        //Getting the date to store the records on the database.

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            Fdate = (formatter.format(date));

            //Creating the record object.
            Record record = new Record(cName, valTime, id, fat, snf, weight, (weight * rate), rate, Fdate, valMilkType);


        //Adding records to database
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        emailID = user.getEmail();

        documentReference = mFirebaseDatabase.collection("MRecords").document("user").collection(emailID).document(Fdate).collection(EID.getText().toString()).document(valTime).collection(valMilkType).document("Record");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                            documentReference = mFirebaseDatabase.collection("MRecords").document("user").collection(emailID).document(Fdate).collection(EID.getText().toString()).document(valTime).collection(valMilkType).document("Record");
                        Toast.makeText(getApplicationContext(), "Record Already Exist!", Toast.LENGTH_SHORT).show();
                        Log.d(this.getClass().toString(), "Document Already exist! from checkIfAlreadyExist Take Reading");
                        progressBar.setVisibility(View.INVISIBLE);

                    } else {
                            documentReference.set(record)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Log.d(this.getClass().getName(), "TAKING READING: successfully written!");
                                            mFirebaseDatabase.collection("MRecords").document("user").collection(emailID).document(Fdate).collection(EID.getText().toString()).document(valTime).collection(valMilkType).document("Record").set(record);
                                            mFirebaseDatabase.collection("MRecords").document(emailID).collection("ALL").document().set(record);
                                            //Giving the message to user.
                                            Toast.makeText(getApplicationContext(), "Record Added successfully!", Toast.LENGTH_SHORT).show();
                                            //Finishing current Activity
                                            finish();
                                            //Staring the same Activity Again.
                                            Intent intent = new Intent(TakeReadings.this, TakeReadings.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(this.getClass().getName(), "TAKING READING:Error writing document", e);
                                            //Giving the message to user.
                                            Toast.makeText(getApplicationContext(), " Network ERROR!", Toast.LENGTH_SHORT).show();
                                            //Finishing current Activity
                                            finish();
                                            //Staring the same Activity Again.
                                            Intent intent = new Intent(TakeReadings.this, TakeReadings.class);
                                            startActivity(intent);
                                        }
                                    });
                            Log.d(this.getClass().toString(), "Document does not exist! from checkIfAlreadyExist Take Reading");
                    }
                } else {
                    Log.d(this.getClass().toString(), "from checkIfAlreadyExist Take ReadingFailed with: ", task.getException());
                }
            }

        });
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //TODO
    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


}