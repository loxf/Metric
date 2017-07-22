package org.loxf.metric.api;

import com.google.common.util.concurrent.RateLimiter;
import org.loxf.metric.common.dto.BaseResult;
import org.loxf.metric.common.dto.RateLimitDto;

import java.util.List;

/**
 * Created by hutingting on 2017/7/22.
 */
public interface IRateLimitService {
    public List<RateLimitDto> getRateLimitList();

}
