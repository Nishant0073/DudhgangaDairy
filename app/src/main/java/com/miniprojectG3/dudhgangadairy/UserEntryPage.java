package com.miniprojectG3.dudhgangadairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserEntryPage extends AppCompatActivity {

    private FirebaseFirestore mFirebaseDatabase;
    private String mDate;
    private DocumentReference documentReference;
    public static final int RC_SIGN_IN = 1;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ChildEventListener mChildEventListener;
    public String emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_entry_page);

        //Hiding Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        //Getting Instance from the firebase
        mAuth = FirebaseAuth.getInstance();

        //Requesting User from FireUser
        user = mAuth.getCurrentUser();

        //User not logged In then provide the login and registration option
        if (user == null) {
            setContentView(R.layout.activity_user_entry_page);
            Button SignIn = findViewById(R.id.btnSignIn);
            Button SignUp = findViewById(R.id.btnSignUp);

            //On login Button
            SignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserEntryPage.this, LoginActivity.class);
                    startActivity(intent);
                }
            });

            //On Registration Button
            SignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserEntryPage.this, RegistrationActivity.class);
                    startActivity(intent);
                }
            });
        }
//        //User is already logged In.
//        else {
//            //Requesting Instance from the Firestore.
//            mFirebaseDatabase = FirebaseFirestore.getInstance();
//            //Requesting User information from the Firebase
//            user = mAuth.getCurrentUser();
//
//            //Using email Id for document creation
//            String emailId = user.getEmail();
//
//            //Checking that the user have created profile or not
//            DocumentReference documentReference = mFirebaseDatabase.collection("users").document(emailId);
//            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            //Profile is Already Created .
//                            Intent intent = new Intent(UserEntryPage.this, HomePage.class);
//                            startActivity(intent);
//                        } else {
//                            //Sending CreateUserProfile Activity to create profile.
//                            Intent intent = new Intent(UserEntryPage.this, CreateUserProfile.class);
//                            startActivity(intent);
//
//                        }
//                    }
//                }
//            });
//        }
    }

}