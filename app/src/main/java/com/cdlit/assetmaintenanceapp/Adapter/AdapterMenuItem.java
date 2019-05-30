package com.cdlit.assetmaintenanceapp.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.ActivityNavigationDrawerAdmin;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rakesh on 05-12-2017.
 */

public class AdapterMenuItem extends RecyclerView.Adapter<AdapterMenuItem.MyViewHolder> {

    private final Activity context;
    private final HashMap<String, Integer> mapMenu;
    private final ArrayList<String> listKey;
    private final ArrayList<Integer> listValues;

    public AdapterMenuItem(Activity context, HashMap<String, Integer> mapMenu) {

        this.mapMenu = mapMenu;
        this.context = context;

        listKey = new ArrayList<>(mapMenu.keySet());
        listValues = new ArrayList<>(mapMenu.values());

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);

        return new AdapterMenuItem.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.imageItem.
                setImageResource(listValues.get(position));

        holder.tvMenuItem.setText(listKey.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("itemView", "itemView");

                ((ActivityNavigationDrawerAdmin) context).menuClick(listKey.get(position), null, null, null, null, null);

            }
        });


      /*  holder.llMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("llMenuItem", "llMenuItem");


            }
        });*/
    }

    @Override
    public int getItemCount() {
        return listKey.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageItem;
        private final TextView tvMenuItem;
        private final LinearLayout llMenuItem;

        public MyViewHolder(View view) {
            super(view);

            imageItem = (ImageView) view.findViewById(R.id.image_item);
            tvMenuItem = (TextView) view.findViewById(R.id.tv_menu_item);
            llMenuItem = (LinearLayout) view.findViewById(R.id.ll_menu_item);


        }


    }

}
