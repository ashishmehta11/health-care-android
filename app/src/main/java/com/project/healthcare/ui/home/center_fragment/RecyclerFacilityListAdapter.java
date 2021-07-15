package com.project.healthcare.ui.home.center_fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.healthcare.R;
import com.project.healthcare.data.FacilityListItem;
import com.project.healthcare.data.HealthFacility;
import com.project.healthcare.databinding.RecyclerFacilityListItemBinding;

import java.util.ArrayList;

public class RecyclerFacilityListAdapter extends RecyclerView.Adapter<RecyclerFacilityListAdapter.ViewHolder> {

    private final ArrayList<HealthFacility> list;

    public RecyclerFacilityListAdapter(ArrayList<HealthFacility> list) {
        this.list = list;
    }

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
        String[] emails, phones;
        emails = new String[list.get(position).getEmails().size()];
        phones = new String[list.get(position).getPhoneNumbers().size()];
        emails[0] = list.get(position).getEmails().get(0);
        phones[0] = list.get(position).getPhoneNumbers().get(0);
        FacilityListItem item = new FacilityListItem(emails, phones, list.get(position).getAddress(), list.get(position).getName());
//        holder.binding.setData(new FacilityListItem(new String[]{"this.is.an.email@mailman.com"}
//                , new String[]{"883 332 1223"}
//                , "Besides famous temple, behind this hall, on this street, this relative area, this absolute area"));
        holder.binding.setData(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
//        return 10;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerFacilityListItemBinding binding;

        public ViewHolder(@NonNull RecyclerFacilityListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
