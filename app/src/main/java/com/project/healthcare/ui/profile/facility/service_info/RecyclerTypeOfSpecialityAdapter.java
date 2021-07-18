package com.project.healthcare.ui.profile.facility.service_info;

import android.util.Log;
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


public class RecyclerTypeOfSpecialityAdapter extends RecyclerView.Adapter<RecyclerTypeOfSpecialityAdapter.ViewHolder> {
    private static final String TAG = "RecyclerFacilities";
    private final ArrayList<SpecialityType> list;
    private final SpecialityAddedNotifier notifier;

    public RecyclerTypeOfSpecialityAdapter(List<SpecialityType> list, SpecialityAddedNotifier notifier) {
        this.list = new ArrayList<>(list);
        this.notifier = notifier;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_reg_type_of_facility_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt.setText(SpecialityType.toString(list.get(position)));
        holder.add.setOnClickListener(v -> {
            Log.d(TAG, "onBindViewHolder: inside add : on click : " + holder.txt.getText().toString());
            notifier.notifyTypeAdded(list.get(position));
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
        ImageButton add;
        TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.btnAdd);
            txt = itemView.findViewById(R.id.txtTypeOfFacility);

        }
    }

    static class SpecialityAddedNotifier extends Observable {
        public void notifyTypeAdded(SpecialityType t) {
            setChanged();
            notifyObservers(t);
        }
    }
}
