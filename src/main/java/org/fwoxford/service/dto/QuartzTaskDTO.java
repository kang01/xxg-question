package org.fwoxford.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the QuartzTask entity.
 */
public class QuartzTaskDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 256)
    private String jobName;

    @NotNull
    private String jobGroup;

    @NotNull
    @Size(max = 128)
    private String triggerName;

    @NotNull
    private String triggerGroup;

    @NotNull
    @Size(max = 128)
    private String className;

    @NotNull
    @Max(value = 10)
    private Integer enableStatus;

    @NotNull
    private String triggerTime;

    @NotNull
    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    private Long businessId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
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

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuartzTaskDTO quartzTaskDTO = (QuartzTaskDTO) o;
        if(quartzTaskDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quartzTaskDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuartzTaskDTO{" +
            "id=" + getId() +
            ", jobName='" + getJobName() + "'" +
            ", triggerName='" + getTriggerName() + "'" +
            ", className='" + getClassName() + "'" +
            ", enableStatus=" + getEnableStatus() +
            ", triggerTime='" + getTriggerTime() + "'" +
            ", status='" + getStatus() + "'" +
            ", memo='" + getMemo() + "'" +
            "}";
    }
}
