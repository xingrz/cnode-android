package org.cnodejs.util;

import android.util.Log;

import org.cnodejs.html.HtmlView;

public class TextRenderer {

    private static final String WRAPPER_START = "<div class=\"markdown-text\">";
    private static final String WRAPPER_END = "</div>";

    public static void render(final HtmlView htmlView, String source) {
        // 处理掉最外层的 <div class="markdown-text"> ... </div>
        if (source.startsWith(WRAPPER_START) && source.endsWith(WRAPPER_END)) {
            source = source.substring(
                    WRAPPER_START.length(),
                    source.length() - WRAPPER_END.length());
            Log.d("TextRenderer", source);
        }

        htmlView.setHtml(source);
    }

}
