package com.android.leafter.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.leafter.R;
import com.android.leafter.activities.Book_List_Activity;
import com.android.leafter.persistence.database.DatabaseAPI;

import java.util.List;

public class CatalogueAdapter extends  RecyclerView.Adapter<CatalogueAdapter.CatalogueViewHolder> {
    private final List<Integer> mCtlgIds;
    private final DatabaseAPI mDB;
    private final Context mContext;

    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mOnLongClickListener;

    static class CatalogueViewHolder extends RecyclerView.ViewHolder {

        final ViewGroup mCatalogueEntry;
        final TextView mTitleView;
        final TextView mLinkView;

        CatalogueViewHolder(ViewGroup listEntry) {
            super(listEntry);
            mCatalogueEntry = listEntry;
            mTitleView = listEntry.findViewById(R.id.CtlgName);
            mLinkView = listEntry.findViewById(R.id.CtlgURL);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CatalogueAdapter(Context context, DatabaseAPI db, List<Integer> ctlgIds) {
        mContext = context;
        mCtlgIds = ctlgIds;
        mDB = db;
        setHasStableIds(true);
    }


    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;

    }

    public void notifyItemIdRemoved(long id) {
        int pos = mCtlgIds.indexOf((int)id);
        if (pos>=0) {
            mCtlgIds.remove(pos);
            notifyItemRemoved(pos);
        }

    }

    public void notifyItemIdChanged(long id) {
        int pos = mCtlgIds.indexOf((int)id);
        if (pos>=0) {
            notifyItemChanged(pos);
        }

    }

    public void setCatalogues(List<Integer> ctlgIds) {
        int size = mCtlgIds.size();
        mCtlgIds.clear();
        notifyItemRangeRemoved(0, size);
        mCtlgIds.addAll(ctlgIds);
        notifyItemRangeInserted(0, mCtlgIds.size());

    }

    @Override
    public long getItemId(int position) {
        return mCtlgIds.get(position);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CatalogueAdapter.CatalogueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup listEntry = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.catalogue_tile, parent, false);

        return new CatalogueViewHolder(listEntry);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CatalogueViewHolder holder, int position) {
        int ctlgId = mCtlgIds.get(position);
        Log.d("CATALOGUE", String.valueOf(ctlgId));
        DatabaseAPI.CatalogueRecord catalogue = mDB.getCatalogueRecord(ctlgId);

        if (catalogue != null) {
            holder.mTitleView.setText(Book_List_Activity.maxlen(catalogue.name, 120));
            holder.mLinkView.setText(Book_List_Activity.maxlen(catalogue.url, 50));

            holder.mCatalogueEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnClickListener!=null) {
                        mOnClickListener.onClick(view);
                    }
                }
            });

        } else {
            holder.mTitleView.setText("Error with " + ctlgId);
            holder.mLinkView.setText("Error");
        }

        holder.mCatalogueEntry.setTag(ctlgId);
        holder.mCatalogueEntry.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mOnLongClickListener!=null) {
                    return mOnLongClickListener.onLongClick(view);
                }
                return false;
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCtlgIds.size();
    }
}


