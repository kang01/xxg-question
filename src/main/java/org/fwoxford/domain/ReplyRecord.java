package org.fwoxford.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ReplyRecord.
 */
@Entity
@Table(name = "reply_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ReplyRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_reply_record")
    @SequenceGenerator(name = "seq_reply_record",sequenceName = "seq_reply_record",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "stranger_email", length = 255, nullable = false)
    private String strangerEmail;

    @Size(max = 255)
    @Column(name = "stranger_name", length = 255)
    private String strangerName;

    @NotNull
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @NotNull
    @Size(max = 255)
    @Column(name = "question_code", length = 255, nullable = false)
    private String questionCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @Size(max = 1024)
    @Column(name = "reply_content", length = 1024)
    private String replyContent;

    @ManyToOne(optional = false)
    @NotNull
    private SendRecord sendRecord;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStrangerEmail() {
        return strangerEmail;
    }

    public ReplyRecord strangerEmail(String strangerEmail) {
        this.strangerEmail = strangerEmail;
        return this;
    }

    public void setStrangerEmail(String strangerEmail) {
        this.strangerEmail = strangerEmail;
    }

    public String getStrangerName() {
        return strangerName;
    }

    public ReplyRecord strangerName(String strangerName) {
        this.strangerName = strangerName;
        return this;
    }

    public void setStrangerName(String strangerName) {
        this.strangerName = strangerName;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public ReplyRecord questionId(Long questionId) {
        this.questionId = questionId;
        return this;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public ReplyRecord questionCode(String questionCode) {
        this.questionCode = questionCode;
        return this;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getStatus() {
        return status;
    }

    public ReplyRecord status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public ReplyRecord memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getReplyContent() {
        return replyContent;
    }
    public ReplyRecord replyContent(String replyContent) {
        this.replyContent = replyContent;
        return this;
    }
    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public SendRecord getSendRecord() {
        return sendRecord;
    }
    public ReplyRecord sendRecord(SendRecord sendRecord) {
        this.sendRecord = sendRecord;
        return this;
    }
    public void setSendRecord(SendRecord sendRecord) {
        this.sendRecord = sendRecord;
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
        ReplyRecord replyRecord = (ReplyRecord) o;
        if (replyRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), replyRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReplyRecord{" +
            "id=" + getId() +
            ", strangerEmail='" + getStrangerEmail() + "'" +
            ", strangerName='" + getStrangerName() + "'" +
            ", questionId=" + getQuestionId() +
            ", questionCode='" + getQuestionCode() + "'" +
            ", status='" + getStatus() + "'" +
            ", memo='" + getMemo() + "'" +
            "}";
    }
}
