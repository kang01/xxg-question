package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import org.fwoxford.core.config.Constants;
import org.fwoxford.core.domain.FrozenBox;
import org.fwoxford.core.repository.FrozenBoxRepository;
import org.fwoxford.core.repository.FrozenTubeRepository;
import org.fwoxford.core.service.mapper.FrozenTubeMapper;
import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing FrozenTube.
 */
@Service
@Transactional
public class FrozenTubeServiceImpl implements FrozenTubeService {

    private final Logger log = LoggerFactory.getLogger(FrozenTubeServiceImpl.class);
    @Autowired
    FrozenTubeRepository frozenTubeRepository;
    @Autowired
    FrozenTubeMapper frozenTubeMapper;
    @Autowired
    EntityManager entityManager;
    @Autowired
    FrozenBoxRepository frozenBoxRepository;

    /**
     * 查询问题样本
     * @param questionItemDetailsDTO
     * @return
     */
    @Override
    public List<QuestionItemDetailsDTO> findQuestionFrozenTube(QuestionItemDetailsDTO questionItemDetailsDTO) {
        List<QuestionItemDetailsDTO> result = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT t.ID,t.SAMPLE_CODE,t.SAMPLE_TYPE_NAME,t.SAMPLE_CLASSIFICATION_NAME,t.PROJECT_CODE," +
            "t.FROZEN_BOX_ID,t.FROZEN_BOX_CODE,t.TUBE_ROWS,t.TUBE_COLUMNS," +
            "NVL(t.TAG1,'占位符'), " +
            "NVL(t.TAG2,'占位符'), " +
            "NVL(t.TAG3,'占位符'), " +
            "NVL(t.TAG4,'占位符'),"+
//            "case when t.TAG1 is null THEN  to_char('占位符') else  to_char(t.TAG1) end  ," +
//            "case when t.TAG2 is null THEN  to_char('占位符') else  to_char(t.TAG2)end  ," +
//            "case when t.TAG3 is null THEN  to_char('占位符') else  to_char(t.TAG3) end  ," +
//            "case when t.TAG4 is null THEN  to_char('占位符') else  to_char(t.TAG4) end  ," +
            "t.STATUS,t.SAMPLE_TYPE_ID,t.SAMPLE_TYPE_CODE,t.SAMPLE_CLASSIFICATION_ID,t.SAMPLE_CLASSIFICATION_CODE," +
//            "  case when t.MEMO is null THEN  to_char('占位符') else  to_char(t.MEMO) end  " +
            "  NVL(t.MEMO,'占位符')  " +
            " FROM FROZEN_TUBE t " );
            if(!StringUtils.isEmpty(questionItemDetailsDTO.getFrozenBoxCode1D()))
            {
                sql.append("LEFT JOIN FROZEN_BOX b ON b.ID = t.FROZEN_BOX_ID");
            }
            sql.append(" WHERE rownum <=500 and t.STATUS != '"+ Constants.INVALID+"'");
            //一维码
        if(!StringUtils.isEmpty(questionItemDetailsDTO.getFrozenBoxCode1D()))
        {
            sql.append(" and b.FROZEN_BOX_CODE_1D = '"+questionItemDetailsDTO.getFrozenBoxCode1D()+"'");
        }
        //项目编码不为空
        if(!StringUtils.isEmpty(questionItemDetailsDTO.getProjectCode())){
            sql.append(" and t.PROJECT_CODE = '"+questionItemDetailsDTO.getProjectCode()+"'");
        }
        //项目ID
        if(!StringUtils.isEmpty(questionItemDetailsDTO.getProjectId())){
            sql.append(" and t.PROJECT_ID = "+questionItemDetailsDTO.getProjectId()+"");
        }
        //样本类型
        if(questionItemDetailsDTO.getSampleTypeId()!=null){
            sql.append(" and t.SAMPLE_TYPE_ID = "+questionItemDetailsDTO.getSampleTypeId()+"");
        }
        //样本状态
        if(!StringUtils.isEmpty(questionItemDetailsDTO.getStatus())){
            sql.append(" and t.STATUS = '"+questionItemDetailsDTO.getStatus()+"'");
        }
        //冻存盒编码
        if(questionItemDetailsDTO.getFrozenBoxId()!=null){
            sql.append(" and t.FROZEN_BOX_ID = "+questionItemDetailsDTO.getFrozenBoxId()+"");
        }
        //冻存盒编码
        if(!StringUtils.isEmpty(questionItemDetailsDTO.getFrozenBoxCode())){
            sql.append(" and t.FROZEN_BOX_CODE = '"+questionItemDetailsDTO.getFrozenBoxCode()+"'");
        }
        //样本编码
        if(!StringUtils.isEmpty(questionItemDetailsDTO.getSampleCode())){
            sql.append(" and t.SAMPLE_CODE = '"+questionItemDetailsDTO.getSampleCode()+"'");
        }
        //标签
        if(questionItemDetailsDTO.getTags()!=null && questionItemDetailsDTO.getTags().length>0){
            for(String tag :questionItemDetailsDTO.getTags()){
                sql.append(" and (");
                sql.append("  t.TAG1 like '%"+tag+"%'");
                sql.append("  or t.TAG2 like '%"+tag+"%'");
                sql.append("  or t.TAG3 like '%"+tag+"%'");
                sql.append("  or t.TAG4 like '%"+tag+"%'");
                sql.append("  or t.MEMO like '%"+tag+"%'");
                sql.append(" )" );
            }
        }
        Query query = entityManager.createNativeQuery(sql.toString());
        List resultList = query.getResultList();
        if (resultList!=null){
            Iterator<Object> iterator = resultList.iterator();
            while( iterator.hasNext() ){
                Object[] obj  = (Object[])iterator.next();
                QuestionItemDetailsDTO resultDTO = new QuestionItemDetailsDTO();
                resultDTO.setId(obj[0]!=null?Long.valueOf(obj[0].toString()):0L);
                resultDTO.setSampleCode(obj[1]!=null?obj[1].toString():null);
                resultDTO.setSampleTypeName(obj[2]!=null?obj[2].toString():null);
                resultDTO.setSampleClassificationName(obj[3]!=null?obj[3].toString():null);
                resultDTO.setProjectCode(obj[4]!=null?obj[4].toString():null);
                resultDTO.setFrozenBoxId(obj[5]!=null?Long.valueOf(obj[5].toString()):0L);
                resultDTO.setFrozenBoxCode(obj[6]!=null?obj[6].toString():null);
                resultDTO.setTubeRows(obj[7]!=null?obj[7].toString():null);
                resultDTO.setTubeColumns(obj[8]!=null?obj[8].toString():null);
                resultDTO.setTag1(obj[9]!=null&!obj[9].equals("占位符")?obj[9].toString():null);
                resultDTO.setTag2(obj[10]!=null&!obj[10].equals("占位符")?obj[10].toString():null);
                resultDTO.setTag3(obj[11]!=null&!obj[11].equals("占位符")?obj[11].toString():null);
                resultDTO.setTag4(obj[12]!=null&!obj[11].equals("占位符")?obj[12].toString():null);
                resultDTO.setStatus(obj[13]!=null?obj[13].toString():null);
                resultDTO.setSampleTypeId(obj[14]!=null?Long.valueOf(obj[14].toString()):0L);
                resultDTO.setSampleTypeCode(obj[15]!=null?obj[15].toString():null);
                resultDTO.setSampleClassificationId(obj[16]!=null?Long.valueOf(obj[16].toString()):0L);
                resultDTO.setSampleClassificationCode(obj[17]!=null?obj[17].toString():null);
                resultDTO.setMemo(obj[18]!=null&!obj[18].equals("占位符")?obj[18].toString():null);

                result.add(resultDTO);
            }
        }
        List<Long> frozenBoxIds =  new ArrayList<>(new HashSet<>(result.stream().map(s->s.getFrozenBoxId()).collect(Collectors.toList())));
        List<List<Long>> frozenBoxIdsEach1000List = Lists.partition(frozenBoxIds,1000);
        List<FrozenBox> frozenBoxListAll = new ArrayList<>();
        for(List<Long> ids : frozenBoxIdsEach1000List){
            List<FrozenBox> frozenBoxListEach1000 = frozenBoxRepository.findByIdIn(ids);
            frozenBoxListAll.addAll(frozenBoxListEach1000);
        }
        result.forEach(s->{
            FrozenBox frozenBox = frozenBoxListAll.stream().filter(b->b.getId().equals(s.getFrozenBoxId())).findFirst().orElse(null);
            if(frozenBox!=null){
                ArrayList<String> positions = new ArrayList<>();
                if (frozenBox.getEquipmentCode() != null && frozenBox.getEquipmentCode().length() > 0){
                    positions.add(frozenBox.getEquipmentCode());
                }

                if (frozenBox.getAreaCode() != null && frozenBox.getAreaCode().length() > 0) {
                    positions.add(frozenBox.getAreaCode());
                }

                if (frozenBox.getSupportRackCode() != null && frozenBox.getSupportRackCode().length() > 0){
                    positions.add(frozenBox.getSupportRackCode());
                }

                if (frozenBox.getRowsInShelf() != null && frozenBox.getRowsInShelf().length() > 0 && frozenBox.getColumnsInShelf() != null && frozenBox.getColumnsInShelf().length() > 0){
                    positions.add(frozenBox.getColumnsInShelf()+frozenBox.getRowsInShelf());
                }
                s.setPosition(String.join(".", positions));
                s.setFrozenBoxCode1D(frozenBox.getFrozenBoxCode1D());
            }
        });
        return result;
    }
}
