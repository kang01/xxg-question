package org.fwoxford.service.dto;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the QuestionItem entity.
 */
public class QuestionItemDTO  implements Serializable {

    private Long id;

    @Size(max = 1024)
    private String questionDescription;

    @Size(max = 20)
    private String status;

    @Size(max = 1024)
    private String memo;

    @NotNull
    private Long questionId;

    private List<Long> frozenTubeIds;

    private List<QuestionItemDetailsDTO> questionItemDetailsDTOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
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

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public List<Long> getFrozenTubeIds() {
        return frozenTubeIds;
    }

    public void setFrozenTubeIds(List<Long> frozenTubeIds) {
        this.frozenTubeIds = frozenTubeIds;
    }

    public List<QuestionItemDetailsDTO> getQuestionItemDetailsDTOS() {
        return questionItemDetailsDTOS;
    }

    public void setQuestionItemDetailsDTOS(List<QuestionItemDetailsDTO> questionItemDetailsDTOS) {
        this.questionItemDetailsDTOS = questionItemDetailsDTOS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionItemDTO questionItemDTO = (QuestionItemDTO) o;

        if ( ! Objects.equals(id, questionItemDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QuestionItemDTO{" +
            "id=" + id +
            ", questionDescription='" + questionDescription + "'" +
            ", status='" + status + "'" +
            ", memo='" + memo + "'" +
            '}';
    }
}
