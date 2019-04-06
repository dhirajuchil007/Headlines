package com.example.headlines;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.headlines.Model.Article;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.headlines.ArticleRepo.TAG;

public class HeadlineAdapter extends RecyclerView.Adapter<HeadlineAdapter.MyViewHolder> {
    private OnItemClicked onClick;

    public interface OnItemClicked {
        void onItemClick(int position,String url);
    }


    List<Article> articles=new ArrayList<>();

    public  HeadlineAdapter(){
        super();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.headlines_list_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Article currentArticle=articles.get(position);
        holder.title.setText(currentArticle.getTitle());
        holder.description.setText(currentArticle.getDescription());
        holder.source.setText(currentArticle.getSource().getName());
        String published=getPublishedTime(currentArticle.getPublishedAt());
        holder.timeElapsed.setText(published);
        Picasso.get()
                .load(currentArticle.getUrlToImage())
                .resize(80, 80)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .into(holder.thumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position,currentArticle.getUrl());
            }
        });



    }

    public void setData(List<Article> articles){
        this.articles=articles;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;
        TextView source;
        TextView timeElapsed;
        ImageView thumbnail;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title_tv);
            description=itemView.findViewById(R.id.list_item_description);
            source=itemView.findViewById(R.id.list_item_source);
            timeElapsed=itemView.findViewById(R.id.list_item_time);
            thumbnail=itemView.findViewById(R.id.list_item_thumbnail);

        }
    }
    //Returns a string showing how long ago the article was published
    String getPublishedTime(String publishedAt){

        long seconds=0;

        try {
            //Converting date to required format
            publishedAt=publishedAt.substring(0,publishedAt.indexOf('Z'));
            publishedAt=publishedAt.substring(0,publishedAt.indexOf('T'))+" "+publishedAt.substring(publishedAt.indexOf('T')+1);

            Date publishedDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(publishedAt);
            Log.d(TAG, "getPublishedTime: "+publishedDate);
            Date currentTime=new Date();

            //getting the time elapsed since the article was published
            seconds=(currentTime.getTime()-publishedDate.getTime())/1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long hours=seconds/3600;
        long days=seconds/(3600*24);
        long minutes=seconds/60;

        if(days>=1)
            return days+" days ago";
        else if(hours>=1)
            return hours+" hours ago";
        else if(minutes>=1)
            return minutes+" minutes ago";
        else
            return seconds+ " seconds ago";


    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}
