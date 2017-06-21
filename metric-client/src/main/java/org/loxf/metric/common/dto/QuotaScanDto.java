package org.loxf.metric.common.dto;


public class QuotaScanDto extends Common{
    private Long id;

    private String scanId;

    private String quotaId;

    private String boardId;

    private String busiDomain;

    private Integer state;

    private String quotaName;

    private String boardName;

    private String busiName;


    private String updateUserId;

    private  String updateUserName;

    private String  quotaScanShowName;

    private Integer quotaScanType;

    public String getQuotaName() {
        return quotaName;
    }

    public void setQuotaName(String quotaName) {
        this.quotaName = quotaName;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBusiName() {
        return busiName;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId == null ? null : scanId.trim();
    }

    public String getQuotaId() {
        return quotaId;
    }

    public void setQuotaId(String quotaId) {
        this.quotaId = quotaId == null ? null : quotaId.trim();
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId == null ? null : boardId.trim();
    }

    public String getBusiDomain() {
        return busiDomain;
    }

    public void setBusiDomain(String busiDomain) {
        this.busiDomain = busiDomain == null ? null : busiDomain.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getQuotaScanShowName() {
        return quotaScanShowName;
    }

    public void setQuotaScanShowName(String quotaScanShowName) {
        this.quotaScanShowName = quotaScanShowName;
    }

    public Integer getQuotaScanType() {
        return quotaScanType;
    }

    public void setQuotaScanType(Integer quotaScanType) {
        this.quotaScanType = quotaScanType;
    }
}