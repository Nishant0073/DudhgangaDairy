package com.miniprojectG3.dudhgangadairy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.shape.MaterialShapeDrawable;

public class AboutActivity extends AppCompatActivity {
    private  TextView TLinkedIn;
    private  TextView TGitBub;
    private TextView TEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TLinkedIn = findViewById(R.id.DLinkedIn);
        TGitBub = findViewById(R.id.DGithub);
        TEmailId = findViewById(R.id.DEMail);
        MaterialShapeDrawable shapeDrawable = new MaterialShapeDrawable();
        shapeDrawable.setFillColor(ContextCompat.getColorStateList(this,android.R.color.transparent));
        shapeDrawable.setStroke(1.0f, ContextCompat.getColor(this,R.color.blue_dark));
        ViewCompat.setBackground(TLinkedIn,shapeDrawable);
        ViewCompat.setBackground(TGitBub,shapeDrawable);
        ViewCompat.setBackground(TEmailId,shapeDrawable);

       TLinkedIn.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               startActivity(new Intent(Intent.ACTION_VIEW,
                       Uri.parse("https://www.linkedin.com/in/nishant0073/")));
           }
       });
       TGitBub.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               // TODO Auto-generated method stub
               startActivity(new Intent(Intent.ACTION_VIEW,
                       Uri.parse("https://github.com/Nishant0073")));
           }
       });
        TEmailId.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, "nishantks121@gmail.com");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }
}