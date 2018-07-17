package org.fwoxford.service;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.fwoxford.service.dto.QuestionSampleData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Service Interface for managing Delegate.
 */
public interface FrozenTubeService {
    /**
     * 查询问题样本
     * @param questionItemDetailsDTO
     * @return
     */
    List<QuestionItemDetailsDTO> findQuestionFrozenTube(QuestionItemDetailsDTO questionItemDetailsDTO);

    /**
     * 查询问题样本数量
     * @param questionItemDetailsDTO
     * @return
     */
    Long countQuestionFrozenTube(QuestionItemDetailsDTO questionItemDetailsDTO);

    /**
     * 上传问题样本表格
     * @param file
     * @param request
     * @return
     */
    List<QuestionItemDetailsDTO> findQuestionSampleByExcel(MultipartFile file, HttpServletRequest request);
}
