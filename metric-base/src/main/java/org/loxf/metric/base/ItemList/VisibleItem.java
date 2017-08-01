package org.loxf.metric.base.ItemList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by luohj on 2017/7/17.
 */
@ApiModel("可见范围")
public class VisibleItem implements Serializable {
    /**
     * 类型：USER/GROUP
     */
    @ApiModelProperty(value = "类型", example = "用户：USER/组：GROUP", required = true)
    private String type;
    /**
     * USERNAME/GROUP_ID
     */
    @ApiModelProperty(value = "编码", required = true)
    private String userName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
