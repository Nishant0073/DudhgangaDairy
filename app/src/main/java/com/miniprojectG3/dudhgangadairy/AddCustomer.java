package com.miniprojectG3.dudhgangadairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class AddCustomer extends AppCompatActivity {
    private DocumentReference documentReference;
    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user ;

    private EditText ECustomerName ;
    private EditText EPhoneNumber;
    private Button EAddCustomer;
    private String name;
    private String phoneNumber;
    private int uniqueId;
    private String emailID;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        progressBar = findViewById(R.id.AAddUser);
        progressBar.setVisibility(View.INVISIBLE);
        ECustomerName = findViewById(R.id.CUserName);
        EPhoneNumber = findViewById(R.id.CContactNumber);
        EAddCustomer = findViewById(R.id.CAddCustomer);

        //When CAddCustomer button is pressed
        EAddCustomer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Getting the form.
                name = (ECustomerName.getText().toString()).trim();
                phoneNumber = (EPhoneNumber.getText().toString()).trim();
                mAuth = FirebaseAuth.getInstance();
                mFirebaseDatabase = FirebaseFirestore.getInstance();
                user = mAuth.getCurrentUser();
                emailID = user.getEmail();

                //Retrieving the uniqueID initializer from the current user to assign the Unique id to the customer.
                documentReference = mFirebaseDatabase.collection("users").document(emailID);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();

                    //After retrieving the Unique id store in the uniqueId variable and increment it and store again in current user.
                    if(document.exists())
                    {
                        User  user = document.toObject(User.class);
                        uniqueId = user.uniqueInitialize;
                        documentReference.update("uniqueInitialize",uniqueId+1);
                        //Then adding the customer to database with the UniqueId.
                        addCustomer();
                    }
                    else
                    {
                        System.out.println("ERROR WHILE LOADING!");
                    }
                }
            }
        });
            }

            private void addCustomer() {
                //Getting all data from form
                ECustomerName = findViewById(R.id.CUserName);
                EPhoneNumber = findViewById(R.id.CContactNumber);
                name = ECustomerName.getText().toString();
                phoneNumber = EPhoneNumber.getText().toString();

                // validations for input email and password


                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your Name!",
                            Toast.LENGTH_LONG)
                            .show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter Phone Number!",
                            Toast.LENGTH_LONG)
                            .show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }


                Customer customer = new Customer(name,uniqueId,phoneNumber);
                EAddCustomer = findViewById(R.id.CAddCustomer);

                //Disabling the submit button.
                EAddCustomer.setEnabled(false);

                //Adding customer.
                String sUniqueId = Integer.toString(uniqueId);
                documentReference =  mFirebaseDatabase.collection("customers").document(emailID).collection("customers").document(sUniqueId);
                documentReference.set(customer)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.d(this.getClass().getName(), "Customer successfully Added!");

                                //Staring new CustomerProfile activity
                                Intent intent = new Intent(AddCustomer.this, CustomerProfile.class);
                                //Passing Unique Id with Intent.
                                intent.putExtra("UniqueID", sUniqueId);
                                startActivity(intent);
                            }
                        })
                        //Handling Error
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(this.getClass().getName(), "Error writing Customer", e);
                            }
                        });
                return;
            }
        });

    }

    //When back is pressed then directly start home page activity
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(AddCustomer.this,HomePage.class);
        startActivity(intent);

    }
}