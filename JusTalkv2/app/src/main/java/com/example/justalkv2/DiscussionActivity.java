package com.example.justalkv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DiscussionActivity extends AppCompatActivity {

    Button btnSendMsg;
    Button btnClrTxt;
    EditText etMsg;
    ListView lvdiscussion;
    ArrayList<String> listConversation = new ArrayList<String>();
    ArrayAdapter arrayAdpt;

    String UserName, SelectedTopic,user_msg_key;
    private DatabaseReference dbr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        btnClrTxt = (Button) findViewById(R.id.btnClrTxt);
        btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        etMsg = (EditText) findViewById(R.id.etMessages);
        lvdiscussion = (ListView) findViewById(R.id.lvConversation);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listConversation);
        lvdiscussion.setAdapter(arrayAdpt);

        UserName = getIntent().getExtras().get("user_name").toString();
        SelectedTopic = getIntent().getExtras().get("selected_topic").toString();
        setTitle(SelectedTopic);

        btnClrTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMsg.setText("");
            }
        });

        lvdiscussion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickData = (String) lvdiscussion.getItemAtPosition(position);
                String[] clickedData = clickData.split(":",2);
                String fdata = clickedData[1].substring(0,clickedData[1].length()-15);

                ClipData myClip;
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label",fdata);
                clipboard.setPrimaryClip(clip);
            }
        });



        dbr = FirebaseDatabase.getInstance().getReference().child(SelectedTopic);


        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currTime = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                String fd = df.format(currTime);
                Map<String, Object> map = new HashMap<String, Object>();
                user_msg_key = dbr.push().getKey();
                dbr.updateChildren(map);

                DatabaseReference dbr2 = dbr.child(user_msg_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("msg",etMsg.getText().toString());
                map2.put("user",UserName);
                map2.put("timestamp",fd);
                dbr2.updateChildren(map2);
                etMsg.setText("");

            }
        });

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateConversation(dataSnapshot);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateConversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void updateConversation(DataSnapshot dataSnapshot){
        String msg,user,conversation,timestamp;
        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            msg = (String) ((DataSnapshot)i.next()).getValue();
            timestamp = (String) ((DataSnapshot)i.next()).getValue();
            user = (String) ((DataSnapshot)i.next()).getValue();


            conversation = user + ": " + msg+ "          "+ timestamp;
            arrayAdpt.add(conversation);
            arrayAdpt.notifyDataSetChanged();


        }

    }
}
