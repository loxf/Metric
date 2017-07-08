package org.loxf.metric.base.constants;

/**
 * Created by hutingting on 2017/7/6.
 */
public enum  CollectionConstants {
    CHART("chart"),
    BOARD("board"),
    INDEX_SETTING("index_setting"),
    OPERATION("operation"),
    QUOTA("quota"),
    QUOTA_DIMENSION("quota_dimension"),
    QUOTA_DIMENSION_VALUE("quota_dimension_value"),
    TARGET("target"),
    USER("user");

    private String collectionName;

    CollectionConstants(String collectionName){
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }

}
