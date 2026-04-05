package com.example.myhealthmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapterIz extends RecyclerView.Adapter<RecyclerAdapterIz.ViewHolder> {


    private static final String Tag = "RecyclerViewIz";
    private Context mContext;
    private ArrayList<InfluenzaData> izDataList;


    public RecyclerAdapterIz(Context mContext,ArrayList<InfluenzaData> izDataListList) {
        this.mContext = mContext;
        this.izDataList = izDataListList;
    }

    @NonNull
    @Override
    public RecyclerAdapterIz.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.influenzadata_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.d.setText(izDataList.get(position).getIz_date().toString());
        holder.a.setText(izDataList.get(position).getIz_Age().toString());
        holder.c.setText(izDataList.get(position).getIz_Cough().toString());
        holder.f.setText(izDataList.get(position).getIz_Fatigue().toString());
        holder.fe.setText(izDataList.get(position).getIz_Fever().toString());
        holder.h.setText(izDataList.get(position).getIz_Headache().toString());
        holder.m.setText(izDataList.get(position).getIz_Medicalcond().toString());
        holder.my.setText(izDataList.get(position).getIz_Myalgia().toString());
        holder.rn.setText(izDataList.get(position).getIz_Runnynose().toString());
        holder.t.setText(izDataList.get(position).getIz_Throatache().toString());
        holder.v.setText(izDataList.get(position).getIz_Vomiting().toString());
        holder.result_iz.setText(izDataList.get(position).getIz_result().toString());

    }

    @Override
    public int getItemCount() {
        return izDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView d,a,c,f,fe,h,m,my,rn,t,v,result_iz;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            d = (TextView) itemView.findViewById(R.id.date_db_iz);
            a = (TextView) itemView.findViewById(R.id.age_db);
            c = (TextView) itemView.findViewById(R.id.cough_db);
            f = (TextView) itemView.findViewById(R.id.fatigue_db);
            fe = (TextView) itemView.findViewById(R.id.fever_db);
            h = (TextView) itemView.findViewById(R.id.headache_db);
            m = (TextView) itemView.findViewById(R.id.medicalcond_db);
            my = (TextView) itemView.findViewById(R.id.myalgia_db);
            rn = (TextView) itemView.findViewById(R.id.runnynose_db);
            t = (TextView) itemView.findViewById(R.id.throatache_db);
            v = (TextView) itemView.findViewById(R.id.vomiting_db);
            result_iz = (TextView) itemView.findViewById(R.id.result_db_iz);
        }
    }


}
