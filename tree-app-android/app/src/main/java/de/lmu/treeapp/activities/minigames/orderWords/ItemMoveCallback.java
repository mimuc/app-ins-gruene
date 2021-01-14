package de.lmu.treeapp.activities.minigames.orderWords;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;

    public ItemMoveCallback(ItemTouchHelperContract adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP
                | ItemTouchHelper.DOWN
                | ItemTouchHelper.RIGHT
                | ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof RecyclerAdapter.MyViewHolder) {
                RecyclerAdapter.MyViewHolder myViewHolder =
                        (RecyclerAdapter.MyViewHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }

        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof RecyclerAdapter.MyViewHolder) {

            RecyclerAdapter.MyViewHolder myViewHolder =
                    (RecyclerAdapter.MyViewHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }


    public interface ItemTouchHelperContract {

        void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position);

        void onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(RecyclerAdapter.MyViewHolder myViewHolder);

        void onRowClear(RecyclerAdapter.MyViewHolder myViewHolder);

    }
}
