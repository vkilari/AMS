package com.mobisolutions.ams.maintenance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobisolutions.ams.R;
import com.mobisolutions.ams.contacts.Contacts;

import java.util.List;

/**
 * Created by vkilari on 12/10/17.
 */

public class IndividualMaintenanceAdapter extends RecyclerView.Adapter<IndividualMaintenanceAdapter.MyViewHolder> {
    private Context context;
    private List<Contacts> cartList;
    private GetGeneralMaintenanceCount getGeneralMaintenanceCount;
    private String amount;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView flat_no, name, amount;
        // public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;
        public EditText textValue;


        public MyViewHolder(View view) {
            super(view);
            flat_no = (TextView) view.findViewById(R.id.flat_no);
            name = (TextView) view.findViewById(R.id.name);
            amount = (TextView) view.findViewById(R.id.amount);

            viewBackground =  (RelativeLayout) view.findViewById(R.id.view_background);
            viewForeground = (RelativeLayout)view.findViewById(R.id.view_foreground);
        }


    }


    public IndividualMaintenanceAdapter(Context context, List<Contacts> cartList, String individualAmount) {
        this.context = context;
        this.cartList = cartList;
        this.amount = individualAmount;
    }

    @Override
    public IndividualMaintenanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_individual_maintenance, parent, false);

        return new IndividualMaintenanceAdapter.MyViewHolder(itemView);
    }

    public Contacts getItem(int index) {
        return this.cartList.get(index);
    }


    @Override
    public void onBindViewHolder(final IndividualMaintenanceAdapter.MyViewHolder holder, final int position) {
        final Contacts item = cartList.get(position);
        holder.name.setText(item.getName());
        holder.flat_no.setText(item.getFlatNo());
        holder.amount.setText("₹ "+amount);


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }




}