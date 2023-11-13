package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FoodAnalyzeAdapter extends RecyclerView.Adapter<FoodAnalyzeAdapter.ViewHolder> {
    private Context context;
    private List<FoodData> dataList;

    public FoodAnalyzeAdapter(Context context, List<FoodData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public FoodAnalyzeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.analyze_item, parent, false);
        return new FoodAnalyzeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAnalyzeAdapter.ViewHolder holder, int position) {
        FoodData foodData = dataList.get(position);
        String Type=foodData.getSelectedType();
        if (Type.equals("음료")) {
            holder.AfoodTextView.setText("음료: " + foodData.getDrink());
        } else {
            holder.AfoodTextView.setText("음식: " + foodData.getFood());
        }
        holder.AdateTextView.setText("날짜: " + foodData.getSelectedDate());
        holder.AplaceTextView.setText("장소: "+foodData.getSelectedPlace());
        holder.AcalTextView.setText("칼로리: "+foodData.getCalories());
        holder.AcostTextView.setText("가격: "+foodData.getCost());

        Glide.with(context)
                .load(foodData.getImagePath())
                .into(holder.AimageView);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView AimageView;
        TextView AfoodTextView;
        TextView AdateTextView;
        TextView AcostTextView;
        TextView AcalTextView;
        TextView AplaceTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            AimageView = itemView.findViewById(R.id.AimageView);
            AfoodTextView = itemView.findViewById(R.id.AfoodTextView);
            AdateTextView = itemView.findViewById(R.id.AdateTextView);
            AcostTextView=itemView.findViewById(R.id.AcostTextView);
            AcalTextView=itemView.findViewById(R.id.AcalTextView);
            AplaceTextView=itemView.findViewById(R.id.AplaceTextView);
        }
    }

}
