package org.fwoxford.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ReplyRecord entity.
 */
public class ReplyRecordDTO implements Serializable {

    private Long id;

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

    @NotNull
    @Size(max = 100)
    private String handleTypeCode;

    @NotNull
    @Size(max = 1024)
    private String replyContent;

    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long questionItemDetailsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStrangerEmail() {
        return strangerEmail;
    }

    public void setStrangerEmail(String strangerEmail) {
        this.strangerEmail = strangerEmail;
    }

    public String getStrangerName() {
        return strangerName;
    }

    public void setStrangerName(String strangerName) {
        this.strangerName = strangerName;
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

    public String getHandleTypeCode() {
        return handleTypeCode;
    }

    public void setHandleTypeCode(String handleTypeCode) {
        this.handleTypeCode = handleTypeCode;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
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

    public Long getQuestionItemDetailsId() {
        return questionItemDetailsId;
    }

    public void setQuestionItemDetailsId(Long questionItemDetailsId) {
        this.questionItemDetailsId = questionItemDetailsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReplyRecordDTO replyRecordDTO = (ReplyRecordDTO) o;
        if(replyRecordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), replyRecordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReplyRecordDTO{" +
            "id=" + getId() +
            ", strangerEmail='" + getStrangerEmail() + "'" +
            ", strangerName='" + getStrangerName() + "'" +
            ", questionId=" + getQuestionId() +
            ", questionCode='" + getQuestionCode() + "'" +
            ", handleTypeCode='" + getHandleTypeCode() + "'" +
            ", replyContent='" + getReplyContent() + "'" +
            ", status='" + getStatus() + "'" +
            ", memo='" + getMemo() + "'" +
            "}";
    }
}
