package com.cdlit.assetmaintenanceapp.Utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.cdlit.assetmaintenanceapp.Adapter.AdaperAssignedEquipmentType;
import com.cdlit.assetmaintenanceapp.Adapter.AdaperEquipmentChecklist;
import com.cdlit.assetmaintenanceapp.Adapter.AdaperEquipmentType;
import com.cdlit.assetmaintenanceapp.Adapter.AdaperLocation;
import com.cdlit.assetmaintenanceapp.Adapter.AdaperRepairLog;
import com.cdlit.assetmaintenanceapp.Adapter.AdaperUsers;
import com.cdlit.assetmaintenanceapp.Adapter.AdapterEquipment;

/**
 * Created by ravi on 29/09/17.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;
    private boolean mSwipable;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        if (viewHolder != null) {

            View foregroundView = null;

            if (viewHolder instanceof AdaperLocation.MyViewHolder) {
                foregroundView = ((AdaperLocation.MyViewHolder) viewHolder).viewForeground;
            } else if (viewHolder instanceof AdaperUsers.MyViewHolder) {
                foregroundView = ((AdaperUsers.MyViewHolder) viewHolder).viewForeground;
            } else if (viewHolder instanceof AdapterEquipment.MyViewHolder) {
                foregroundView = ((AdapterEquipment.MyViewHolder) viewHolder).viewForeground;
            } else if (viewHolder instanceof AdaperRepairLog.MyViewHolder) {
                foregroundView = ((AdaperRepairLog.MyViewHolder) viewHolder).viewForeground;
            } else if (viewHolder instanceof AdaperEquipmentType.MyViewHolder) {
                foregroundView = ((AdaperEquipmentType.MyViewHolder) viewHolder).viewForeground;
            } else if (viewHolder instanceof AdaperEquipmentChecklist.MyViewHolder) {
                foregroundView = ((AdaperEquipmentChecklist.MyViewHolder) viewHolder).viewForeground;
            } else if (viewHolder instanceof AdaperAssignedEquipmentType.MyViewHolder) {
                foregroundView = ((AdaperAssignedEquipmentType.MyViewHolder) viewHolder).viewForeground;
            }

            getDefaultUIUtil().onSelected(foregroundView);
        }

    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {

        View foregroundView = null;

        if (viewHolder instanceof AdaperLocation.MyViewHolder) {
            foregroundView = ((AdaperLocation.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperUsers.MyViewHolder) {
            foregroundView = ((AdaperUsers.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdapterEquipment.MyViewHolder) {
            foregroundView = ((AdapterEquipment.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperRepairLog.MyViewHolder) {
            foregroundView = ((AdaperRepairLog.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperEquipmentType.MyViewHolder) {
            foregroundView = ((AdaperEquipmentType.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperEquipmentChecklist.MyViewHolder) {
            foregroundView = ((AdaperEquipmentChecklist.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperAssignedEquipmentType.MyViewHolder) {
            foregroundView = ((AdaperAssignedEquipmentType.MyViewHolder) viewHolder).viewForeground;
        }
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        View foregroundView = null;

        if (viewHolder instanceof AdaperLocation.MyViewHolder) {
            foregroundView = ((AdaperLocation.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperUsers.MyViewHolder) {
            foregroundView = ((AdaperUsers.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdapterEquipment.MyViewHolder) {
            foregroundView = ((AdapterEquipment.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperRepairLog.MyViewHolder) {
            foregroundView = ((AdaperRepairLog.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperEquipmentType.MyViewHolder) {
            foregroundView = ((AdaperEquipmentType.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperEquipmentChecklist.MyViewHolder) {
            foregroundView = ((AdaperEquipmentChecklist.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperAssignedEquipmentType.MyViewHolder) {
            foregroundView = ((AdaperAssignedEquipmentType.MyViewHolder) viewHolder).viewForeground;
        }
        getDefaultUIUtil().clearView(foregroundView);

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {

        View foregroundView = null;

        if (viewHolder instanceof AdaperLocation.MyViewHolder) {
            foregroundView = ((AdaperLocation.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperUsers.MyViewHolder) {
            foregroundView = ((AdaperUsers.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdapterEquipment.MyViewHolder) {
            foregroundView = ((AdapterEquipment.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperRepairLog.MyViewHolder) {
            foregroundView = ((AdaperRepairLog.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperEquipmentType.MyViewHolder) {
            foregroundView = ((AdaperEquipmentType.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperEquipmentChecklist.MyViewHolder) {
            foregroundView = ((AdaperEquipmentChecklist.MyViewHolder) viewHolder).viewForeground;
        } else if (viewHolder instanceof AdaperAssignedEquipmentType.MyViewHolder) {
            foregroundView = ((AdaperAssignedEquipmentType.MyViewHolder) viewHolder).viewForeground;
        }

        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return mSwipable;
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public void setSwipeEnable(boolean mSwipable) {
        this.mSwipable = mSwipable;
    }

  /*  public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }*/

}