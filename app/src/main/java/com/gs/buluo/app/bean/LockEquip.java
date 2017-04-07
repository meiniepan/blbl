package com.gs.buluo.app.bean;

import java.util.List;

/**
 * Created by hjn on 2017/3/10.
 */

public class LockEquip {
    public String id;
    public String communityId;
    public long createTime;
    public String managerId;
    public String name;
    public String desc;
    public List<String> activityTime;
    public boolean actived;
    public LockEquip(String name) {
        this.name =name;
    }
    public LockEquip() {
    }

    @Override
    public String toString() {
        return name;
    }
}
