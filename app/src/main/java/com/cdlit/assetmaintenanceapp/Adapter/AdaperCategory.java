package com.cdlit.assetmaintenanceapp.Adapter;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdlit.assetmaintenanceapp.Model.CategoryResponse;
import com.cdlit.assetmaintenanceapp.R;
import com.cdlit.assetmaintenanceapp.Ui.FragmentCategory;
import com.cdlit.assetmaintenanceapp.Utils.Global;
import com.cdlit.assetmaintenanceapp.Utils.Utilities;

import java.util.List;
import java.util.Set;

/**
 * Created by rakesh on 16-06-2017.
 */

public class AdaperCategory extends RecyclerView.Adapter<AdaperCategory.MyViewHolder> {

    private final List<CategoryResponse> listCategory;
    private final FragmentCategory context;
    private Set<String> listPrivilege;

    public AdaperCategory(FragmentCategory context, List<CategoryResponse> listCategory) {
        this.listCategory = listCategory;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final CategoryResponse categoryResponse = listCategory.get(position);

        holder.category.setText(categoryResponse.getCategoryName());

        holder.imageOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPopupMenu(holder.imageOption, categoryResponse);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, CategoryResponse categoryResponse) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context.getActivity(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_option, popup.getMenu());

        listPrivilege = Utilities.getSetFromSharedPreferances(context.getActivity(), Global.KEY_PRIVILEGE_ID, null);

        if (!listPrivilege.contains(Utilities.CATEGORY_MASTER_WRITE)) {

            popup.getMenu().getItem(0).setVisible(false);
        }

        if (!listPrivilege.contains(Utilities.CATEGORY_MASTER_DELETE)) {

            popup.getMenu().getItem(1).setVisible(false);
        }

        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(categoryResponse));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private final CategoryResponse categoryResponse;

        public MyMenuItemClickListener(CategoryResponse categoryResponse) {
            this.categoryResponse = categoryResponse;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit:

                    ((FragmentCategory) context).editClick(categoryResponse);

                    return true;

                case R.id.action_delete:

                    ((FragmentCategory) context).deleteClick(categoryResponse);

                    return true;
                default:
            }
            return false;
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView category;
        private final ImageView imageOption;

        public MyViewHolder(View view) {
            super(view);
            category = (TextView) view.findViewById(R.id.tv_category);
            imageOption = (ImageView) view.findViewById(R.id.image_option);
        }

    }


}
