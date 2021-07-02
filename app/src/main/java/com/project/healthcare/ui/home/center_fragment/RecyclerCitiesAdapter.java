package com.project.healthcare.ui.home.center_fragment;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.healthcare.R;

import java.util.List;
import java.util.Observable;

public class RecyclerCitiesAdapter extends RecyclerView.Adapter<RecyclerCitiesAdapter.ViewHolder>{

    List<String> cities;
    SelectedCityNotifier notObj = new SelectedCityNotifier();
    String selectedCity ;

    public RecyclerCitiesAdapter(List<String> cities, String selectedCity) {
        this.cities = cities;
        this.selectedCity = selectedCity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cities_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerCitiesAdapter.ViewHolder holder, int position) {
        holder.city.setSelected(true);
        holder.city.setText(cities.get(position));
        setCardColor(holder, position);
        attachClickListener(holder, position);
    }

    private void setCardColor( ViewHolder holder, int position) {
        String bottomCardColor="#2784ff";
        String topCardColor="#add0fe";
        if(!selectedCity.equalsIgnoreCase(cities.get(position))){
            bottomCardColor="#36b27f";
            topCardColor = "#bee6d4";
        }
        holder.bottomCard.setCardBackgroundColor(Color.parseColor(bottomCardColor));
        holder.topCard.setCardBackgroundColor(Color.parseColor(topCardColor));
    }

    private void attachClickListener(ViewHolder holder, int position) {
        holder.bottomCard.setOnClickListener(v -> {
            selectedCity= cities.get(position);
            notObj.notifyCityChanged(selectedCity);
            holder.bottomCard.setCardBackgroundColor(Color.parseColor("#2784ff"));
            holder.topCard.setCardBackgroundColor(Color.parseColor("#add0fe"));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView city;
        CardView bottomCard,topCard;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.txtCity);
            bottomCard = itemView.findViewById(R.id.bottomCard);
            topCard = itemView.findViewById(R.id.topCard);
        }
    }

   public static class SelectedCityNotifier extends Observable{
        public void notifyCityChanged(String city){
           notifyObservers(city);
        }
    }
}
