package org.fwoxford.service.impl;

import com.google.common.collect.Lists;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenBox;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.repository.FrozenBoxRepository;
import org.fwoxford.repository.FrozenTubeRepository;
import org.fwoxford.service.FrozenTubeService;
import org.fwoxford.service.dto.QuestionItemDetailsDTO;
import org.fwoxford.service.dto.QuestionSampleData;
import org.fwoxford.service.mapper.FrozenTubeMapper;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
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
            sql.append(" WHERE rownum <=500 and t.STATUS != '"+ Constants.INVALID+"' and (t.frozen_tube_state = '2011' or t.frozen_tube_state='2004') ");
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
        //样本分类
        if(questionItemDetailsDTO.getSampleClassificationId()!=null){
            sql.append(" and t.SAMPLE_CLASSIFICATION_ID = "+questionItemDetailsDTO.getSampleClassificationId()+"");
        }
        //样本状态
        if(!StringUtils.isEmpty(questionItemDetailsDTO.getStatus())){
            sql.append(" and t.STATUS = '"+questionItemDetailsDTO.getStatus()+"'");
        }
        //冻存盒Id
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

    @Override
    public Long countQuestionFrozenTube(QuestionItemDetailsDTO questionItemDetailsDTO) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT count(1) FROM FROZEN_TUBE t " );
        if(!StringUtils.isEmpty(questionItemDetailsDTO.getFrozenBoxCode1D()))
        {
            sql.append("LEFT JOIN FROZEN_BOX b ON b.ID = t.FROZEN_BOX_ID");
        }
        sql.append(" WHERE t.STATUS != '"+ Constants.INVALID+"' and (t.frozen_tube_state = '2011' or t.frozen_tube_state='2004') ");
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
        //样本分类
        if(questionItemDetailsDTO.getSampleClassificationId()!=null){
            sql.append(" and t.SAMPLE_CLASSIFICATION_ID = "+questionItemDetailsDTO.getSampleClassificationId()+"");
        }
        //样本状态
        if(!StringUtils.isEmpty(questionItemDetailsDTO.getStatus())){
            sql.append(" and t.STATUS = '"+questionItemDetailsDTO.getStatus()+"'");
        }
        //冻存盒Id
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
        Long count = query.getSingleResult()!=null?Long.valueOf(query.getSingleResult().toString()):null;
        return count;
    }

    /**
     * 上传问题样本表格
     * @param file
     * @param request
     * @return
     */
    @Override
    public List<QuestionItemDetailsDTO> findQuestionSampleByExcel(MultipartFile file, HttpServletRequest request) {
        List<QuestionItemDetailsDTO> result = new ArrayList<QuestionItemDetailsDTO>();
        List<JSONObject> notExistResult = new ArrayList<JSONObject>();
        List<JSONObject> jsonArray = getExcelValues(file);
        //获取整盒样本(指定冻存盒编码，未指定样本编码)
        List<JSONObject> appointBoxList = new ArrayList<>();
        //指定样本编码
        List<JSONObject> appointSampleList = new ArrayList<>();
        jsonArray.forEach(s->{
            String boxCode = s.getString("boxCode");
            String type = s.getString("type");
            String sampleCode = s.getString("sampleCode");
            String classCode = s.getString("classCode");
            if(!StringUtils.isEmpty(boxCode) && StringUtils.isEmpty(sampleCode)){
                appointBoxList.add(s);
            }else{
                Long boxId = 0L;
                if(!StringUtils.isEmpty(boxCode)){
                    Object frozenBoxId = frozenBoxRepository.findIdByFrozenBoxCodeAndSampleType(boxCode,type);
                    boxId = frozenBoxId!=null?Long.valueOf(frozenBoxId.toString()):0L;
                }
                s.put("boxId",boxId);
                appointSampleList.add(s);
            }
        });
        //查询整盒样本
        Map<String, List<JSONObject>> boxListGroupByType =
            appointBoxList.stream().collect(Collectors.groupingBy(w -> w.getString("type")+"&"+w.getString("classCode")));
        for(String key :boxListGroupByType.keySet()) {
            List<JSONObject> boxCodeListGroupByType = boxListGroupByType.get(key);
            int i = key.split("&").length;
            String type = key.split("&")[0];
            String classCode = i == 2 ? key.split("&")[1] : null;
            List<String> codeList = new ArrayList<>();
            boxCodeListGroupByType.forEach(s -> codeList.add(s.getString("boxCode")));
            List<List<String>> boxCodeListEach1000 = Lists.partition(codeList, 1000);
            for (List<String> code : boxCodeListEach1000) {
                //整盒出库可以指定到某一个冻存盒，所以可以获取指定冻存盒的Id
                List<FrozenTube> sampleList = frozenTubeRepository.findByFrozenBoxCode1DInOrFrozenBoxCodeInAndSampleTypeAndSampleClassification(code, type, classCode);
                List<QuestionItemDetailsDTO> questionItemDetailsDTOS = frozenTubeMapper.frozenTubesToQuestionSamples(sampleList);
                result.addAll(questionItemDetailsDTOS);
                boxCodeListGroupByType.forEach(s -> {
                    Boolean isExists = sampleList.stream().anyMatch(t ->
                        t.getFrozenBoxCode().equals(s.getString("boxCode")) ||
                            (t.getFrozenBox().getFrozenBoxCode1D()!=null && t.getFrozenBox().getFrozenBoxCode1D().equals(s.getString("boxCode"))));
                    if(isExists == false){
                        notExistResult.add(s);
                    }
                });
            }
        }
        //指定样本
        Map<String,List<JSONObject>> requirementGroupBySampleType =
            appointSampleList.stream().collect(Collectors.groupingBy(w -> w.getString("type")+"&"+w.getString("classCode")+"&"+w.getString("boxId")));
        for(String key:requirementGroupBySampleType.keySet()){
            String sampleType = key.split("&")[0];
            String classCode = key.split("&")[1];
            Long frozenBoxId = !key.split("&")[2].equals("null")&&!key.split("&")[2].equals(null)?Long.valueOf(key.split("&")[2]):0L;

            List<JSONObject> stockOutRequiredSampleListEachSampleType = requirementGroupBySampleType.get(key);
            //每次取1000支
            List<List<JSONObject>> arrReqSamples = Lists.partition(stockOutRequiredSampleListEachSampleType, 1000);
            for(int index = 0; index < arrReqSamples.size(); index += 10){
                int start = index;
                String[] s1 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getString("sampleCode")).toArray(s->new String[s]);
                String[] s2 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getString("sampleCode")).toArray(s->new String[s]);
                String[] s3 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getString("sampleCode")).toArray(s->new String[s]);
                String[] s4 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getString("sampleCode")).toArray(s->new String[s]);
                String[] s5 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getString("sampleCode")).toArray(s->new String[s]);
                String[] s6 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getString("sampleCode")).toArray(s->new String[s]);
                String[] s7 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getString("sampleCode")).toArray(s->new String[s]);
                String[] s8 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getString("sampleCode")).toArray(s->new String[s]);
                String[] s9 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getString("sampleCode")).toArray(s->new String[s]);
                String[] s10 = arrReqSamples.size() <= start ? new String[]{"N/A"} : arrReqSamples.get(start++).stream().map(d->d.getString("sampleCode")).toArray(s->new String[s]);
                List<FrozenTube> frozenTubeList = frozenTubeRepository.
                    findBySampleTypeCodeAndSampleCodeInAndFrozenBoxIdAndSampleClassificationCode(
                        sampleType
                        , Arrays.asList(s1)
                        , Arrays.asList(s2)
                        , Arrays.asList(s3)
                        , Arrays.asList(s4)
                        , Arrays.asList(s5)
                        , Arrays.asList(s6)
                        , Arrays.asList(s7)
                        , Arrays.asList(s8)
                        , Arrays.asList(s9)
                        , Arrays.asList(s10)
                        , frozenBoxId,classCode
                    );
                int countOfAppointSampleSize = 0;
                if(!Arrays.asList(s1).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s1).size();
                }
                if(!Arrays.asList(s2).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s2).size();
                }
                if(!Arrays.asList(s3).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s3).size();
                }
                if(!Arrays.asList(s4).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s4).size();
                }
                if(!Arrays.asList(s5).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s5).size();
                }
                if(!Arrays.asList(s6).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s6).size();
                }
                if(!Arrays.asList(s7).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s7).size();
                }
                if(!Arrays.asList(s8).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s8).size();
                }
                if(!Arrays.asList(s9).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s9).size();
                }
                if(!Arrays.asList(s10).contains("N/A")){
                    countOfAppointSampleSize+=Arrays.asList(s10).size();
                }
                if(frozenTubeList.size()>countOfAppointSampleSize){
                    frozenTubeList = frozenTubeList.subList(0,countOfAppointSampleSize);
                }

                result.addAll(frozenTubeMapper.frozenTubesToQuestionSamples(frozenTubeList));
                final List<FrozenTube> frozenTubes = frozenTubeList;
                arrReqSamples.forEach(sampleEach1000->{
                    sampleEach1000.forEach(sp->{
                        Boolean isExists= null;
                        if(!StringUtils.isEmpty(sp.getString("boxCode"))){
                            isExists= frozenTubes.stream().anyMatch(t->
                                (sp.getString("boxCode")).equals(t.getFrozenBoxCode())
                                    || (t.getFrozenBox().getFrozenBoxCode1D()!=null && t.getFrozenBox().getFrozenBoxCode1D().equals(sp.getString("boxCode")))
                                    && t.getSampleCode().equals(sp.getString("sampleCode")));
                        }else{
                            isExists= frozenTubes.stream().anyMatch(t->t.getSampleCode().equals(sp.getString("sampleCode")));
                        }
                        if(isExists == false){
                            notExistResult.add(sp);
                        }
                    });

                });
            }
        }
        if(notExistResult.size()>0){
            throw new BankServiceException("上传数据中有不存在于库存中的样本，请确认后重新上传！",notExistResult.toString());
        }

        return result;
    }

    private List<JSONObject> getExcelValues(MultipartFile file) {
        List<JSONObject> jsonArray = new ArrayList<>();
        if(file == null){
            return jsonArray;
        }
        try {
            InputStream input =  new ByteArrayInputStream(file.getBytes());
            ArrayList<ArrayList<Object>> arrayLists = ExcelUtils.readExcel("xlsx",input);
            List<ArrayList<Object>>  arrayListArrayList = arrayLists.subList(1,arrayLists.size());
            for(List arrayList :arrayListArrayList){
                int i = arrayList.size();
                if(arrayList.size()<3){
                    continue;
                }
                Object boxCode =  arrayList.get(0);
                Object sampleCode =  arrayList.get(1);
                Object sampleType =  arrayList.get(2);
                Object sampleClass = null;
                if(StringUtils.isEmpty(boxCode) &&StringUtils.isEmpty(sampleCode) ){
                    continue;
                }
                if(i>3){
                    sampleClass = arrayList.get(3);
                }
                if(StringUtils.isEmpty(sampleType)){
                    throw new BankServiceException("冻存盒编码为："+boxCode+"，样本编码为："+sampleCode+"。未指定样本类型！不能上传！");
                }
                if(! StringUtils.isEmpty(sampleCode) && StringUtils.isEmpty(sampleClass)){
                    throw new BankServiceException("冻存盒编码为："+boxCode+"，样本编码为："+sampleCode+"。未指定样本分类！不能上传！");
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("boxCode",boxCode);
                jsonObject.put("sampleCode",sampleCode);
                jsonObject.put("type",sampleType);
                jsonObject.put("classCode",sampleClass);
                jsonArray.add(jsonObject);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return jsonArray;
    }
}
