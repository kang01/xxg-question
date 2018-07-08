package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.sf.json.JSONObject;
import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.fwoxford.service.dto.QuestionSampleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing FrozenTube.
 */
@RestController
@RequestMapping("/api")
public class FrozenTubeResource {

    private final Logger log = LoggerFactory.getLogger(FrozenTubeResource.class);

    private static final String ENTITY_NAME = "frozenTube";
    @Autowired
    FrozenTubeService frozenTubeService;

    /**
     * 查询问题样本
     * @param searchForm
     * @return
     * @throws URISyntaxException
     */
    @GetMapping("/frozen-tubes/question")
    @Timed
    public QuestionSampleData getQuestionFrozenTube(
        @RequestParam(value = "searchForm",required = false) String searchForm) throws URISyntaxException {
        log.debug("REST request to getQuestionFrozenTube : {}", searchForm);
        JSONObject jsonObject = JSONObject.fromObject(searchForm);
        QuestionItemDetailsDTO questionItemDetailsDTO = (QuestionItemDetailsDTO) JSONObject.toBean(jsonObject, QuestionItemDetailsDTO.class);
        QuestionSampleData questionSampleData = new QuestionSampleData();
        Long total = frozenTubeService.countQuestionFrozenTube(questionItemDetailsDTO);
        List<QuestionItemDetailsDTO> result = frozenTubeService.findQuestionFrozenTube(questionItemDetailsDTO);
        questionSampleData.setTotal(total);
        questionSampleData.setQuestionItemDetailsDTOS(result);
        return  questionSampleData;
    }
}
