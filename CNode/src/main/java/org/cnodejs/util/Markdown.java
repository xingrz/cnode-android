package org.cnodejs.util;

import android.widget.TextView;

public class Markdown {

    public static void render(TextView textView, String markdown) {
        // TODO: 更完善的 Markdown 解析
        textView.setText(markdown);
    }

}
