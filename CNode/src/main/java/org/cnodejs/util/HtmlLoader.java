package org.cnodejs.util;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.cnodejs.R;
import org.cnodejs.api.Constants;

public class HtmlLoader {

    private static String stylesheet;

    public static void load(final WebView webView, String content) {
        if (stylesheet == null) {
            stylesheet = "<style>" + webView.getContext().getString(R.string.topic_style) + "</style>";
        }

        webView.loadDataWithBaseURL(Constants.BASE, stylesheet + content, "text/html", null, null);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                webView.requestLayout();
            }
        });
    }

}
