package org.fwoxford.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AuthorizationRecord entity.
 */
public class AuthorizationRecordDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String authorizationCode;

    @Size(max = 255)
    private String strangerName;

    private Long questionId;

    @Size(max = 255)
    private String questionCode;

    @NotNull
    @Size(max = 255)
    private String strangerEmail;

    private ZonedDateTime expirationTime;

    @Max(value = 10)
    private Integer applyTimes;

    private Long authorityPersonId;

    @Size(max = 255)
    private String authorityName;

    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
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

    public ZonedDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(ZonedDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Integer getApplyTimes() {
        return applyTimes;
    }

    public void setApplyTimes(Integer applyTimes) {
        this.applyTimes = applyTimes;
    }

    public Long getAuthorityPersonId() {
        return authorityPersonId;
    }

    public void setAuthorityPersonId(Long authorityPersonId) {
        this.authorityPersonId = authorityPersonId;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthorizationRecordDTO authorizationRecordDTO = (AuthorizationRecordDTO) o;
        if(authorizationRecordDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), authorizationRecordDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuthorizationRecordDTO{" +
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
