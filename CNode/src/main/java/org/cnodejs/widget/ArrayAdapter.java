package org.cnodejs.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ArrayAdapter<T, VH extends ArrayAdapter.ViewHolder>
        extends android.widget.ArrayAdapter<T> {

    public static class ViewHolder {
        public final View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            this.itemView.setTag(this);
        }
    }

    private final LayoutInflater inflater;

    public ArrayAdapter(Context context) {
        super(context, 0);
        this.inflater = LayoutInflater.from(context);
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindViewHolder(VH holder, int position);

    public void onViewRecycled(VH holder) {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = onCreateViewHolder(parent, getItemViewType(position)).itemView;
        } else {
            onViewRecycled(getViewHolder(convertView));
        }

        onBindViewHolder(getViewHolder(convertView), position);

        return convertView;
    }

    @SuppressWarnings("unchecked")
    private VH getViewHolder(View view) {
        return (VH) view.getTag();
    }

    protected View inflate(@LayoutRes int layout, ViewGroup parent) {
        return inflater.inflate(layout, parent, false);
    }

}
