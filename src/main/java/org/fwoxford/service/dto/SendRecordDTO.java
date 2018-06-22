package org.fwoxford.service.dto;


import org.fwoxford.domain.AbstractAuditingEntity;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the SendRecord entity.
 */
public class SendRecordDTO extends AbstractAuditingEntity implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String senderEmail;

    @NotNull
    @Size(max = 255)
    private String strangerEmail;

    @Size(max = 255)
    private String strangerName;

    @NotNull
    private Long questionId;

    @NotNull
    @Size(max = 255)
    private String questionCode;

    private Long authorizationRecordId;

    private String status;

    @Size(max = 1024)
    private String memo;

    private ZonedDateTime replyDate ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getStrangerName() {
        return strangerName;
    }

    public void setStrangerName(String strangerName) {
        this.strangerName = strangerName;
    }

    public String getStrangerEmail() {
        return strangerEmail;
    }

    public void setStrangerEmail(String strangerEmail) {
        this.strangerEmail = strangerEmail;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public Long getAuthorizationRecordId() {
        return authorizationRecordId;
    }

    public void setAuthorizationRecordId(Long authorizationRecordId) {
        this.authorizationRecordId = authorizationRecordId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public ZonedDateTime getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(ZonedDateTime replyDate) {
        this.replyDate = replyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SendRecordDTO sendRecordDTO = (SendRecordDTO) o;
        if(sendRecordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sendRecordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SendRecordDTO{" +
            "id=" + getId() +
            ", senderEmail='" + getSenderEmail() + "'" +
            ", strangerEmail='" + getStrangerEmail() + "'" +
            ", questionId=" + getQuestionId() +
            ", questionCode='" + getQuestionCode() + "'" +
            ", authorizationRecordId=" + getAuthorizationRecordId() +
            ", status='" + getStatus() + "'" +
            ", memo='" + getMemo() + "'" +
            "}";
    }

    private String questionTypeCode;
    private String questionSummary;
    private Long projectId;
    private String projectCode;
    private Long countOfSample;

    public String getQuestionTypeCode() {
        return questionTypeCode;
    }

    public void setQuestionTypeCode(String questionTypeCode) {
        this.questionTypeCode = questionTypeCode;
    }

    public String getQuestionSummary() {
        return questionSummary;
    }

    public void setQuestionSummary(String questionSummary) {
        this.questionSummary = questionSummary;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }

    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }

    private String projectName;
    private LocalDate sendDate;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDate sendDate) {
        this.sendDate = sendDate;
    }
}
