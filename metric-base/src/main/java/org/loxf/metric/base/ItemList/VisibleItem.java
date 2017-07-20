package org.loxf.metric.base.ItemList;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by luohj on 2017/7/17.
 */
@ApiModel("可见范围")
public class VisibleItem {
    /**
     * 类型：USER/GROUP
     */
    @ApiModelProperty(value = "类型", example = "用户：USER/组：GROUP", required = true)
    private String type;
    /**
     * USERNAME/GROUP_ID
     */
    @ApiModelProperty(value = "编码", required = true)
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
