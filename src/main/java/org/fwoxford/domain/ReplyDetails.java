package org.fwoxford.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ReplyDetails.
 */
@Entity
@Table(name = "reply_details")
public class ReplyDetails extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reply_details")
    @SequenceGenerator(name = "seq_reply_details",sequenceName = "seq_reply_details",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "handle_type_code", length = 255, nullable = false)
    private String handleTypeCode;

    @NotNull
    @Size(max = 1024)
    @Column(name = "reply_content", length = 1024, nullable = false)
    private String replyContent;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne(optional = false)
    @NotNull
    private ReplyRecord replyRecord;

    @ManyToOne(optional = false)
    @NotNull
    private QuestionItemDetails questionItemDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHandleTypeCode() {
        return handleTypeCode;
    }

    public ReplyDetails handleTypeCode(String handleTypeCode) {
        this.handleTypeCode = handleTypeCode;
        return this;
    }

    public void setHandleTypeCode(String handleTypeCode) {
        this.handleTypeCode = handleTypeCode;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public ReplyDetails replyContent(String replyContent) {
        this.replyContent = replyContent;
        return this;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getStatus() {
        return status;
    }

    public ReplyDetails status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public ReplyDetails memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public ReplyRecord getReplyRecord() {
        return replyRecord;
    }

    public ReplyDetails replyRecord(ReplyRecord replyRecord) {
        this.replyRecord = replyRecord;
        return this;
    }

    public void setReplyRecord(ReplyRecord replyRecord) {
        this.replyRecord = replyRecord;
    }

    public QuestionItemDetails getQuestionItemDetails() {
        return questionItemDetails;
    }

    public ReplyDetails questionItemDetails(QuestionItemDetails questionItemDetails) {
        this.questionItemDetails = questionItemDetails;
        return this;
    }

    public void setQuestionItemDetails(QuestionItemDetails questionItemDetails) {
        this.questionItemDetails = questionItemDetails;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReplyDetails replyDetails = (ReplyDetails) o;
        if (replyDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), replyDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReplyDetails{" +
            "id=" + getId() +
            ", handleTypeCode='" + getHandleTypeCode() + "'" +
            ", replyContent='" + getReplyContent() + "'" +
            ", status='" + getStatus() + "'" +
            ", memo='" + getMemo() + "'" +
            "}";
    }
}
