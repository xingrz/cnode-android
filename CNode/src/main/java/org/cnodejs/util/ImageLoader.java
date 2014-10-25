package org.cnodejs.util;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.cnodejs.BuildConfig;

public class ImageLoader {

    public static void load(ImageView view, String url) {
        if (url.startsWith("//")) {
            url = "https:" + url;
        }

        Picasso picasso = Picasso.with(view.getContext());
        picasso.setLoggingEnabled(BuildConfig.DEBUG);
        picasso.load(url).into(view);
    }

}
