package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.FoodData;
import com.example.myapplication.R;

import java.util.List;

public class FoodDataAdapter extends RecyclerView.Adapter<FoodDataAdapter.ViewHolder> {
    private Context context;
    private List<FoodData> dataList;
    private OnItemClickListener onItemClickListener; // 클릭 이벤트 리스너

    public FoodDataAdapter(Context context, List<FoodData> dataList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.dataList = dataList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodData foodData = dataList.get(position);
        String Type=foodData.getSelectedType();
        if (Type.equals("음료")) {
            holder.foodTextView.setText("음료: " + foodData.getDrink());
        } else {
            holder.foodTextView.setText("음식: " + foodData.getFood());
        }
        holder.dateTextView.setText("날짜: " + foodData.getSelectedDate());

        Glide.with(context)
                .load(foodData.getImagePath())
                .into(holder.imageView);
        // 항목을 클릭했을 때의 동작 설정
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(foodData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView foodTextView;
        TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            foodTextView = itemView.findViewById(R.id.foodTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }

    // 항목 클릭 이벤트 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(FoodData foodData);
    }
}
