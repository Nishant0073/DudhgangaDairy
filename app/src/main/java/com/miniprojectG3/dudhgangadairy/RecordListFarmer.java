package com.miniprojectG3.dudhgangadairy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class RecordListFarmer extends AppCompatActivity {
    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user ;
    private String emailID;
    private DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list_farmer);

        ArrayList<Record> records = new ArrayList<Record>();
        SharedPreferences prefs = this.getSharedPreferences("com.miniprojectG3.dudhgangadairy", Context.MODE_PRIVATE);
        int uniqueID = prefs.getInt("Authentication_Id", 0);
        emailID  = prefs.getString("Authentication_email","");
        mFirebaseDatabase.collection("MRecords").document(emailID).collection("ALL").whereEqualTo("id",uniqueID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        records.add(documentSnapshot.toObject(Record.class));
                    }
                    Collections.sort(records,(Record r1, Record r2) -> {
                        try {
                            return new SimpleDateFormat("dd-MM-yyyy").parse(r1.date).compareTo(new SimpleDateFormat("dd-MM-yyyy").parse(r2.date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    });
                    milkRecordFarmerAdapter milkRecordFarmerAdapter1 = new milkRecordFarmerAdapter(RecordListFarmer.this, records);
                    ListView listView = (ListView) findViewById(R.id.MilkListFarmer);
                    listView.setAdapter(milkRecordFarmerAdapter1);
                }
            }

        });
    }
}
