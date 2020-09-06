package com.example.justalkv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;

    ListView lvDiscussionTopics;
    ArrayList<String> listofDiscussion = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    Button btnChange;
    Button btnNotify;

    String UserName;

    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);
        lvDiscussionTopics = (ListView) findViewById(R.id.lvDiscussionTopics);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,listofDiscussion);
        lvDiscussionTopics.setAdapter(arrayAdpt);
        UserName = Build.MODEL;
        btnChange = (Button) findViewById(R.id.btnChange);
        //btnNotify = (Button) findViewById(R.id.btnNotify);

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();
                while(i.hasNext())
                {
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                arrayAdpt.clear();
                arrayAdpt.addAll(set);
                arrayAdpt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lvDiscussionTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),DiscussionActivity.class);
                i.putExtra("selected_topic",((TextView)view).getText().toString());
                i.putExtra("user_name",UserName);
                startActivity(i);
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUserName();

            }
        });



    }


    @Override
    public void onBackPressed(){
        finishAffinity();
        finish();
    }
    private void GetUserName(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText username = new EditText(this);
        builder.setTitle("Enter Name");
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setView(username);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserName = username.getText().toString();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Operation Cancelled",Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();

    }
}
