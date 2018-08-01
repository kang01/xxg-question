package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.fwoxford.service.dto.QuestionSampleData;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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

    /**
     * 上传问题样本
     * @param file
     * @param request
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/frozen-tubes/question/upload",method = RequestMethod.POST)
    @Timed
    public ResponseEntity<List<QuestionItemDetailsDTO>> getQuestionSampleByExcel(@RequestParam(value = "file",required = true) MultipartFile file,
                                                                                        HttpServletRequest request) throws URISyntaxException {
        List<QuestionItemDetailsDTO> result = frozenTubeService.findQuestionSampleByExcel(file,request);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

}
