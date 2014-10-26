package org.cnodejs.html;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.text.Html;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.cnodejs.BuildConfig;
import org.cnodejs.util.ImageLoader;

public abstract class HtmlImageGetter implements Html.ImageGetter {

    private static final String TAG = "HtmlImageGetter";

    private final Context context;

    public HtmlImageGetter(Context context) {
        this.context = context;
    }

    protected abstract void onImageLoaded();

    @Override
    public Drawable getDrawable(String source) {
        final BitmapDrawablePlaceHolder placeHolder = new BitmapDrawablePlaceHolder();
        final String url = ImageLoader.normalizeUrl(source);

        Picasso picasso = Picasso.with(context);
        picasso.setLoggingEnabled(BuildConfig.DEBUG);
        picasso.load(url).into(new Target() {
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d(TAG, "onPrepareLoad " + url);
            }

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "onBitmapLoaded " + url);
                placeHolder.setDrawable(new BitmapDrawable(context.getResources(), bitmap));
                onImageLoaded();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d(TAG, "onBitmapFailed " + url);
            }
        });

        return placeHolder;
    }

    public static class BitmapDrawablePlaceHolder extends Drawable {

        private static final PaintDrawable paint = new PaintDrawable(Color.argb(0x10, 0, 0, 0));

        static {
            paint.setBounds(0, 0, 200, 200);
        }

        private Drawable drawable;

        public BitmapDrawablePlaceHolder() {
            setBounds(0, 0, 200, 200);
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            this.drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }

        @Override
        public void draw(Canvas canvas) {
            if (drawable == null) {
                paint.draw(canvas);
            } else {
                drawable.draw(canvas);
            }
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
        }

        @Override
        public int getOpacity() {
            return 1;
        }

    }

}
