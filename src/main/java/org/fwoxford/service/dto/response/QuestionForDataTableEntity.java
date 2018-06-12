package org.fwoxford.service.dto.response;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by gengluying on 2018/2/11.
 */
@Entity
@Table(name = "view_question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QuestionForDataTableEntity {

    @Id
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private Long id;
    /**
     * 问题编码
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "question_code")
    private String questionCode;
    /**
     * 相关项目
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "project_code")
    private String projectCode;
    /**
     * 相关单位
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "related_agency")
    private String relatedAgency;
    /**
     * 提问人
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "author")
    private String author;
    /**
     * 提问日期
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "occur_date")
    private String occurDate;
    /**
     * 问题描述
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "question_summary")
    private String questionSummary;
    /**
     * 回答日期
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "reply_date")
    private String replyDate;
    /**
     * 状态：草拟中，已提问，已回复，已过期，已结束
     */
    @NotNull
    @JsonView(DataTablesOutput.View.class)
    private String status;

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

    public String getRelatedAgency() {
        return relatedAgency;
    }

    public void setRelatedAgency(String relatedAgency) {
        this.relatedAgency = relatedAgency;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(String occurDate) {
        this.occurDate = occurDate;
    }

    public String getQuestionSummary() {
        return questionSummary;
    }

    public void setQuestionSummary(String questionSummary) {
        this.questionSummary = questionSummary;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
