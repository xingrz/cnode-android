package org.cnodejs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cnodejs.api.model.Reply;
import org.cnodejs.util.ImageLoader;
import org.cnodejs.util.Markdown;
import org.cnodejs.widget.ArrayAdapter;

public class TopicRepliesAdapter extends ArrayAdapter<Reply, TopicRepliesAdapter.ViewHolder> {

    public static class ViewHolder extends ArrayAdapter.ViewHolder {
        public ImageView avatar;
        public TextView user;
        public TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            this.avatar = (ImageView) itemView.findViewById(R.id.avatar);
            this.user = (TextView) itemView.findViewById(R.id.user);
            this.content = (TextView) itemView.findViewById(R.id.content);
        }
    }

    public TopicRepliesAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflate(R.layout.reply_item, parent));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Reply item = getItem(position);

        holder.user.setText(item.author.loginname);

        Markdown.render(holder.content, item.content);
        ImageLoader.load(holder.avatar, item.author.avatarUrl);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.avatar.setImageBitmap(null);
    }

}
