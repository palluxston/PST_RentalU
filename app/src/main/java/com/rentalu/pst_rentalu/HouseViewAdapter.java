package com.rentalu.pst_rentalu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HouseViewAdapter extends RecyclerView.Adapter<HouseViewAdapter.ViewHolder>{

    private Context context;

    public HouseViewAdapter(Context context, SelectListener listener) {
        this.context = context;
        this.selectListener = listener;
    }

    //arraylist to get each house's information
    private ArrayList<PropertyModel> houses = new ArrayList<>();
    private SelectListener selectListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /** @noinspection MalformedFormatString*/
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //getting the information into the holder
        try{
            holder.list_num.setText(String.format("Ref List Num - %s", String.valueOf(houses.get(position).getRef_list_num())));
            holder.property_type.setText(String.format("Property_Type - " + String.valueOf(houses.get(position).getProp_type())));
            holder.bedroom.setText(String.format("Bedroom - " + String.valueOf(houses.get(position).getBedroom())));
            holder.date_time.setText("Date Time - " + String.format(String.valueOf(houses.get(position).getDate_time())));
            holder.rent_price.setText("Monthly Rent Price - " + String.format(String.valueOf(houses.get(position).getRent_price())) + " K");
            holder.furniture.setText(String.format("Furniture Status - " + String.format(String.valueOf(houses.get(position).getFurniture()))));
            holder.remark.setText(String.format("Remark - " + String.valueOf(houses.get(position).getRemark())));
            holder.reporter_name.setText(String.format("Reporter Name - " + String.valueOf(houses.get(position).getReporter_name())));
            Glide.with(context)
                    .asBitmap()
                    .load(houses.get(position).getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.house_img);

            holder.propertyCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectListener.onItemClicked(houses.get(position));
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return houses.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setHouses(ArrayList<PropertyModel> houses){
        this.houses = houses;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        
        TextView list_num, property_type, bedroom, date_time, rent_price, furniture, remark, reporter_name;
        ImageView house_img;
        CardView propertyCardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            house_img = (ImageView) itemView.findViewById(R.id.house_img);
            list_num = (TextView) itemView.findViewById(R.id.list_num);
            property_type = itemView.findViewById(R.id.property_type);
            bedroom = (TextView) itemView.findViewById(R.id.bedrooms);
            date_time = (TextView) itemView.findViewById(R.id.date_Time);
            rent_price = (TextView) itemView.findViewById(R.id.rent_Price);
            furniture = (TextView) itemView.findViewById(R.id.Furniture);
            remark = (TextView) itemView.findViewById(R.id.Remark);
            reporter_name = (TextView) itemView.findViewById(R.id.reporter_Name);
            propertyCardView = (CardView) itemView.findViewById(R.id.propertyCardView);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public interface SelectListener{
        void onItemClicked(PropertyModel propertyModel);
    }
}
