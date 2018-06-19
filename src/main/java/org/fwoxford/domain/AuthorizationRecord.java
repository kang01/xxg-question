package org.fwoxford.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A AuthorizationRecord.
 */
@Entity
@Table(name = "authorization_record")
public class AuthorizationRecord extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_authorization_record")
    @SequenceGenerator(name = "seq_authorization_record",sequenceName = "seq_authorization_record",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "authorization_code", length = 255, nullable = false)
    private String authorizationCode;

    @Size(max = 255)
    @Column(name = "stranger_name", length = 255)
    private String strangerName;

    @NotNull
    @Size(max = 255)
    @Column(name = "stranger_email", length = 255, nullable = false)
    private String strangerEmail;

    @NotNull
    @Column(name = "expiration_time", nullable = false)
    private ZonedDateTime expirationTime;

    @Max(value = 10)
    @Column(name = "apply_times")
    private Integer applyTimes;

    @NotNull
    @Column(name = "authority_person_id", nullable = false)
    private Long authorityPersonId;

    @NotNull
    @Size(max = 255)
    @Column(name = "authority_name", length = 255, nullable = false)
    private String authorityName;

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

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public AuthorizationRecord authorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
        return this;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getStrangerName() {
        return strangerName;
    }

    public AuthorizationRecord strangerName(String strangerName) {
        this.strangerName = strangerName;
        return this;
    }

    public void setStrangerName(String strangerName) {
        this.strangerName = strangerName;
    }

    public String getStrangerEmail() {
        return strangerEmail;
    }

    public AuthorizationRecord strangerEmail(String strangerEmail) {
        this.strangerEmail = strangerEmail;
        return this;
    }

    public void setStrangerEmail(String strangerEmail) {
        this.strangerEmail = strangerEmail;
    }

    public ZonedDateTime getExpirationTime() {
        return expirationTime;
    }

    public AuthorizationRecord expirationTime(ZonedDateTime expirationTime) {
        this.expirationTime = expirationTime;
        return this;
    }

    public void setExpirationTime(ZonedDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Integer getApplyTimes() {
        return applyTimes;
    }

    public AuthorizationRecord applyTimes(Integer applyTimes) {
        this.applyTimes = applyTimes;
        return this;
    }

    public void setApplyTimes(Integer applyTimes) {
        this.applyTimes = applyTimes;
    }

    public Long getAuthorityPersonId() {
        return authorityPersonId;
    }

    public AuthorizationRecord authorityPersonId(Long authorityPersonId) {
        this.authorityPersonId = authorityPersonId;
        return this;
    }

    public void setAuthorityPersonId(Long authorityPersonId) {
        this.authorityPersonId = authorityPersonId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public AuthorizationRecord authorityName(String authorityName) {
        this.authorityName = authorityName;
        return this;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getStatus() {
        return status;
    }

    public AuthorizationRecord status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public AuthorizationRecord memo(String memo) {
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
        AuthorizationRecord authorizationRecord = (AuthorizationRecord) o;
        if (authorizationRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), authorizationRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuthorizationRecord{" +
            "id=" + getId() +
            ", authorizationCode='" + getAuthorizationCode() + "'" +
            ", strangerName='" + getStrangerName() + "'" +
            ", strangerEmail='" + getStrangerEmail() + "'" +
            ", expirationTime='" + getExpirationTime() + "'" +
            ", applyTimes=" + getApplyTimes() +
            ", authorityPersonId=" + getAuthorityPersonId() +
            ", authorityName='" + getAuthorityName() + "'" +
            ", status='" + getStatus() + "'" +
            ", memo='" + getMemo() + "'" +
            "}";
    }
}
