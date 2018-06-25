package org.fwoxford.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;



import io.github.jhipster.service.filter.ZonedDateTimeFilter;


/**
 * Criteria class for the QuartzTask entity. This class is used in QuartsTaskResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /quarts-tasks?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuartzTaskCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter jobName;

    private StringFilter triggerName;

    private StringFilter className;

    private IntegerFilter enableStatus;

    private ZonedDateTimeFilter triggerTime;

    private StringFilter status;

    private StringFilter memo;

    public QuartzTaskCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getJobName() {
        return jobName;
    }

    public void setJobName(StringFilter jobName) {
        this.jobName = jobName;
    }

    public StringFilter getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(StringFilter triggerName) {
        this.triggerName = triggerName;
    }

    public StringFilter getClassName() {
        return className;
    }

    public void setClassName(StringFilter className) {
        this.className = className;
    }

    public IntegerFilter getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(IntegerFilter enableStatus) {
        this.enableStatus = enableStatus;
    }

    public ZonedDateTimeFilter getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(ZonedDateTimeFilter triggerTime) {
        this.triggerTime = triggerTime;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getMemo() {
        return memo;
    }

    public void setMemo(StringFilter memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "QuartzTaskCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (jobName != null ? "jobName=" + jobName + ", " : "") +
                (triggerName != null ? "triggerName=" + triggerName + ", " : "") +
                (className != null ? "className=" + className + ", " : "") +
                (enableStatus != null ? "enableStatus=" + enableStatus + ", " : "") +
                (triggerTime != null ? "triggerTime=" + triggerTime + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (memo != null ? "memo=" + memo + ", " : "") +
            "}";
    }

}
