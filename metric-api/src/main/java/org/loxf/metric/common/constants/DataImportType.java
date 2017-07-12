package org.loxf.metric.common.constants;

/**
 * Created by luohj on 2017/7/12.
 */
public enum DataImportType {
    EXCEL("数据表导入"),
    SDK("SDK接口");

    DataImportType(String desc){
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    private String desc;
}
