package com.example.sosengasgo_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sosengasgo_android.model.ButtonActivation;

import java.util.ArrayList;
import java.util.List;

public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder> {

    private List<ButtonActivation> activations = new ArrayList<>();

    @NonNull
    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_historico, parent, false);
        return new HistoricoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoViewHolder holder, int position) {
        ButtonActivation currentActivation = activations.get(position);
        holder.textViewDate.setText("Data: " + currentActivation.getDate());
        holder.textViewTime.setText("Hora: " + currentActivation.getTime());
        holder.textViewLocation.setText("Local: " + currentActivation.getLocation());
    }

    @Override
    public int getItemCount() {
        return activations.size();
    }

    public void setActivations(List<ButtonActivation> activations) {
        this.activations = activations;
        notifyDataSetChanged();
    }

    class HistoricoViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate;
        private TextView textViewTime;
        private TextView textViewLocation;

        public HistoricoViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewTime = itemView.findViewById(R.id.text_view_time);
            textViewLocation = itemView.findViewById(R.id.text_view_location);
        }
    }
}