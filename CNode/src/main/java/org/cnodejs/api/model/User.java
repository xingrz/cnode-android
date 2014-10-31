package org.cnodejs.api.model;

import java.util.Date;
import java.util.List;

public class User {

    public String loginname;

    public String avatarUrl;

    public String githubUsername;

    public Date createAt;

    public int score;

    public List<Topic> recentTopics;

    @Override
    public boolean equals(Object o) {
        return o != null && (o instanceof User) && ((User) o).loginname.equals(loginname);
    }

}
