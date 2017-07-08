package org.loxf.metric.common.constants;

/**
 * Created by caiyang on 2017/5/12.
 */
public enum ChartTypeEnum {
    BROKENLINE("brokenLine", "折线图"),
    ROUND("round", "饼图"),
    HISTOGRAMHORIZONTAL ("histogramHorizontal", "水平柱状"),
    HISTOGRAMVERTICAL ("histogramVertical", "垂直柱状"),
    PILE ("pile", "堆叠图"),
    TABLE ("table", "表格");

    private String type;//类型
    private String desc;//值

    ChartTypeEnum(String type, String desc){
        this.type = type;
        this.desc = desc;
    }

    /**
     * 根据type获取value
     * @param type
     * @return String
     */
    public static String getValueByType(String type) {
        ChartTypeEnum[]enums=    ChartTypeEnum.values();
        for (int i = 0; i < enums.length; i++) {
            if (enums[i].getType().equals(type)) {
                return enums[i].getDesc();
            }
        }
        return "";
    }
    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public boolean equals(String str){
        if(str!=null){
            return str.equals(this.type);
        }
        return false;
    }
}
