package com.example.myhealthmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<StressData> stressDataList;


    public RecyclerAdapter(Context mContext, ArrayList<StressData> stressDataList) {
        this.mContext = mContext;
        this.stressDataList = stressDataList;
    }



    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stressdata_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.d.setText(stressDataList.get(position).getS_date().toString());
        holder.n.setText(stressDataList.get(position).getS_nervousness().toString());
        holder.c.setText(stressDataList.get(position).getS_control().toString());
        holder.r.setText(stressDataList.get(position).getS_relaxation().toString());
        holder.res.setText(stressDataList.get(position).getS_restlessness().toString());
        holder.w.setText(stressDataList.get(position).getS_worry().toString());
        holder.i.setText(stressDataList.get(position).getS_irritability().toString());
        holder.f.setText(stressDataList.get(position).getS_fear().toString());
        holder.result.setText(stressDataList.get(position).getS_result().toString());

    }

    @Override
    public int getItemCount() {
        return stressDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView d, c, n, res, r, w, i, f, result;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            d = (TextView) itemView.findViewById(R.id.date_db);
            n = (TextView) itemView.findViewById(R.id.nervousness_db);
            c = (TextView) itemView.findViewById(R.id.control_db);
            r = (TextView) itemView.findViewById(R.id.relaxation_db);
            res = (TextView) itemView.findViewById(R.id.restlessness_db);
            w = (TextView) itemView.findViewById(R.id.worry_db);
            i = (TextView) itemView.findViewById(R.id.irritability_db);
            f = (TextView) itemView.findViewById(R.id.fear_db);
            result = (TextView) itemView.findViewById(R.id.result_db);
        }
    }


}
