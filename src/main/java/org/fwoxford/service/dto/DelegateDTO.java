package org.fwoxford.service.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Delegate entity.
 */
public class DelegateDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String delegateCode;

    @NotNull
    @Size(max = 255)
    private String delegateName;

    @NotNull
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

    public String getDelegateCode() {
        return delegateCode;
    }

    public void setDelegateCode(String delegateCode) {
        this.delegateCode = delegateCode;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
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

        DelegateDTO delegateDTO = (DelegateDTO) o;

        if ( ! Objects.equals(id, delegateDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DelegateDTO{" +
            "id=" + id +
            ", delegateCode='" + delegateCode + "'" +
            ", delegateName='" + delegateName + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
