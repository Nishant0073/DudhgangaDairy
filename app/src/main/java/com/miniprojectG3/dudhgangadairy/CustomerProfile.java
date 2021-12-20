package com.miniprojectG3.dudhgangadairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerProfile extends AppCompatActivity {

    //Declaring the required variables.
    private DocumentReference documentReference;
    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user ;

    private Button BBackToHome;
    private String uniqueId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        //Retrieving unique pass by the AddCustomer Activity.
        Bundle extras = getIntent().getExtras();
        uniqueId = extras.getString("UniqueID");

        //For email ID initializing the FireAuth instance.
        mAuth = FirebaseAuth.getInstance();
        user =  mAuth.getCurrentUser();

        //Finding the location of the views  in xml.
        TextView TName = findViewById(R.id.TextName);
        TextView TPhoneNumber = findViewById(R.id.TextPhoneNumber);
        TextView TUniqueId = findViewById(R.id.TextUniqueId);
        String emailID = user.getEmail();

        //Referring to the location of customer where customer's profile is stored
        documentReference =  mFirebaseDatabase.collection("customers").document(emailID).collection("customers").document(uniqueId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    //On Successfully retrieving the document putting values to respective view
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       TName.setText(document.getString("name"));
                       TPhoneNumber.setText(document.getString("phoneNumber"));
                       //Converting the unique ID to long because the FireStore store as the long.
                        Long uniqueS = (Long) document.get("uniqueId");
                        String strUniqueS = Long.toString(uniqueS);
                       TUniqueId.setText(strUniqueS);

                    } else {
                        Log.d(this.getClass().getName(), "CUSTOMER PROFILE: No such document");
                    }
                } else {
                    Log.d(this.getClass().getName(), " CUSTOMER PROFILE:  get failed with ", task.getException());
                }
            }
        });



        //Giving option  to the user directly go to HomePage activity instead of AddCustomer  Activity
        BBackToHome = findViewById(R.id.BackToHome);

        BBackToHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(CustomerProfile.this,HomePage.class);
                startActivity(intent);
            }
        });

    }
    //When back is pressed then directly start home page activity
    @Override
    public void onBackPressed() {
        //Finishing current Activity
        finish();
        //Staring the  Add Customer Activity
        Intent intent = new Intent(this,AddCustomer.class);
        startActivity(intent);


    }

}