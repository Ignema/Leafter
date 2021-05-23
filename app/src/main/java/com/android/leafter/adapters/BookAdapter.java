package com.android.leafter.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.leafter.R;
import com.android.leafter.activities.Book_List_Activity;
import com.android.leafter.persistence.database.DatabaseAPI;

import java.util.Date;
import java.util.List;

public class BookAdapter extends  RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private final List<Integer> mBookIds;
    private final DatabaseAPI mDB;
    private final Context mContext;

    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mOnLongClickListener;

    static class BookViewHolder extends RecyclerView.ViewHolder {

        final ViewGroup mBookEntry;
        final TextView mTitleView;
        final TextView mAuthorView;
        final TextView mStatusView;
        BookViewHolder(ViewGroup listEntry) {
            super(listEntry);
            mBookEntry = listEntry;
            mTitleView = listEntry.findViewById(R.id.BookTitle);
            mAuthorView = listEntry.findViewById(R.id.BookAuthor);
            mStatusView = listEntry.findViewById(R.id.BookStatus);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BookAdapter(Context context, DatabaseAPI db, List<Integer> bookIds) {
        mContext = context;
        mBookIds = bookIds;
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
        int pos = mBookIds.indexOf((int)id);
        if (pos>=0) {
            mBookIds.remove(pos);
            notifyItemRemoved(pos);
        }

    }

    public void notifyItemIdChanged(long id) {
        int pos = mBookIds.indexOf((int)id);
        if (pos>=0) {
            notifyItemChanged(pos);
        }

    }

    public void setBooks(List<Integer> bookIds) {
        int size = mBookIds.size();
        mBookIds.clear();
        notifyItemRangeRemoved(0, size);
        mBookIds.addAll(bookIds);
        notifyItemRangeInserted(0, mBookIds.size());

    }

    @Override
    public long getItemId(int position) {
        return mBookIds.get(position);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public BookAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewGroup listEntry = (ViewGroup)LayoutInflater.from(parent.getContext()).inflate(R.layout.book_tile, parent, false);

        return new BookViewHolder(listEntry);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        int bookid = mBookIds.get(position);
        DatabaseAPI.BookRecord book = mDB.getBookRecord(bookid);

        if (book != null && book.filename != null) {
            holder.mTitleView.setText(Book_List_Activity.maxlen(book.title, 120));
            holder.mAuthorView.setText(Book_List_Activity.maxlen(book.author, 50));

            long lastread = book.lastread;
            long time = lastread;

            int text;
            if (book.status==DatabaseAPI.STATUS_DONE) {
                text = R.string.book_status_completed;
            } else if (book.status==DatabaseAPI.STATUS_LATER) {
                time = 0;
                text = R.string.book_status_later;
            } else if (lastread>0 && book.status==DatabaseAPI.STATUS_STARTED) {
                text = R.string.book_viewed_on;
            } else {
                time = book.added;
                text = R.string.book_added_on;
            }

            CharSequence rtime = android.text.format.DateUtils.getRelativeTimeSpanString(time);

            holder.mStatusView.setTextSize(12);

            if (text==R.string.book_viewed_on) {
                holder.mStatusView.setTextSize(14);
            }
            holder.mStatusView.setText(mContext.getString(text, rtime));


            holder.mBookEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnClickListener!=null) {
                        mOnClickListener.onClick(view);
                    }
                }
            });

        } else {
            holder.mTitleView.setText("Error with " + bookid);
            holder.mAuthorView.setText("Error");
            holder.mStatusView.setText("");
        }

        holder.mBookEntry.setTag(bookid);
        holder.mBookEntry.setOnLongClickListener(new View.OnLongClickListener() {
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
        return mBookIds.size();
    }
}

