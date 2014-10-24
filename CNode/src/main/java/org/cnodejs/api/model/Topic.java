package org.cnodejs.api.model;

import java.util.Date;
import java.util.List;

public class Topic {

    public String id;

    public String tab;

    public String title;

    public String content;

    public Date lastReplyAt;

    public boolean good;

    public boolean top;

    public User author;

    public List<Reply> replies;

}
