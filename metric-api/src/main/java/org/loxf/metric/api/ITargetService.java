package org.loxf.metric.api;

import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.TargetDto;

import java.util.List;

/**
 * Created by hutingting on 2017/7/7.
 */
public interface ITargetService extends IBaseService<TargetDto> {
    public BaseResult<List<TargetDto>> queryTarget(TargetDto targetDto);
}
