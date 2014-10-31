package org.cnodejs.api.model;

import java.util.Date;

public class Reply {

    public String id;

    public User author;

    public String content;

    public Date createAt;

    @Override
    public boolean equals(Object o) {
        return o != null && (o instanceof Reply) && ((Reply) o).id.equals(id);
    }

}
