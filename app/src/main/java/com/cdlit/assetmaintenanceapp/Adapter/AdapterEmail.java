package com.cdlit.assetmaintenanceapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Dialog.DialogAddEmail;
import com.cdlit.assetmaintenanceapp.R;

import java.util.ArrayList;

public class AdapterEmail extends RecyclerView.Adapter<AdapterEmail.MyViewHolder> {

    private final DialogAddEmail context;
    public final ArrayList<String> listEmail;

    public AdapterEmail(DialogAddEmail context, ArrayList<String> listEmail) {

        this.context = context;
        this.listEmail = listEmail;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.email_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvEmailId.setText(listEmail.get(position));

        holder.deleteOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listEmail.remove(position);
                notifyDataSetChanged();

               // context.removeEmailItem(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listEmail.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView deleteOption;
        private final TextView tvEmailId;

        public MyViewHolder(View view) {
            super(view);
            tvEmailId = (TextView) view.findViewById(R.id.tv_email_id);
            deleteOption = (ImageView) view.findViewById(R.id.delete_option);
        }

    }
}
