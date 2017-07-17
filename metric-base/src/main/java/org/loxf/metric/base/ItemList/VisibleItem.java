package org.loxf.metric.base.ItemList;

/**
 * Created by luohj on 2017/7/17.
 */
public class VisibleItem {
    /**
     * 类型：USER/GROUP
     */
    private String type;
    /**
     * USERNAME/GROUP_ID
     */
    private String code;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
