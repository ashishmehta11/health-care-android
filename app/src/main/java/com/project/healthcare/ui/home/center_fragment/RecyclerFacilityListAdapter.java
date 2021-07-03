package com.project.healthcare.ui.home.center_fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.healthcare.R;
import com.project.healthcare.data.FacilityListItem;
import com.project.healthcare.databinding.RecyclerFacilityListItemBinding;

public class RecyclerFacilityListAdapter extends RecyclerView.Adapter<RecyclerFacilityListAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerFacilityListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext())
                , R.layout.recycler_facility_list_item
                , parent
                , false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerFacilityListAdapter.ViewHolder holder, int position) {
        holder.binding.setData(new FacilityListItem(new String[]{"this.is.an.email@mailman.com"}
                , new String[]{"883 332 1223"}
                , "Besides famous temple, behind this hall, on this street, this relative area, this absolute area"));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerFacilityListItemBinding binding;

        public ViewHolder(@NonNull RecyclerFacilityListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}