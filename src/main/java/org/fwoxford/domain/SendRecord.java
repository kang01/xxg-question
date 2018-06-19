package org.fwoxford.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A SendRecord.
 */
@Entity
@Table(name = "send_record")
public class SendRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_send_record")
    @SequenceGenerator(name = "seq_send_record",sequenceName = "seq_send_record",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "sender_email", length = 255, nullable = false)
    private String senderEmail;

    @Size(max = 255)
    @Column(name = "stranger_name", length = 255)
    private String strangerName;

    @NotNull
    @Size(max = 255)
    @Column(name = "stranger_email", length = 255, nullable = false)
    private String strangerEmail;

    @NotNull
    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @NotNull
    @Size(max = 255)
    @Column(name = "question_code", length = 255, nullable = false)
    private String questionCode;

    @Column(name = "authorization_record_id")
    private Long authorizationRecordId;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public SendRecord senderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
        return this;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
    public String getStrangerName() {
        return strangerName;
    }

    public SendRecord strangerName(String strangerName) {
        this.strangerName = strangerName;
        return this;
    }

    public void setStrangerName(String strangerName) {
        this.strangerName = strangerName;
    }

    public String getStrangerEmail() {
        return strangerEmail;
    }

    public SendRecord strangerEmail(String strangerEmail) {
        this.strangerEmail = strangerEmail;
        return this;
    }

    public void setStrangerEmail(String strangerEmail) {
        this.strangerEmail = strangerEmail;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public SendRecord questionId(Long questionId) {
        this.questionId = questionId;
        return this;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public SendRecord questionCode(String questionCode) {
        this.questionCode = questionCode;
        return this;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public Long getAuthorizationRecordId() {
        return authorizationRecordId;
    }

    public SendRecord authorizationRecordId(Long authorizationRecordId) {
        this.authorizationRecordId = authorizationRecordId;
        return this;
    }

    public void setAuthorizationRecordId(Long authorizationRecordId) {
        this.authorizationRecordId = authorizationRecordId;
    }

    public String getStatus() {
        return status;
    }

    public SendRecord status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public SendRecord memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
        SendRecord sendRecord = (SendRecord) o;
        if (sendRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sendRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SendRecord{" +
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
}
