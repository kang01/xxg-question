package org.fwoxford.service.dto;

import java.util.List;

/**
 * Created by gengluying on 2018/7/5.
 */
public class QuestionSampleData {
    private Long total;

    List<QuestionItemDetailsDTO> questionItemDetailsDTOS;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<QuestionItemDetailsDTO> getQuestionItemDetailsDTOS() {
        return questionItemDetailsDTOS;
    }

    public void setQuestionItemDetailsDTOS(List<QuestionItemDetailsDTO> questionItemDetailsDTOS) {
        this.questionItemDetailsDTOS = questionItemDetailsDTOS;
    }
}
