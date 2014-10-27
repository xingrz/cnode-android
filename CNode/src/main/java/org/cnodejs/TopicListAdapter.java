package org.cnodejs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cnodejs.api.model.Topic;
import org.cnodejs.api.model.TopicList;
import org.cnodejs.util.ImageLoader;
import org.cnodejs.widget.ArrayRecyclerAdapter;

import java.util.List;

public class TopicListAdapter extends ArrayRecyclerAdapter<Topic, TopicListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public TextView title;
        public TextView user;

        public ViewHolder(View itemView) {
            super(itemView);
            this.avatar = (ImageView) itemView.findViewById(R.id.avatar);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.user = (TextView) itemView.findViewById(R.id.user);
        }
    }

    public static interface OnItemClickListener {
        public void onItemClick(Topic item);
    }

    private final LayoutInflater inflater;
    private final OnItemClickListener listener;

    public TopicListAdapter(Context context, OnItemClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.topic_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Topic item = get(position);

        holder.title.setText(item.title);
        holder.user.setText(item.author.loginname);

        ImageLoader.load(holder.avatar, item.author.avatarUrl);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.avatar.setImageBitmap(null);
    }

}