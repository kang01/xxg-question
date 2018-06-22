package org.fwoxford.service.dto;


import org.fwoxford.service.dto.response.FrozenTubeForQuestion;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ReplyDetails entity.
 */
public class ReplyDetailsDTO extends FrozenTubeForQuestion implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String handleTypeCode;

    @NotNull
    @Size(max = 1024)
    private String replyContent;

    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long replyRecordId;
    @NotNull
    private Long questionItemDetailsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getReplyRecordId() {
        return replyRecordId;
    }

    public void setReplyRecordId(Long replyRecordId) {
        this.replyRecordId = replyRecordId;
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

        ReplyDetailsDTO replyDetailsDTO = (ReplyDetailsDTO) o;
        if(replyDetailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), replyDetailsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReplyDetailsDTO{" +
            "id=" + getId() +
            ", handleTypeCode='" + getHandleTypeCode() + "'" +
            ", replyContent='" + getReplyContent() + "'" +
            ", status='" + getStatus() + "'" +
            ", memo='" + getMemo() + "'" +
            "}";
    }
}
