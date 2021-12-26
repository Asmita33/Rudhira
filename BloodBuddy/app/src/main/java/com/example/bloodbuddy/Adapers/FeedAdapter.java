package com.example.bloodbuddy.Adapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodbuddy.R;
import com.example.bloodbuddy.modelClasses.Feed;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder>
{
    Context context;
    ArrayList<Feed> feedArrayList;

    public FeedAdapter(Context context, ArrayList<Feed> feedArrayList) {
        this.context = context;
        this.feedArrayList = feedArrayList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(context).inflate(R.layout.sample_feed_row, parent, false);

        // can do this if context is not asked in constructor
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_row, parent, false);
        return new FeedViewHolder(view);
    }

    //here: Replace the contents of a view (invoked by the layout manager) with your current data
    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        Feed feed = feedArrayList.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        // data binding
//        holder.image.setImage
        Glide.with(context).load(feed.getImage()).into(holder.image);
        holder.text.setText(feed.getText() +"\n\n\n" +"URI: " +feed.getImage());
    }

    @Override
    public int getItemCount() {
        return feedArrayList.size();
    }

    //here: our nested view holder class. It holds the view of each item/row in our RV
    public static class FeedViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView text;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);

            // binding the elements
            image = itemView.findViewById(R.id.feed_row_img);
            text = itemView.findViewById(R.id.feed_row_text);
        }
    }
}
