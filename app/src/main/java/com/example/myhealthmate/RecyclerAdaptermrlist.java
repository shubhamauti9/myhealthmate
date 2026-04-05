package com.example.myhealthmate;







        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.Query;
        import com.google.firebase.database.ValueEventListener;

        import java.text.DateFormat;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;

        import static androidx.core.content.ContextCompat.getSystemService;

public class RecyclerAdaptermrlist extends RecyclerView.Adapter<RecyclerAdaptermrlist.ViewHolder> {


    private static final String Tag = "RecyclerView";
    private Context mContext;
    private ArrayList<PatientMedicationReminder> mrlist;
    String userId;
    AlarmManager alarmManager;
    OnItemClickListener listener;


    public RecyclerAdaptermrlist(Context mContext, ArrayList<PatientMedicationReminder> mrlist, AlarmManager alarmManager, String userId) {
        this.mContext = mContext;
        this.mrlist = mrlist;
        this.alarmManager = alarmManager;
        this.userId = userId;
    }



    @NonNull

    public RecyclerAdaptermrlist.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mrlist, parent, false);
        return new ViewHolder(view, listener);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String sdy = mrlist.get(position).getDt();
        String fsdy = "";

        if(sdy.charAt(0) == '1'){
            fsdy += "Sun, ";
        }
        if(sdy.charAt(1) == '1'){
            fsdy += "Mon, ";
        }
        if(sdy.charAt(2) == '1'){
            fsdy += "Tue, ";
        }
        if(sdy.charAt(3) == '1'){
            fsdy += "Wed, ";
        }
        if(sdy.charAt(4) == '1'){
            fsdy += "Thu, ";
        }
        if(sdy.charAt(5) == '1'){
            fsdy += "Fri, ";
        }
        if(sdy.charAt(6) == '1'){
            fsdy += "Sat, ";
        }

        String fdy= fsdy.substring(0, fsdy.length() - 2);
        if(sdy.charAt(0) == '1' && sdy.charAt(1) == '1' && sdy.charAt(2) == '1' && sdy.charAt(3) == '1' && sdy.charAt(4) == '1' && sdy.charAt(5) == '1' && sdy.charAt(6) == '1' && sdy.charAt(7) == '1'){
            holder.dy.setText("Everyday");
        }
        else{
            holder.dy.setText(fdy);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        Date date = null;
        try {

            date = sdf.parse(mrlist.get(position).getCal());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        String ct = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());


        holder.t.setText(ct);
        holder.m.setText(mrlist.get(position).getMs());
        String ids = mrlist.get(position).getUserId();
        //holder.setListeners(position, ids);

    }

    public interface OnItemClickListener{
        void onItemdelete(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    @Override
    public int getItemCount() {
        return mrlist.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView dy, t, m;
        Button dl;


        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            dy = (TextView) itemView.findViewById(R.id.rday_db);
            m = (TextView) itemView.findViewById(R.id.rmedicine_db);
            t = (TextView) itemView.findViewById(R.id.rtime_db);
            dl = (Button) itemView.findViewById(R.id.del);
            dl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemdelete(position);
                        }
                    }
                }
            });


        }


    }



}
