package com.miniprojectG3.dudhgangadairy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShowBillCustomer extends AppCompatActivity {

    //private EditText ECSUCID;
    private TextView TCSUCustomerName;
    private TextView TCSUFromDateDisplay;
    private TextView TCSUToDateDisplay;
    private TextView TCSUTotalCostDisplay;
    private LinearLayout LLCSUInformationMain;
    private ListView LVCSUMilkListFarmer;
    private TextView TCNoMoreRecordsMessage;

    private DocumentReference documentReference;
    private FirebaseFirestore mFirebaseDatabase = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    public String cName;
    public String MFdate;
    public String Fdate;
    public int id;
    public float fat;
    public float snf;
    public float weight;
    public String emailID;
    public String sUniqueId;
    public String startDate = "";
    public String endDate = "";
    public float cost = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bill_customer);
        LLCSUInformationMain = findViewById(R.id.CSUInformationMain);
        LVCSUMilkListFarmer = findViewById(R.id.CSUMilkListFarmer);
        TCSUFromDateDisplay = findViewById(R.id.CSUFromDateDisplay);
        TCSUToDateDisplay = findViewById(R.id.CSUToDateDisplay);
        TCSUTotalCostDisplay = findViewById(R.id.CSUTotalCostDisplay);
        TCNoMoreRecordsMessage = findViewById(R.id.CNoMoreRecordsMessage);

        //LLCSUInformationMain.setVisibility(View.INVISIBLE);
        //LVCSUMilkListFarmer.setVisibility(View.INVISIBLE);
        //   ECSUCID = findViewById(R.id.CSUCID);

        findCustomer();

    }

    private void findCustomer() {

        TCSUCustomerName = findViewById(R.id.CSUCustomerName);
        //Using the FirebaseAuth to get email id of customer.


        findDate();
        SharedPreferences prefs = this.getSharedPreferences("com.miniprojectG3.dudhgangadairy", Context.MODE_PRIVATE);
        int uniqueID = prefs.getInt("Authentication_Id", 0);
        id = uniqueID;
        sUniqueId = Integer.toString(uniqueID);
        emailID = prefs.getString("Authentication_email", "");

        documentReference = mFirebaseDatabase.collection("customers").document(emailID).collection("customers").document(sUniqueId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //Setting the customer name.
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        cName = document.getString("name");
                        TCSUCustomerName.setText(cName);
                        showBill();

                    } else {
                        Log.d(this.getClass().getName(), "ShowBillUser: No such document");
                        Toast.makeText(getApplicationContext(), "Enter Valid customer ID", Toast.LENGTH_SHORT).show();
                        TCSUCustomerName.setText("Customer Name");
                        return;
                    }
                } else {
                    Log.d(this.getClass().getName(), "ShowBillUser:get failed with ", task.getException());
                }
            }
        });

    }


    private void findDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String Fdate = (formatter.format(date));
        //      Fdate = "08-05-2020";


        String sDate = "";
        sDate = Fdate.substring(0, 2);
        String sMonth = "";
        sMonth = Fdate.substring(3, 5);
        String sYear = "";
        sYear = Fdate.substring(6, 10);

        int iDate = Integer.valueOf(sDate);
        int iMonth = Integer.valueOf(sMonth);
        int iYear = Integer.valueOf(sYear);

        if (iDate >= 1 && iDate <= 10) {
            if (iMonth == 3) {
                startDate += "21-02-" + sYear;
                if (((iYear % 4 == 0) && (iYear % 100 != 0)) || (iYear % 400 == 0))
                    endDate += "28-02-" + sYear;
                else
                    endDate += "29-02-" + sYear;
            } else if (iMonth == 2 || iMonth == 4 || iMonth == 6 || iMonth == 9 || iMonth == 11 || iMonth == 1) {
                if (iMonth == 1) {
                    String tsYear = Integer.toString(iYear - 1);
                    startDate += "21-12-" + tsYear;
                    endDate += "31-12" + tsYear;
                } else {
                    startDate += "21-" + Integer.toString(iMonth - 1) + "-" + sYear;
                    endDate += "31-" + Integer.toString(iMonth - 1) + "-" + sYear;
                }
            } else {
                startDate += "21-" + sMonth + "-" + sYear;
                endDate += "30-" + sMonth + "-" + sYear;
            }
        } else if (iDate >= 11 && iDate <= 20) {
            startDate += "01-" + sMonth + "-" + sYear;
            endDate += "10-" + sMonth + "-" + sYear;
        } else {
            startDate += "11-" + sMonth + "-" + sYear;
            endDate += "20-" + sMonth + "-" + sYear;
        }

    }

    private void showBill() {
        ArrayList<Record> records = new ArrayList<Record>();
        SharedPreferences prefs = this.getSharedPreferences("com.miniprojectG3.dudhgangadairy", Context.MODE_PRIVATE);
        int uniqueID = prefs.getInt("Authentication_Id", 0);
        emailID = prefs.getString("Authentication_email", "");
        String sDate = "";
        sDate += startDate.charAt(0) + startDate.charAt(1);
        String eDate = "";
        eDate += endDate.charAt(0) + endDate.charAt(1);
        Log.v(this.getClass().toString(), "DATE:" + startDate + " " + endDate + " Dates");
        TCSUFromDateDisplay.setText(startDate);
        TCSUToDateDisplay.setText(endDate);

        int siDate = Integer.valueOf(startDate.substring(0, 2));
        int eiDate = Integer.valueOf(endDate.substring(0, 2));


        for (int i = siDate; i <= eiDate; i++) {
            String fDate = "";
            String fDay = Integer.toString(i);
            if (fDay.length() == 1)
                fDate += '0' + fDay;
            else
                fDate += fDay;
            fDate += endDate.substring(2, 10);
            Log.v(this.getClass().getName(), "DATE:" + "Final Date:" + fDate);

            mFirebaseDatabase.collection("MRecords").document(emailID).collection("ALL").whereEqualTo("id", uniqueID).whereEqualTo("date", fDate)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            records.add(documentSnapshot.toObject(Record.class));
                            Record record = documentSnapshot.toObject(Record.class);
                            Float sCost = record.cost;
                            addCost(sCost);
                            //.setText(sCost + "Rs");

                        }
                        milkRecordFarmerAdapter milkRecordFarmerAdapter1 = new milkRecordFarmerAdapter(ShowBillCustomer.this, records);
                        ListView listView = (ListView) findViewById(R.id.CSUMilkListFarmer);
                        listView.setAdapter(milkRecordFarmerAdapter1);
                    } else {
                        System.out.println("Record: ERROR At ShowBilCustomer Activity!");
                    }
                }
            });
        }


    }

    private void addCost(Float sCost) {
        cost += sCost;
        TCSUTotalCostDisplay = findViewById(R.id.CSUTotalCostDisplay);
        TCSUTotalCostDisplay.setText(Float.toString(cost) + "Rs");
    }
}