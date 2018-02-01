package com.mobisolutions.ams.contacts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobisolutions.ams.MainActivity;
import com.mobisolutions.ams.R;
import com.mobisolutions.ams.maintenance.MaintenanceItem;

import java.util.ArrayList;

/**
 * Created by vkilari on 11/4/17.
 */

public class ContactsAdapter  extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private ArrayList<Contacts> mDataset;
    private final OnItemClickListener listener;
    private static Context mContext;
    private static Activity mActivity;
    boolean isEditMode;
    private int mPosition;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView flatNo;
        public View itemView;
        public ImageView phone_call;
        public ImageView editable;
        private int mPosition;
        public ViewHolder(View view) {
            super(view);
            itemView = view;
            title = (TextView) view.findViewById(R.id.person_name);
            flatNo = (TextView) view.findViewById(R.id.flat_no);
            phone_call = (ImageView) view.findViewById(R.id.phone_call);
            editable = (ImageView) view.findViewById(R.id.admin_edit);
        }

        public void bind(final Contacts item, final OnItemClickListener listener) {
            title.setText(item.getName());
            flatNo.setText(item.getFlatNo());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, v, false, getAdapterPosition());
                }
            });

            editable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, v, true, getAdapterPosition());
                }
            });

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactsAdapter(ArrayList<Contacts> myDataset, OnItemClickListener listener, Context context, Activity activity, boolean isEditMode) {
        mDataset = myDataset;
        this.listener = listener;
        mContext  = context;
        mActivity = activity;
        this.isEditMode=isEditMode;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_contact_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.bind(mDataset.get(position), listener);

        mPosition = position;
        if (isEditMode) {
            holder.editable.setVisibility(View.VISIBLE);
        } else {
            holder.editable.setVisibility(View.GONE);
        }


        // - get element from your dataset at this position
        // - replace the contents of the view with that element
       // holder.mTextView.setText(mDataset[position]);


    }

    public Contacts getItem(int index) {
        return this.mDataset.get(index);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}



