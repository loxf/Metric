package org.loxf.metric.common.dto;


import java.util.Date;
import java.util.List;

public class TargetDto extends Common{
    private Long id;

    private String targetId;

    private String targetName;

    private String targetDesc;

    private Integer state;

    private String busiDomain;

    private Date targetStartTime;

    private Date targetEndTime;

    private String boardId;

    private List<TargetItemDto> targetItemDtoLists;

    private List<TargetNoticeUserDto> targetNoticeUserDtos;

    private String updateAtStr;

    private String createUserId;

    private String createUserName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName == null ? null : targetName.trim();
    }

    public String getTargetDesc() {
        return targetDesc;
    }

    public void setTargetDesc(String targetDesc) {
        this.targetDesc = targetDesc == null ? null : targetDesc.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getBusiDomain() {
        return busiDomain;
    }

    public void setBusiDomain(String busiDomain) {
        this.busiDomain = busiDomain == null ? null : busiDomain.trim();
    }

    public Date getTargetStartTime() {
        return targetStartTime;
    }

    public void setTargetStartTime(Date targetStartTime) {
        this.targetStartTime = targetStartTime;
    }

    public Date getTargetEndTime() {
        return targetEndTime;
    }

    public void setTargetEndTime(Date targetEndTime) {
        this.targetEndTime = targetEndTime;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId == null ? null : boardId.trim();
    }

    public List<TargetItemDto> getTargetItemDtoLists() {
        return targetItemDtoLists;
    }

    public void setTargetItemDtoLists(List<TargetItemDto> targetItemDtoLists) {
        this.targetItemDtoLists = targetItemDtoLists;
    }

    public List<TargetNoticeUserDto> getTargetNoticeUserDtos() {
        return targetNoticeUserDtos;
    }

    public void setTargetNoticeUserDtos(List<TargetNoticeUserDto> targetNoticeUserDtos) {
        this.targetNoticeUserDtos = targetNoticeUserDtos;
    }

    public String getUpdateAtStr() {
        return updateAtStr;
    }

    public void setUpdateAtStr(String updateAtStr) {
        this.updateAtStr = updateAtStr;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}