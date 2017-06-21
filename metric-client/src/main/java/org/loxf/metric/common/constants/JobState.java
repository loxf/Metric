package org.loxf.metric.common.constants;

/**
 * Created by luohj on 2017/5/5.
 */
public enum JobState {
    WAITING(0, "等待执行"),
    RUNNING(1, "执行中"),
    COMPLETED(9, "运行成功"),
    COMPLETED_FAILED(-9, "运行失败");

    JobState(int value, String desc){
        this.value = value;
        this.desc = desc;
    }
    private int value;
    private String desc;

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
