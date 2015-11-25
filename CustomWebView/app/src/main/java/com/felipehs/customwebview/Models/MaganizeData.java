package com.felipehs.customwebview.Models;

/**
 * Created by Felipe Haack Schmitz
 */
public class MaganizeData {

    private String issueName;
    private String issueIdentifier;
    private Integer issueTotalPages;
    private String issuePrice;
    private String issuePublicationDate;
    private String deviceId;

    public String getIssueName() {

        return issueName;
    }

    public void setIssueName(String issueName) {

        this.issueName = issueName;
    }

    public String getIssueIdentifier() {

        return issueIdentifier;
    }

    public void setIssueIdentifier(String issueIdentifier) {

        this.issueIdentifier = issueIdentifier;
    }

    public Integer getIssueTotalPages() {

        return issueTotalPages;
    }

    public void setIssueTotalPages(Integer issueTotalPages) {

        this.issueTotalPages = issueTotalPages;
    }

    public String getIssuePrice() {

        return issuePrice;
    }

    public void setIssuePrice(String issuePrice) {

        this.issuePrice = issuePrice;
    }

    public String getIssuePublicationDate() {

        return issuePublicationDate;
    }

    public void setIssuePublicationDate(String issuePublicationDate) {

        this.issuePublicationDate = issuePublicationDate;
    }

    public String getDeviceId() {

        return deviceId;
    }

    public void setDeviceId(String deviceId) {

        this.deviceId = deviceId;
    }
}
