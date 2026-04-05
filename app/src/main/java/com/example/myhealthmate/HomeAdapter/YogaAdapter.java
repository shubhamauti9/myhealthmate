package com.example.myhealthmate.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myhealthmate.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class YogaAdapter extends RecyclerView.Adapter<YogaAdapter.YogaViewHolder> {

    ArrayList<YogaHelperClass> yogaHelperLocations;

    public YogaAdapter(ArrayList<YogaHelperClass> yogaHelperLocations) {
        this.yogaHelperLocations = yogaHelperLocations;
    }

    @NonNull
    @Override
    public YogaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yoga_recycler_design,parent, false);
        YogaViewHolder yogaViewHolder = new YogaViewHolder(view);

        return yogaViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull YogaViewHolder holder, int position) {

        YogaHelperClass yogaHelperClass = yogaHelperLocations.get(position);
        holder.image.setImageResource(yogaHelperClass.getImage());
        holder.title.setText(yogaHelperClass.getTitle());
        holder.des.setText(yogaHelperClass.getDes());

    }

    @Override
    public int getItemCount() {
        return yogaHelperLocations.size();
    }


    public static class YogaViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title,des;

        public YogaViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.yoga_image);
            title = itemView.findViewById(R.id.yoga_title);
            des = itemView.findViewById(R.id.yoga_description);


        }
    }
}
