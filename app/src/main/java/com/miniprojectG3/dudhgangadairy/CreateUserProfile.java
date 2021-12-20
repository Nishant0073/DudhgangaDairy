package com.miniprojectG3.dudhgangadairy;

import static android.telephony.PhoneNumberUtils.isGlobalPhoneNumber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CreateUserProfile extends AppCompatActivity {
    
    private DocumentReference documentReference;
    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user ;
    public  String emailId;

    private  EditText Ename;
    private  EditText EdairyName;
    private EditText EconcatNumber;
    private  EditText Eaddress;
    private  String contactNumber ;
    private String  name;
    private String dairyName ;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_create_user_profile);
        Button createProfile = (Button) findViewById(R.id.UCreateProfile);

        //On submitting data starting function.
        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUser();
            }

        });
    }

    private void CreateUser() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        //if error is getting while loading the user then move to Home Page.
        if(user!=null)
        {
            addUser();
        }
        else
        {
            Intent intent = new Intent(CreateUserProfile.this,HomePage.class);
            startActivity(intent);
        }
    }

    private void addUser() {
        //Requesting all Values from the form from function getValues.
        if(!getValues())
        {
            //If phone number is not valid.
            Toast.makeText(this, "Enter valid Phone number", Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }


        User user = new User(name,dairyName,emailId,address,contactNumber);

        //Adding new user to database
        documentReference =  mFirebaseDatabase.collection("users").document(emailId);
        documentReference.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(this.getClass().getName(), "DocumentSnapshot successfully written!");
                        Intent intent = new Intent(CreateUserProfile.this, HomePage.class);
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

    private boolean getValues() {

        emailId = user.getEmail();
        Ename = (EditText) findViewById(R.id.UserName);
        EdairyName = (EditText) findViewById(R.id.UDairyName);
        EconcatNumber = (EditText) findViewById(R.id.UContactNumber);
        Eaddress = (EditText) findViewById(R.id.UAddress);
        name  = (Ename.getText().toString()).trim();
        contactNumber = (EconcatNumber.getText().toString()).trim();
        dairyName = (EdairyName.getText().toString()).trim();
        address = (Eaddress.getText().toString()).trim();

        //Checking Phone number is valid or not.
        if(isGlobalPhoneNumber("+91"+contactNumber))
        {
            return true;
        }
        return false;

    }

    //Providing option menu for logout the user.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.SignOut:
                SignOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void SignOut()
    {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        mAuth.signOut();
        Intent intent = new Intent(CreateUserProfile.this,MainActivity.class);
        startActivity(intent);
        System.out.println("Unable to start Intent");
    }
}