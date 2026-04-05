package com.example.myhealthmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class expenselist extends RecyclerView.Adapter<expenselist.ViewHolder> {


    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<ExpenseData> mrlist;
    String userId;

    OnItemClickListener listener;

    public expenselist(Context mContext, ArrayList<ExpenseData> mrlist, String userId) {
        this.mContext = mContext;
        this.mrlist = mrlist;
        this.userId = userId;

    }


    @NonNull
    public expenselist.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_expenselist, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String sdy = mrlist.get(position).getDate();
        String a = mrlist.get(position).getAmount_str();
        String detail = mrlist.get(position).getDetails_str();


        holder.dy.setText(sdy);
        holder.t.setText(detail);
        holder.m.setText(a);
        String ids = mrlist.get(position).getUserId();
    }

    public interface OnItemClickListener {
        void onItemdelete(int position);
    }




    public void setOnItemClickListener(expenselist.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mrlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dy, t, m;
        Button dl;


        public ViewHolder(@NonNull View itemView, final expenselist.OnItemClickListener listener) {

            super(itemView);

            dy = (TextView) itemView.findViewById(R.id.rday_db);
            m = (TextView) itemView.findViewById(R.id.rmedicine_db);
            t = (TextView) itemView.findViewById(R.id.rtime_db);
            dl = (Button) itemView.findViewById(R.id.del);
            dl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemdelete(position);
                        }
                    }
                }
            });


        }


    }

}



