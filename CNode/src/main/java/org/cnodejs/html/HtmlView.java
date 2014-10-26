package org.cnodejs.html;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;


public class HtmlView extends TextView {

    private static final String TAG = "HtmlView";

    public HtmlView(Context context) {
        super(context);
    }

    public HtmlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HtmlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public HtmlView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setHtml(String html) {
        setText(cleanCharSequence(Html.fromHtml(
                html,
                new HtmlImageGetter(getContext()) {
                    @Override
                    protected void onImageLoaded() {
                        invalidate();
                        requestLayout();
                        postInvalidate();
                        forceLayout();
                        //setText(getText());
                    }
                },
                new HtmlTagHandler()
        )));
    }

    private CharSequence cleanCharSequence(CharSequence source) {
        if (source == null) {
            return "";
        }

        int i = source.length();
        while (i > 0 && Character.isWhitespace(source.charAt(i - 1))) {
            i--;
        }

        return source.subSequence(0, i);
    }

}
