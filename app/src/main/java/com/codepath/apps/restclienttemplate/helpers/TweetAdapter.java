package com.codepath.apps.restclienttemplate.helpers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.activities.ComposeActivity;
import com.codepath.apps.restclienttemplate.activities.DetailsActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by fmonsalve on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{



    private List<Tweet> tweets;
    AppCompatActivity context;

    //pass in the Tweets array into constructor
    public TweetAdapter(List<Tweet> tweets){
        this.tweets=tweets;
    }

    //for each row, inflate layout and cache references into ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=(AppCompatActivity) parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet,parent,false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    //bind the values based on the position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the data according to position
        Tweet tweet=tweets.get(position);
        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText("@"+tweet.user.screenName+"\n"+tweet.body);
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 5, 0))
                .into(holder.ivProfileImage);
        holder.tvRelativeTime.setText(Tweet.getRelativeTimeAgo(tweet.createdAt));

        final String username=tweet.user.screenName;
        final long uid=tweet.uid;

        holder.reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ComposeActivity.class);
                i.putExtra("reply_to","@"+username+" ");
                i.putExtra("uid",uid);
                context.startActivityForResult(i,20);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    //create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvRelativeTime;
        public ImageView reply;

        public ViewHolder(View itemView){
            super(itemView);

            //perform the findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvRelativeTime = (TextView) itemView.findViewById(R.id.tvRelativeTime);
            reply = (ImageView) itemView.findViewById(R.id.reply_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Tweet tweet = tweets.get(pos);
                    //Toast.makeText(context,String.format("Success on position %s",pos),Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context, DetailsActivity.class);
                    i.putExtra("position",pos);
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivityForResult(i,21);
                }
            });
        }
    }
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(JSONArray json) {
        for (int i=0;i<json.length();i++){
            try {
                tweets.add(Tweet.fromJSON(json.getJSONObject(i)));
                notifyItemInserted(tweets.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
