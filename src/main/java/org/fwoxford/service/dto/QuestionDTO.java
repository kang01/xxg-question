package org.fwoxford.service.dto;

import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Question entity.
 */
public class QuestionDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String questionCode;

    @Size(max = 100)
    private String projectCode;

    @Size(max = 255)
    private String projectName;

    private Long authorId;

    @Size(max = 255)
    private String author;

    private LocalDate occurDate;

    @Size(max = 20)
    private String questionTypeCode;

    @Size(max = 255)
    private String questionSummary;

    @Lob
    private String questionDescription;

    @Size(max = 255)
    private String replyPerson;

    private LocalDate replyDate;

    @Size(max = 255)
    private String relatedAgency;

    private Long relatedAgencyId;

    @Size(max = 20)
    private String status;

    @Size(max = 2048)
    private String memo;

    private Long projectId;

    private List<QuestionItemDTO> questionItemDTOList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }
    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }
    public String getProjectName() {
        return projectName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public LocalDate getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(LocalDate occurDate) {
        this.occurDate = occurDate;
    }

    public String getQuestionTypeCode() {
        return questionTypeCode;
    }

    public void setQuestionTypeCode(String questionTypeCode) {
        this.questionTypeCode = questionTypeCode;
    }

    public String getQuestionSummary() {
        return questionSummary;
    }

    public void setQuestionSummary(String questionSummary) {
        this.questionSummary = questionSummary;
    }
    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }
    public String getReplyPerson() {
        return replyPerson;
    }

    public void setReplyPerson(String replyPerson) {
        this.replyPerson = replyPerson;
    }
    public LocalDate getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(LocalDate replyDate) {
        this.replyDate = replyDate;
    }
    public String getRelatedAgency() {
        return relatedAgency;
    }

    public void setRelatedAgency(String relatedAgency) {
        this.relatedAgency = relatedAgency;
    }
    public Long getRelatedAgencyId() {
        return relatedAgencyId;
    }

    public void setRelatedAgencyId(Long relatedAgencyId) {
        this.relatedAgencyId = relatedAgencyId;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;

        if ( ! Objects.equals(id, questionDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + id +
            ", questionCode='" + questionCode + "'" +
            ", projectCode='" + projectCode + "'" +
            ", projectName='" + projectName + "'" +
            ", authorId='" + authorId + "'" +
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

    public List<QuestionItemDTO> getQuestionItemDTOList() {
        return questionItemDTOList;
    }

    public void setQuestionItemDTOList(List<QuestionItemDTO> questionItemDTOList) {
        this.questionItemDTOList = questionItemDTOList;
    }
}
