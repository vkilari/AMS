package com.mobisolutions.ams.maintenance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobisolutions.ams.R;

import java.util.List;

/**
 * Created by vkilari on 11/30/17.
 */

public class GeneralMaintenanceAdapter  extends RecyclerView.Adapter<GeneralMaintenanceAdapter.MyViewHolder> {
    private Context context;
    private List<MaintenanceItem> cartList;
    private static ClickListener clickListener;
    private GetGeneralMaintenanceCount getGeneralMaintenanceCount;
    private double total;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView name, description;
        // public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;
        public EditText textValue;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            description =  (TextView) view.findViewById(R.id.description);
            textValue = (EditText) view.findViewById(R.id.add_amount);

            // price = view.findViewById(R.id.price);
            //  thumbnail = view.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            viewBackground =  (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout)view.findViewById(R.id.view_foreground);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        GeneralMaintenanceAdapter.clickListener = clickListener;
    }


    public GeneralMaintenanceAdapter(Context context, List<MaintenanceItem> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_general_maintenance, parent, false);

        return new MyViewHolder(itemView);
    }

    public MaintenanceItem getItem(int index) {
        return this.cartList.get(index);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MaintenanceItem item = cartList.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDescription());


        holder.textValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (holder.textValue.getText().length() > 0)
                    total = (total + Double.parseDouble(holder.textValue.getText().toString()));

                Log.d("TAG", "-----onFocusChange-------" + total);

                getGeneralMaintenanceCount.getTotal(position, total);

            }
        });

        //  holder.price.setText("₹" + item.getPrice());

//        Glide.with(context)
//                .load(item.getThumbnail())
//                .into(holder.thumbnail);
    }

    public void setCountListener(GetGeneralMaintenanceCount getMaintenanceCount){
        this.getGeneralMaintenanceCount = getMaintenanceCount;
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        cartList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(MaintenanceItem item, int position) {
        cartList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

}