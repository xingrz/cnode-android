package org.cnodejs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cnodejs.api.model.Topic;
import org.cnodejs.util.ImageLoader;
import org.cnodejs.widget.ArrayAdapter;

public class TopicListAdapter extends ArrayAdapter<Topic, TopicListAdapter.ViewHolder> {

    public static class ViewHolder extends ArrayAdapter.ViewHolder {
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

    public TopicListAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(R.layout.topic_item, parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Topic item = getItem(position);

        holder.title.setText(item.title);
        holder.user.setText(item.author.loginname);

        ImageLoader.load(holder.avatar, item.author.avatarUrl);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.avatar.setImageBitmap(null);
    }

}
