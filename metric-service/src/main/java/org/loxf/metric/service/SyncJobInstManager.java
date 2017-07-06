package org.loxf.metric.service;

import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.common.dto.PageData;
import org.loxf.metric.common.dto.SyncJobInstDto;
import org.loxf.metric.dal.po.SyncJobInst;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luohj on 2017/5/5.
 */
@Component
public class SyncJobInstManager {
    @Autowired
    private SyncJobInstMapper syncJobInstMapper;

    public int createSyncInst(SyncJobInst inst){
        return syncJobInstMapper.insert(inst);
    }
    public int updateSyncInst(SyncJobInst inst){
        return syncJobInstMapper.updateInst(inst);
    }
    public int recoverySyncJobInst(String jobCode, Date circleTime){
        return syncJobInstMapper.recoverySyncJobInst(jobCode, circleTime);
    }
    public int deleteAllInstByJobCode(String jobCode){
        // 删除所有的JOB INST
        return syncJobInstMapper.delete(jobCode);
    }

    public int existsSyncInst(String jobCode, Date circleTime){
        return syncJobInstMapper.existsSyncInst(jobCode, circleTime);
    }

    public void cleanDataByCircleTime(Date circleTime, String dataTable){
        syncJobInstMapper.cleanDataByCircleTime(circleTime, dataTable);
    }
    public void cleanDataTable(String dataTable){
        syncJobInstMapper.cleanDataTable(dataTable);
    }

    public List<SyncJobInst> getWaitingInst(String jobCode){
        return syncJobInstMapper.getWaitingInst(jobCode);
    }

    public List<SyncJobInst> getExecutingInst(String jobCode){
        return syncJobInstMapper.getExecutingInst(jobCode);
    }

    public PageData<SyncJobInstDto> getSyncJobInst(SyncJobInstDto syncJobInst){
        try {
            List<SyncJobInstDto> result = new ArrayList<>();
            SyncJobInst cond = new SyncJobInst();
            BeanUtils.copyProperties(syncJobInst, cond);
            List<SyncJobInst> list = syncJobInstMapper.getSyncJobInst(cond);
            int count = syncJobInstMapper.getSyncJobInstCount(cond);
            if(CollectionUtils.isNotEmpty(list)){
                for(SyncJobInst inst : list){
                    SyncJobInstDto dto = new SyncJobInstDto();
                    BeanUtils.copyProperties(inst , dto);
                    result.add(dto);
                }
            }
            int totalPage = count/syncJobInst.getRow() + (count%syncJobInst.getRow()>0?1:0);
            return new PageData<SyncJobInstDto>(totalPage, count, result);
        } catch (Exception e){
            throw new MetricException("获取JOB实例失败：" + e.getMessage(), e);
        }
    }
}
