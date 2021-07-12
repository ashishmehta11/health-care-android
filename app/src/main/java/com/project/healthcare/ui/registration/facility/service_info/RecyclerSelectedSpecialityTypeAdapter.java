package com.project.healthcare.ui.registration.facility.service_info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.healthcare.R;
import com.project.healthcare.data.SpecialityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


public class RecyclerSelectedSpecialityTypeAdapter extends RecyclerView.Adapter<RecyclerSelectedSpecialityTypeAdapter.ViewHolder> {
    private final ArrayList<SpecialityType> list;

    private final SpecialityRemovedNotifier notifier;

    public RecyclerSelectedSpecialityTypeAdapter(List<SpecialityType> list, SpecialityRemovedNotifier notifier) {
        this.notifier = notifier;
        this.list = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_reg_selected_facility_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpecialityType ft = list.get(position);
        holder.txt.setText(SpecialityType.toString(list.get(position)));
        holder.remove.setOnClickListener(v -> {
            notifier.notifyRemoval(ft);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<SpecialityType> getList() {
        return list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton remove;
        TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            remove = itemView.findViewById(R.id.btnRemove);
            txt = itemView.findViewById(R.id.txtSelectedType);

        }
    }

    static class SpecialityRemovedNotifier extends Observable {
        public void notifyRemoval(SpecialityType t) {
            setChanged();
            notifyObservers(t);
        }
    }
}
