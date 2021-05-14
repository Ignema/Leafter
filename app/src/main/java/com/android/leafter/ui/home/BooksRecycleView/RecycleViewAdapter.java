package com.android.leafter.ui.home.BooksRecycleView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.leafter.R;

import java.util.ArrayList;
import java.util.Collection;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.viewModel> implements Filterable {

    private ArrayList<Books> books = new ArrayList<>();
    private ArrayList<Books> booksfull;
    private final Context context;


    private final Filter myfilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchtext=constraint.toString().toLowerCase();
            ArrayList<Books> temp=new ArrayList<>();
            if(searchtext.length() == 0){
                temp.addAll(booksfull);
            }
            else {
                for(Books item: booksfull){
                    if(item.getName().toLowerCase().contains(searchtext)
                    || item.getWriterName().toLowerCase().contains(searchtext))temp.add(item);
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=temp;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            books.clear();
            books.addAll((Collection<? extends Books>) results.values);
            notifyDataSetChanged();

        }
    };



    public RecycleViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public viewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_item, parent, false);

        viewModel viewModel = new viewModel(view);
        return viewModel;
    }


    @Override
    public void onBindViewHolder(@NonNull viewModel holder, int position) {

        holder.bookName.setText(books.get(position).getName());
        holder.bookAuthor.setText(books.get(position).getWriterName());
        holder.parent.setOnClickListener(v ->
                Toast.makeText
                        (context, "khtariti " + books.get(position).getName(), Toast.LENGTH_LONG).show());
    }


    @Override
    public int getItemCount() {
        return books.size();
    }


    public void setBooks(ArrayList<Books> books) {
        this.books = books;
        this.booksfull=new ArrayList<>(books);
        notifyDataSetChanged();
    }


    @Override
    public Filter getFilter() {
        return myfilter;
    }


    public class viewModel extends RecyclerView.ViewHolder {

        private TextView bookName, bookAuthor;
        private ImageView bookImage;
        private CardView parent;

        public viewModel(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookName);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookImage = itemView.findViewById(R.id.bookImage);
            parent = itemView.findViewById(R.id.parent);
        }

    }
}
