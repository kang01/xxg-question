package org.fwoxford.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A QuartzTask.
 */
@Entity
@Table(name = "quartz_task")
public class QuartzTask extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_quartz_task")
    @SequenceGenerator(name = "seq_quartz_task" ,sequenceName = "seq_quartz_task",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "job_name", length = 256, nullable = false)
    private String jobName;

    @NotNull
    @Size(max = 256)
    @Column(name = "job_group", length = 256, nullable = false)
    private String jobGroup;

    @NotNull
    @Size(max = 128)
    @Column(name = "trigger_name", length = 128, nullable = false)
    private String triggerName;

    @NotNull
    @Size(max = 128)
    @Column(name = "trigger_group", length = 128, nullable = false)
    private String triggerGroup;

    @NotNull
    @Size(max = 128)
    @Column(name = "class_name", length = 128, nullable = false)
    private String className;

    @NotNull
    @Max(value = 10)
    @Column(name = "enable_status", nullable = false)
    private Integer enableStatus;

    @NotNull
    @Column(name = "trigger_time", nullable = false)
    private String triggerTime;

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

    public String getJobName() {
        return jobName;
    }

    public QuartzTask jobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getJobGroup() {
        return jobGroup;
    }
    public QuartzTask jobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
        return this;
    }
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public QuartzTask triggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
        return this;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public QuartzTask triggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getClassName() {
        return className;
    }

    public QuartzTask className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public QuartzTask enableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
        return this;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public String getTriggerTime() {
        return triggerTime;
    }

    public QuartzTask triggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
        return this;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getStatus() {
        return status;
    }

    public QuartzTask status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public QuartzTask memo(String memo) {
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
        QuartzTask quartzTask = (QuartzTask) o;
        if (quartzTask.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quartzTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuartzTask{" +
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
