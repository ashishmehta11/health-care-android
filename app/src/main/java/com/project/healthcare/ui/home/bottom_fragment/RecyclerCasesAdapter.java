package com.project.healthcare.ui.home.bottom_fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.healthcare.R;
import com.project.healthcare.data.HomeCovidNewsFlash;
import com.project.healthcare.databinding.RecyclerCasesItemBinding;

import java.util.ArrayList;

public class RecyclerCasesAdapter extends RecyclerView.Adapter<RecyclerCasesAdapter.ViewHolder> {
    ArrayList<HomeCovidNewsFlash> list;

    public RecyclerCasesAdapter(ArrayList<HomeCovidNewsFlash> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerCasesItemBinding b = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext())
                , R.layout.recycler_cases_item
                , parent, false);
        return new ViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        //return list.size();
        return 30;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerCasesItemBinding binding;

        public ViewHolder(@NonNull RecyclerCasesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
