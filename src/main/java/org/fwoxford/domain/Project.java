package org.fwoxford.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Project extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_project")
    @SequenceGenerator(name = "seq_project",sequenceName = "seq_project",allocationSize = 1,initialValue = 1)
    private Long id;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 项目编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "project_code", length = 100, nullable = false)
    private String projectCode;
    /**
     * 项目名称
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "project_name", length = 255, nullable = false)
    private String projectName;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 项目与项目点的关系
     */
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProjectRelate> projectRelates = new HashSet<>();

    @ManyToOne
    private Delegate delegate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Project status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public Project projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public Project projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMemo() {
        return memo;
    }

    public Project memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Set<ProjectRelate> getProjectRelates() {
        return projectRelates;
    }

    public Project projectRelates(Set<ProjectRelate> projectRelates) {
        this.projectRelates = projectRelates;
        return this;
    }

    public Project addProjectRelate(ProjectRelate projectRelate) {
        this.projectRelates.add(projectRelate);
        projectRelate.setProject(this);
        return this;
    }

    public Project removeProjectRelate(ProjectRelate projectRelate) {
        this.projectRelates.remove(projectRelate);
        projectRelate.setProject(null);
        return this;
    }

    public void setProjectRelates(Set<ProjectRelate> projectRelates) {
        this.projectRelates = projectRelates;
    }

    public Delegate getDelegate() {
        return delegate;
    }
    public Project delegate(Delegate delegate) {
        this.delegate = delegate;
        return this;
    }
    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if (project.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + id +
            ", status='" + status + '\'' +
            ", projectCode='" + projectCode + '\'' +
            ", projectName='" + projectName + '\'' +
            ", memo='" + memo + '\'' +
            ", projectLeader='" + projectLeader + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", delegateName='" + delegateName + '\'' +
            ", contactPerson='" + contactPerson + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", sampleSpace='" + sampleSpace + '\'' +
            ", countOfSample=" + countOfSample +
            ", description='" + description + '\'' +
            '}';
    }

    @Size(max = 255)
    @Column(name = "project_leader", length = 255)
    private String projectLeader;

    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;

    @Size(max = 255)
    @Column(name = "delegate_name", length = 255)
    private String delegateName;

    @Size(max = 255)
    @Column(name = "contact_person", length = 255)
    private String contactPerson;

    @Size(max = 255)
    @Column(name = "phone_number", length = 255)
    private String phoneNumber;

    @Size(max = 255)
    @Column(name = "sample_space", length = 255)
    private String sampleSpace;

    @Column(name = "count_of_sample")
    private Long countOfSample;

    @Size(max = 1024)
    @Column(name = "description", length = 1024)
    private String description;

    public String getProjectLeader() {
        return projectLeader;
    }
    public Project projectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
        return this;
    }
    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public Project startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public Project endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDelegateName() {
        return delegateName;
    }
    public Project delegateName(String delegateName) {
        this.delegateName = delegateName;
        return this;
    }
    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public String getContactPerson() {
        return contactPerson;
    }
    public Project contactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
        return this;
    }
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public Project phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSampleSpace() {
        return sampleSpace;
    }
    public Project sampleSpace(String sampleSpace) {
        this.sampleSpace = sampleSpace;
        return this;
    }
    public void setSampleSpace(String sampleSpace) {
        this.sampleSpace = sampleSpace;
    }

    public Long getCountOfSample() {
        return countOfSample;
    }
    public Project countOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
        return this;
    }
    public void setCountOfSample(Long countOfSample) {
        this.countOfSample = countOfSample;
    }

    public String getDescription() {
        return description;
    }
    public Project description(String description) {
        this.description = description;
        return this;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
