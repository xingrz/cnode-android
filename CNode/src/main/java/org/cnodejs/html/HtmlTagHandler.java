package org.cnodejs.html;

import android.text.Editable;
import android.text.Html;

import org.xml.sax.XMLReader;

import java.util.LinkedList;

public class HtmlTagHandler implements Html.TagHandler {

    private static final String TAG = "HtmlTagHandler";

    private final LinkedList<Tag> hierarchy = new LinkedList<Tag>();

    private int indent = 0;

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("ul")) {
            handleUl(opening, output);
        } else if (tag.equalsIgnoreCase("ol")) {
            handleOl(opening, output);
        } else if (tag.equalsIgnoreCase("li")) {
            handleLi(opening, output);
        }
    }

    private void handleUl(boolean opening, Editable output) {
        if (opening) {
            hierarchy.addFirst(new Tag("ul"));
            if (output.charAt(output.length() - 1) != '\n') {
                output.append("\n");
            }
            indent++;
        } else {
            hierarchy.removeFirstOccurrence(new Tag("ul"));
            indent--;
        }
    }

    private void handleOl(boolean opening, Editable output) {
        if (opening) {
            hierarchy.addFirst(new Tag("ol"));
            if (output.charAt(output.length() - 1) != '\n') {
                output.append("\n");
            }
            indent++;
        } else {
            hierarchy.removeFirstOccurrence(new Tag("ol"));
            indent--;
        }
    }

    private void handleLi(boolean opening, Editable output) {
        if (opening) {
            hierarchy.addFirst(new Tag("li"));

            if (hierarchy.size() > 1) {
                Tag parent = hierarchy.get(1);

                parent.children++;
                if (parent.children > 1) {
                    output.append("\n");
                }

                for (int i = 1; i < indent; i++) {
                    output.append("    ");
                }

                if (parent.is("ul")) {
                    output.append("• ");
                } else if (parent.is("ol")) {
                    output.append(String.valueOf(parent.children)).append(". ");
                }
            }
        } else {
            hierarchy.removeFirstOccurrence(new Tag("li"));
        }
    }

    private static class Tag {

        private final String name;

        public int children = 0;

        public Tag(String name) {
            this.name = name;
        }

        public boolean is(String name) {
            return this.name.equals(name);
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof Tag) && ((Tag) o).is(name);
        }

    }

}
