package com.miniprojectG3.dudhgangadairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomePage extends AppCompatActivity {

    //Declaring the required variables.
    private DocumentReference documentReference;
    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user ;

    private Button addCustomerBtn;
    private Button listCustomers;
    private Button takeReadings;
    private Button addRates;
    private Button BShowBill;
    private TextView TDairyName;
    private TextView TUserName;
    private TextView TUserEmail;
    private String userName="";
    private String dairyName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        //For email ID initializing the FireAuth instance.
        mAuth = FirebaseAuth.getInstance();
        user =  mAuth.getCurrentUser();
        String emailID = user.getEmail();
        TUserName = findViewById(R.id.UserEmail);
        TUserName.setText(emailID);

        TDairyName = findViewById(R.id.DairyName);
        TUserName = findViewById(R.id.UserName);

        //Referring to the location of customer where customer's profile is stored
        documentReference =  mFirebaseDatabase.collection("users").document(emailID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    //On Successfully retrieving the document putting values to respective view
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        TDairyName.setText(document.getString("nameOfDairy"));
                        TUserName.setText(document.getString("nameOfOwner"));

                    } else {
                        Log.d(this.getClass().getName(), "HOME PAGE USER PROFILE: No such document");
                    }
                } else {
                    Log.d(this.getClass().getName(), " HOME PAGE USER PROFILE:  get failed with ", task.getException());
                }
            }
        });



        addCustomerBtn = (Button) findViewById(R.id.AddCustomer);
        //When Add customer button is clicked then start new Add customer Activity.
        addCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this,AddCustomer.class);
                startActivity(intent);
            }
        });

        listCustomers = findViewById(R.id.ShowAllCustomers);

        listCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(HomePage.this,CustomersList.class);
                startActivity(intent);
            }
        });

        takeReadings = findViewById(R.id.TakeReading);
        takeReadings.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HomePage.this,TakeReadings.class);
                        startActivity(intent);

                    }
                }
        );


        addRates = findViewById(R.id.AddRates);
        addRates.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HomePage.this,AddUpdateRates.class);
                        startActivity(intent);

                    }
                }
        );

        BShowBill = findViewById(R.id.ShowBill);
        BShowBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this,ShowBillUser.class);
                startActivity(intent);
            }
        });


    }

      //Providing option menu for logout the user.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.SignOut:
                SignOut();
                return true;
            case R.id.About:
                About();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void About()
    {
        Intent intent = new Intent(HomePage.this,AboutActivity.class);
        startActivity(intent);
    }
    public void SignOut()
    {
        mAuth = FirebaseAuth.getInstance();
        user =  mAuth.getCurrentUser();
        mAuth.signOut();
        Intent intent = new Intent(HomePage.this,MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}