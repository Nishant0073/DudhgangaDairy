package com.miniprojectG3.dudhgangadairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomersList extends AppCompatActivity {

    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user ;

    private String emailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_list);

        //Getting the instance of the FirebaseAuth for email ID.
         mAuth = FirebaseAuth.getInstance();
         user = mAuth.getCurrentUser();

         //Creating the customer Arraylist of Customer class.
        ArrayList<Customer> customers = new ArrayList<Customer>();
        emailID = user.getEmail();



         mFirebaseDatabase.collection("customers").document(emailID).collection("customers")
                 .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Adding the object of class Customer to the customer ArrayList.
                        customers.add(document.toObject(Customer.class));

//                        Log.d(this.getClass().getName(),"Document is: " + document.getData().toString());

                        //Creating the customerAdapter to show the list of customer.
                        CustomerAdapter customerAdapter = new CustomerAdapter(CustomersList.this,customers);

                        //Accessing the ListView Form the activity_customers_list.xml file
                        ListView listView = (ListView) findViewById(R.id.CustomerList);
                        //Adding adapter to the ListView.
                        listView.setAdapter(customerAdapter);

                    }
                } else {
                    Log.d(this.getClass().getName(), "Document is Error getting documents: ", task.getException());
                }
                Log.d(this.getClass().getName(), "Document is loaded before! ");
            }

        });
        Log.d(this.getClass().getName(), "Document is loaded after! ");
    }
}