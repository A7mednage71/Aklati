package com.example.aklati.presentation.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aklati.R;
import com.example.aklati.data.models.Area;

import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {
    private final List<Area> areas;
    private final OnAreaClickListener listener;

    public AreaAdapter(List<Area> areas, OnAreaClickListener listener) {
        this.areas = areas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        Area area = areas.get(position);
        holder.tvAreaName.setText(area.getStrArea());

        // Load country flag using Glide
        String flagUrl = area.getCountryFlag();
        if (!flagUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(flagUrl)
                    .placeholder(R.drawable.ic_location)
                    .error(R.drawable.ic_location)
                    .centerCrop()
                    .into(holder.ivAreaIcon);
        } else {
            // If no flag available, show location icon
            holder.ivAreaIcon.setImageResource(R.drawable.ic_location);
        }

        holder.itemView.setOnClickListener(v -> listener.onAreaClick(area));
    }

    @Override
    public int getItemCount() {
        return areas.size();
    }

    public interface OnAreaClickListener {
        void onAreaClick(Area area);
    }

    static class AreaViewHolder extends RecyclerView.ViewHolder {
        TextView tvAreaName;
        ImageView ivAreaIcon;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAreaName = itemView.findViewById(R.id.tvAreaName);
            ivAreaIcon = itemView.findViewById(R.id.ivAreaIcon);
        }
    }
}

