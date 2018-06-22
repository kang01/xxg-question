package org.fwoxford.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A QuartsTask.
 */
@Entity
@Table(name = "quarts_task")
public class QuartsTask extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_quarts_task")
    @SequenceGenerator(name = "seq_quarts_task" ,sequenceName = "seq_quarts_task",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 256)
    @Column(name = "job_name", length = 256, nullable = false)
    private String jobName;

    @NotNull
    @Size(max = 128)
    @Column(name = "trigger_name", length = 128, nullable = false)
    private String triggerName;

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
    private ZonedDateTime triggerTime;

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

    public QuartsTask jobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public QuartsTask triggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getClassName() {
        return className;
    }

    public QuartsTask className(String className) {
        this.className = className;
        return this;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getEnableStatus() {
        return enableStatus;
    }

    public QuartsTask enableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
        return this;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public ZonedDateTime getTriggerTime() {
        return triggerTime;
    }

    public QuartsTask triggerTime(ZonedDateTime triggerTime) {
        this.triggerTime = triggerTime;
        return this;
    }

    public void setTriggerTime(ZonedDateTime triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getStatus() {
        return status;
    }

    public QuartsTask status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public QuartsTask memo(String memo) {
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
        QuartsTask quartsTask = (QuartsTask) o;
        if (quartsTask.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), quartsTask.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "QuartsTask{" +
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
