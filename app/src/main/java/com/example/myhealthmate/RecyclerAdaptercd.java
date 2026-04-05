package com.example.myhealthmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdaptercd extends RecyclerView.Adapter<RecyclerAdaptercd.ViewHolder> {


    private static final String Tag = "RecyclerViewIz";
    private Context mContext;
    private ArrayList<CovidData> cdDataList;


    public RecyclerAdaptercd(Context mContext,ArrayList<CovidData> cdDataListList) {
        this.mContext = mContext;
        this.cdDataList = cdDataListList;
    }

    @NonNull
    @Override
    public RecyclerAdaptercd.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coviddata_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.d.setText(cdDataList.get(position).getCd_date().toString());
        holder.f.setText(cdDataList.get(position).getCd_Fever().toString());
        holder.dc.setText(cdDataList.get(position).getCd_Drycough().toString());
        holder.t.setText(cdDataList.get(position).getCd_Sorethroat().toString());
        holder.rn.setText(cdDataList.get(position).getCd_Runnynose().toString());
        holder.h.setText(cdDataList.get(position).getCd_Headache().toString());
        holder.fa.setText(cdDataList.get(position).getCd_Fatigue().toString());
        holder.b.setText(cdDataList.get(position).getCd_Breathingproblem().toString());
        holder.cld.setText(cdDataList.get(position).getCd_Chroniclungdisease().toString());
        holder.ahd.setText(cdDataList.get(position).getCd_Heartdisease());
        holder.db.setText(cdDataList.get(position).getCd_Diabetes().toString());
        holder.hp.setText(cdDataList.get(position).getCd_Hypertension().toString());
        holder.s.setText(cdDataList.get(position).getCd_Stomachache().toString());
        holder.a.setText(cdDataList.get(position).getCd_Abroad().toString());
        holder.ccp.setText(cdDataList.get(position).getCd_Contact().toString());
        holder.alg.setText(cdDataList.get(position).getCd_largegathering().toString());
        holder.vpp.setText(cdDataList.get(position).getCd_publicplacevisit().toString());
        holder.fwpp.setText(cdDataList.get(position).getCd_familyworking().toString());
        holder.result_cd.setText(cdDataList.get(position).getCd_result().toString());
    }

    @Override
    public int getItemCount() {
        return cdDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView d,f,dc,t,rn,h,fa,b,cld,ahd,db,hp,s,a,ccp,alg,vpp,fwpp,result_cd;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            d = (TextView) itemView.findViewById(R.id.date_db_cd);
            f = (TextView) itemView.findViewById(R.id.fever_db);
            dc = (TextView) itemView.findViewById(R.id.drycough_db);
            t = (TextView) itemView.findViewById(R.id.sorethroat_db);
            rn = (TextView) itemView.findViewById(R.id.runnynose_db);
            h = (TextView) itemView.findViewById(R.id.headache_db);
            fa = (TextView) itemView.findViewById(R.id.fatigue_db);
            b = (TextView) itemView.findViewById(R.id.breathingproblem_db);
            cld = (TextView) itemView.findViewById(R.id.chroniclung_db);
            ahd = (TextView) itemView.findViewById(R.id.heartdisease_db);
            db = (TextView) itemView.findViewById(R.id.diabetes_db);
            hp = (TextView) itemView.findViewById(R.id.hypertension_db);
            s = (TextView) itemView.findViewById(R.id.stomachache_db);
            a = (TextView) itemView.findViewById(R.id.abroad_db);
            ccp = (TextView) itemView.findViewById(R.id.contactcdp_db);
            alg = (TextView) itemView.findViewById(R.id.attend_db);
            vpp = (TextView) itemView.findViewById(R.id.visit_db);
            fwpp = (TextView) itemView.findViewById(R.id.family_db);


            result_cd = (TextView) itemView.findViewById(R.id.result_db_cd);
        }
    }


}
