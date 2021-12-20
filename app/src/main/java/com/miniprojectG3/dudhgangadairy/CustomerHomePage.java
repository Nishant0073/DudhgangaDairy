package com.miniprojectG3.dudhgangadairy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerHomePage extends AppCompatActivity {

    private DocumentReference documentReference;
    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private TextView THID ;
    private TextView THName;
    private Button BShowCustomerRecords;
    private Button BCShowBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_page);
        THID = findViewById(R.id.HID);
        THName = findViewById(R.id.HName);
        BShowCustomerRecords = findViewById(R.id.ShowCustomerRecords);
        SharedPreferences prefs = this.getSharedPreferences("com.miniprojectG3.dudhgangadairy", Context.MODE_PRIVATE);
        int uniqueID = prefs.getInt("Authentication_Id", 0);
        String name = prefs.getString("Customer_name","");
        String SUniqueID = Integer.toString(uniqueID);

        THID.setText(SUniqueID);
        THName.setText(name);

        BShowCustomerRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHomePage.this,RecordListFarmer.class);
                startActivity(intent);
            }
        });

        BCShowBill = findViewById(R.id.CShowBill);
        BCShowBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerHomePage.this,ShowBillCustomer.class);
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
        Intent intent = new Intent(CustomerHomePage.this,AboutActivity.class);
        startActivity(intent);
    }

    public  void SignOut()
    {

        SharedPreferences preferences = getSharedPreferences("com.miniprojectG3.dudhgangadairy", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(CustomerHomePage.this,MainActivity.class);
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