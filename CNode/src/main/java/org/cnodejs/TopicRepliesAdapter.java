package org.cnodejs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.cnodejs.api.model.Reply;
import org.cnodejs.api.model.Topic;
import org.cnodejs.util.ImageLoader;

public class TopicRepliesAdapter extends RecyclerView.Adapter<TopicRepliesAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public TextView user;
        public TextView title;
        public WebView content;

        public ViewHolder(View itemView) {
            super(itemView);
            this.avatar = (ImageView) itemView.findViewById(R.id.avatar);
            this.user = (TextView) itemView.findViewById(R.id.user);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.content = (WebView) itemView.findViewById(R.id.content);
        }
    }

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_REPLY = 1;

    private final LayoutInflater inflater;

    private Topic topic;

    public TopicRepliesAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new ViewHolder(inflater.inflate(R.layout.topic_header, parent, false));
            case VIEW_TYPE_REPLY:
                return new ViewHolder(inflater.inflate(R.layout.reply_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER:
                holder.user.setText(topic.author.loginname);
                holder.title.setText(topic.title);
                holder.content.loadData(topic.content, "text/html", "utf8");
                break;
            case VIEW_TYPE_REPLY:
                Reply item = topic.replies.get(position - 1);
                holder.user.setText(item.author.loginname);
                holder.content.loadData(item.content, "text/html", "utf8");
                ImageLoader.load(holder.avatar, item.author.avatarUrl);
                break;
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_REPLY:
                holder.avatar.setImageBitmap(null);
                holder.content.loadData("", "text/html", null);
                break;
            case VIEW_TYPE_HEADER:
                holder.content.loadData("", "text/html", null);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return topic == null ? 0 : 1 + topic.replies.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_REPLY;
    }

}
