package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Question extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_question")
    @SequenceGenerator(name = "seq_question",sequenceName = "seq_question",allocationSize = 1,initialValue = 1)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "question_code", length = 100, nullable = false)
    private String questionCode;

    @Size(max = 100)
    @Column(name = "project_code", length = 100)
    private String projectCode;

    @Size(max = 255)
    @Column(name = "project_name", length = 255)
    private String projectName;

    @Column(name = "author_id")
    private Long authorId;

    @Size(max = 255)
    @Column(name = "author", length = 255)
    private String author;

    @Column(name = "occur_date")
    private LocalDate occurDate;

    @Size(max = 20)
    @Column(name = "question_type_code", length = 20)
    private String questionTypeCode;

    @Size(max = 255)
    @Column(name = "question_summary", length = 255)
    private String questionSummary;

    @Size(max = 1024)
    @Column(name = "question_description", length = 1024)
    private String questionDescription;

    @Size(max = 255)
    @Column(name = "reply_person", length = 255)
    private String replyPerson;

    @Column(name = "reply_date")
    private LocalDate replyDate;

    @Size(max = 255)
    @Column(name = "related_agency", length = 255)
    private String relatedAgency;

    @Column(name = "related_agency_id")
    private Long relatedAgencyId;

    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;

    @ManyToOne
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public Question questionCode(String questionCode) {
        this.questionCode = questionCode;
        return this;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public Question projectCode(String projectCode) {
        this.projectCode = projectCode;
        return this;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public Question projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public Question authorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public Question author(String author) {
        this.author = author;
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getOccurDate() {
        return occurDate;
    }

    public Question occurDate(LocalDate occurDate) {
        this.occurDate = occurDate;
        return this;
    }

    public void setOccurDate(LocalDate occurDate) {
        this.occurDate = occurDate;
    }

    public String getQuestionTypeCode() {
        return questionTypeCode;
    }

    public Question questionTypeCode(String questionTypeCode) {
        this.questionTypeCode = questionTypeCode;
        return this;
    }

    public void setQuestionTypeCode(String questionTypeCode) {
        this.questionTypeCode = questionTypeCode;
    }

    public String getQuestionSummary() {
        return questionSummary;
    }

    public Question questionSummary(String questionSummary) {
        this.questionSummary = questionSummary;
        return this;
    }

    public void setQuestionSummary(String questionSummary) {
        this.questionSummary = questionSummary;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public Question questionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
        return this;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public String getReplyPerson() {
        return replyPerson;
    }

    public Question replyPerson(String replyPerson) {
        this.replyPerson = replyPerson;
        return this;
    }

    public void setReplyPerson(String replyPerson) {
        this.replyPerson = replyPerson;
    }

    public LocalDate getReplyDate() {
        return replyDate;
    }

    public Question replyDate(LocalDate replyDate) {
        this.replyDate = replyDate;
        return this;
    }

    public void setReplyDate(LocalDate replyDate) {
        this.replyDate = replyDate;
    }

    public String getRelatedAgency() {
        return relatedAgency;
    }

    public Question relatedAgency(String relatedAgency) {
        this.relatedAgency = relatedAgency;
        return this;
    }

    public void setRelatedAgency(String relatedAgency) {
        this.relatedAgency = relatedAgency;
    }

    public Long getRelatedAgencyId() {
        return relatedAgencyId;
    }

    public Question relatedAgencyId(Long relatedAgencyId) {
        this.relatedAgencyId = relatedAgencyId;
        return this;
    }

    public void setRelatedAgencyId(Long relatedAgencyId) {
        this.relatedAgencyId = relatedAgencyId;
    }

    public String getStatus() {
        return status;
    }

    public Question status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMemo() {
        return memo;
    }

    public Question memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Project getProject() {
        return project;
    }

    public Question project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        if (question.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", questionCode='" + questionCode + "'" +
            ", projectCode='" + projectCode + "'" +
            ", projectName='" + projectName + "'" +
            ", author='" + author + "'" +
            ", occurDate='" + occurDate + "'" +
            ", questionTypeCode='" + questionTypeCode + "'" +
            ", questionSummary='" + questionSummary + "'" +
            ", questionDescription='" + questionDescription + "'" +
            ", replyPerson='" + replyPerson + "'" +
            ", replyDate='" + replyDate + "'" +
            ", relatedAgency='" + relatedAgency + "'" +
            ", relatedAgencyId='" + relatedAgencyId + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
