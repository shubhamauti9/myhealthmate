package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class expensev extends AppCompatActivity {



    RecyclerView recyclermrlist;
    expenselist recyclerAdaptermrdata;

    ArrayList<ExpenseData> mrlist;
    //String userId;
    //DatabaseReference reference;
    Context mContext;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_expensev);


        SessionManagement sessionManagement = new SessionManagement(expensev.this);
        userId = sessionManagement.getSession();
        mContext = getApplicationContext();

        recyclermrlist = (RecyclerView) findViewById(R.id.recyclermrlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclermrlist.setLayoutManager(layoutManager);
        recyclermrlist.setHasFixedSize(true);
        recyclermrlist.setLayoutManager(new LinearLayoutManager(this));

        mrlist = new ArrayList<ExpenseData>();
        //reference = FirebaseDatabase.getInstance().getReference();

        ClearAll();


        GetDataFromFirebase(userId, mContext);


    }


    public void GetDataFromFirebase(final String userId, final Context mContext) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("expense").child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    ExpenseData mrData = new ExpenseData();
                    mrData.setDate(snapshot.child("date").getValue().toString());
                    mrData.setDetails_str(snapshot.child("details_str").getValue().toString());
                    mrData.setAmount_str(snapshot.child("amount_str").getValue().toString());
                    mrData.setUserId(snapshot.child("userId").getValue().toString());



                    mrlist.add(mrData);

                }
                Log.d("this",mrlist.toString());
                recyclerAdaptermrdata = new expenselist(mContext, mrlist, userId);
                recyclermrlist.setAdapter(recyclerAdaptermrdata);
                recyclerAdaptermrdata.notifyDataSetChanged();





                recyclerAdaptermrdata.setOnItemClickListener(new expenselist.OnItemClickListener(){

                    @Override
                    public void onItemdelete(int position) {
                        String ids = mrlist.get(position).getUserId();
                        int mrsize = mrlist.size();
                        mrlist.remove(position);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("expense");
                        ref.child(userId).child(ids).removeValue();
                        recyclerAdaptermrdata.notifyItemRemoved(position);

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ClearAll() {

        if(mrlist != null){
            mrlist.clear();
            if(recyclerAdaptermrdata != null){
                recyclerAdaptermrdata.notifyDataSetChanged();
            }

        }
        else{
            mrlist = new ArrayList<>();
        }

    }


}