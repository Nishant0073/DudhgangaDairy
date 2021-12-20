package com.miniprojectG3.dudhgangadairy;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.Touch;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


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
        //Hiding Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        //Getting Instance from the firebase
        mAuth = FirebaseAuth.getInstance();

        //Requesting User from FireUser
        user = mAuth.getCurrentUser();
        SharedPreferences prefs = this.getSharedPreferences("com.miniprojectG3.dudhgangadairy", Context.MODE_PRIVATE);
        int uniqueID = prefs.getInt("Authentication_Id", 0);
        if (uniqueID != 0) {
            //Staring CustomerHomePage Activity to create profile.
            loadLocale();
            Intent intent = new Intent(MainActivity.this, CustomerHomePage.class);
            startActivity(intent);
        }
        //User not logged In then provide the login and registration option
        else if (user != null) {
            //Requesting Instance from the Firestore.
            mFirebaseDatabase = FirebaseFirestore.getInstance();
            //Requesting User information from the Firebase
            user = mAuth.getCurrentUser();

            //Using email Id for document creation
            String emailId = user.getEmail();

            //Checking that the user have created profile or not
            DocumentReference documentReference = mFirebaseDatabase.collection("users").document(emailId);
            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //Profile is Already Created .
                            loadLocale();
                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                            startActivity(intent);
                        } else {
                            //Sending CreateUserProfile Activity to create profile.
                            loadLocale();
                            Intent intent = new Intent(MainActivity.this, CreateUserProfile.class);
                            startActivity(intent);

                        }
                    }
                }
            });
        } else {
            loadLocale();
            setContentView(R.layout.activity_main);
            Button BUser = findViewById(R.id.btnUser);
            Button BCustomer = findViewById(R.id.btnCustomer);

            //On login Button
            BUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, UserEntryPage.class);
                    startActivity(intent);
                }
            });

            //On Registration Button
            BCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, CustomerEntryPage.class);
                    startActivity(intent);
                }
            });

            Button BChangeLanguage = findViewById(R.id.ChangeLanguage);
            BChangeLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChangeLanguage();
                }
            });

        }
    }


    private void showChangeLanguage() {
        final String[] listLang = {"English", "मराठी"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(listLang, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setLocal("en");
                    recreate();
                }
                if (which == 1) {
                    setLocal("mr");
                    recreate();
                }
                dialog.dismiss();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void setLocal(String slang) {
        Locale locals = new Locale(slang);
        Locale.setDefault(locals);
        Configuration configuration = new Configuration();
        configuration.locale = locals;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences preferences = getSharedPreferences("com.miniprojectG3.dudhgangadairy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("App_Lang",slang);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = this.getSharedPreferences("com.miniprojectG3.dudhgangadairy", Context.MODE_PRIVATE);
//        int uniqueID = prefs.getInt("Authentication_Id", 0);
//        String name = prefs.getString("Customer_name","");
        String language = prefs.getString("App_Lang", "");
        setLocal(language);
    }


};