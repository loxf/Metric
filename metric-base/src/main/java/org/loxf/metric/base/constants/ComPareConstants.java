package org.loxf.metric.base.constants;

/**
 * Created by hutingting on 2017/7/7.
 */
public enum  ComPareConstants {//$lt/$lte/$gt/$gte/$ne，依次等价于</<=/>/>=/!=。
    BIGGERTHAN("$gt"),
    LITTERTHAN("$lt"),
    BIGGEROREQUAL("$gte"),
    LITTEROREQUAL("$lte"),
    NOTEQUAL("$ne"),
    IN("$in"),
    NOTIN("$nin"),
    OR("$or");

    private String displayName;
    ComPareConstants(String displayName){
        this.displayName=displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
