package com.android.leafter.ui.music.musicRecycleView;

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

    private ArrayList<Music> music = new ArrayList<>();
    private ArrayList<Music> musicfull;
    private final Context context;


    private final Filter myfilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchtext=constraint.toString().toLowerCase();
            ArrayList<Music> temp=new ArrayList<>();
            if(searchtext.length() == 0){
                temp.addAll(musicfull);
            }
            else {
                for(Music item: musicfull){
                    if(item.getName().toLowerCase().contains(searchtext)
                            || item.getSinger().toLowerCase().contains(searchtext))temp.add(item);
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=temp;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            music.clear();
            music.addAll((Collection<? extends Music>) results.values);
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
                .inflate(R.layout.music_item, parent, false);

        viewModel viewModel = new viewModel(view);
        return viewModel;
    }


    @Override
    public void onBindViewHolder(@NonNull viewModel holder, int position) {

        holder.musicName.setText(music.get(position).getName());
        holder.singer.setText(music.get(position).getSinger());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "khtariti " + music.get(position).getName(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return music.size();
    }


    public void setMusic(ArrayList<Music> music) {
        this.music = music;
        this.musicfull=new ArrayList<>(music);
        notifyDataSetChanged();
        for(Music i:music) System.out.println("lala+ "+i);
    }


    @Override
    public Filter getFilter() {
        return myfilter;
    }


    public class viewModel extends RecyclerView.ViewHolder {

        private TextView musicName, singer;
        private ImageView musicImage;
        private CardView parent;

        public viewModel(@NonNull View itemView) {
            super(itemView);
            musicName = itemView.findViewById(R.id.musicName);
            singer = itemView.findViewById(R.id.singer);
            musicImage = itemView.findViewById(R.id.musicImage);
            parent = itemView.findViewById(R.id.parent);
        }

    }
}
