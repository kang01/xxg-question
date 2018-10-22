package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Spring Data JPA repository for the FrozenTube entity.
 */
@SuppressWarnings("unused")
public interface FrozenTubeRepository extends JpaRepository<FrozenTube,Long> {

    @Query("select t from FrozenTube t where t.frozenBox.id = ?1 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+ Constants.FROZEN_BOX_INVALID+"'")
    List<FrozenTube> findFrozenTubeListByBoxId(Long frozenBoxId);

    @Query("select t from FrozenTube t where t.frozenBox.id in ?1 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+ Constants.FROZEN_BOX_INVALID+"'")
    List<FrozenTube> findFrozenTubeListByBoxIdIn(List<Long> frozenBoxId);

    @Query("select t from FrozenTube t where t.frozenBoxCode = ?1 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+ Constants.FROZEN_BOX_INVALID+"'")
    List<FrozenTube> findFrozenTubeListByBoxCode(String frozenBoxCode);

    @Query("select count(t.id) from FrozenTube t where t.frozenBoxCode = ?1 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+ Constants.FROZEN_BOX_INVALID+"'")
    Long countFrozenTubeListByBoxCode(String frozenBoxCode);

    List<FrozenTube> findFrozenTubeListByFrozenBoxCodeAndStatus(String frozenBoxCode, String status);

    @Query(value = "select t.frozen_box_code as frozenBoxCode,count(t.frozen_box_code) as sampleNumber \n" +
        "from frozen_tube t where t.frozen_box_code in ?1 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+ Constants.FROZEN_BOX_INVALID+"' group by t.frozen_box_code \n" +
        " order by sampleNumber asc,t.frozen_box_code desc " ,nativeQuery = true)
    List<Object[]> countSampleNumberByfrozenBoxList(List<String> frozenBoxList);

    int countByFrozenBoxCodeAndStatus(String frozenBoxCode, String status);

    @Query(value = "select count(1) from frozen_tube t where t.frozen_box_id in ?1 and t.status=?2" ,nativeQuery = true)
    int countByFrozenBoxCodeStrAndStatus(List<Long> boxIds, String status);

    @Query(value = "select t.sample_code,count(t.sample_code) as noo from frozen_tube t " +
        " where t.frozen_box_id in ?1 and t.status!='"+ Constants.INVALID+"' " +
        " GROUP BY t.sample_code " ,nativeQuery = true)
    List<Object[]> countByFrozenBoxCodeStrAndGroupBySampleCode(List<Long> boxIds);

    @Query("select t from FrozenTube t where (t.sampleCode in ?1 ) and t.projectCode = ?2 and t.sampleType.id = ?3 and t.status != ?4")
    List<FrozenTube> findBySampleCodeInAndProjectCodeAndSampleTypeIdAndStatusNot(List<String> sampleCode, String projectCode, Long sampleTypeId, String status);

    @Query("select t from FrozenTube t where t.sampleCode =?1  and t.projectCode = ?2 and t.sampleTypeCode =?3 and t.sampleClassification.sampleClassificationCode = ?4 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"')")
    FrozenTube findBySampleCodeAndProjectCodeAndSampleTypeCodeAndSampleClassificationCode(String sampleCode, String projectCode, String sampleTypeCode, String sampleClassTypeCode);


    @Query("select t from FrozenTube t where (t.sampleCode =?1 or t.sampleTempCode =?1)  and t.projectCode = ?2 and t.sampleType.id =?3 and t.sampleClassification.id = ?4 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"')")
    List<FrozenTube> findBySampleCodeAndProjectCodeAndSampleTypeIdAndSampleClassitionId(String sampleCode, String projectCode, Long sampleTypeId, Long sampleClassificationId);

    @Query("select t from FrozenTube t where (t.frozenBoxCode =?1 or t.frozenBox.frozenBoxCode1D =?1)  and t.tubeRows = ?2 and t.tubeColumns =?3 " +
        " and t.status != ?4" )
    FrozenTube findByFrozenBoxCodeAndTubeRowsAndTubeColumnsAndStatusNot(String frozenBoxCode, String tubeRows, String tubeColumns, String status);

    @Query("select t from FrozenTube t where t.frozenBox.equipmentCode =?1  and t.frozenBox.areaCode = ?2" +
        " and t.frozenBox.supportRackCode =?3 and  t.frozenBox.status = '"+ Constants.FROZEN_BOX_STOCKED+"' and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"')")
    List<FrozenTube> findByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String supportRackCode);

    @Query("select t from FrozenTube t where (t.sampleCode =?1 or t.sampleTempCode =?1)  and t.projectCode = ?2 and t.frozenBox.id !=?3 and t.sampleType.id = ?4 and t.sampleClassification.id = ?5 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"')")
    List<FrozenTube> findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleTypeAndSampleClassifacition(String sampleCode, String projectCode, Long frozenBoxId, Long sampleTypeId, Long sampleClassificationId);

    @Query("select t from FrozenTube t where (t.sampleCode =?1 or t.sampleTempCode =?1)  and t.projectCode = ?2 and t.frozenBox.id !=?3 and t.sampleType.id = ?4  and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"')")
    List<FrozenTube> findFrozenTubeBySampleCodeAndProjectAndfrozenBoxAndSampleType(String sampleCode, String projectCode, Long frozenBoxId, Long sampleTypeId);

    @Modifying
    @Query("update FrozenTube t set t.frozenTubeState = ?1  where t.frozenBoxCode in ?2 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_BOX_INVALID+"')")
    void updateFrozenTubeStateByFrozenBoxCodes(String status, List<String> frozenBoxCodes);

    @Query("select count(t) from FrozenTube t where t.frozenBoxCode = ?1 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"') and t.frozenBox.status !='"+ Constants.FROZEN_BOX_INVALID+"' and t.frozenTubeState=?2")
    Long countByFrozenBoxCodeAndFrozenTubeState(String frozenBoxCode, String frozenTubeState);

    @Query(value = "select t.* from frozen_tube t where t.sample_code=?1 and t.sample_type_code = ?2 " +
        "  " ,nativeQuery = true)
    FrozenTube findBySampleCodeAndSampleTypeCode(String sampleCode, String sampleTypeCode);

    @Query(value = "select * from frozen_tube t where t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"' and t.status!='"+ Constants.INVALID+"'" +
        " and (t.sample_code in ?3" +
        " or t.sample_code in ?4" +
        " or t.sample_code in ?5" +
        " or t.sample_code in ?6" +
        " or t.sample_code in ?7" +
        " or t.sample_code in ?8" +
        " or t.sample_code in ?9" +
        " or t.sample_code in ?10" +
        " or t.sample_code in ?11" +
        " or t.sample_code in ?12" +
        " ) " +
        " and t.project_id in ?2 and t.sample_type_code = ?1"
        ,nativeQuery = true)
    List<FrozenTube> findBySampleCodeInAndSampleTypeCodeAndProjectIn(
        String appointedSampleType, List<Long> projectIds
        , List<String> sampleCodeList1
        , List<String> sampleCodeList2
        , List<String> sampleCodeList3
        , List<String> sampleCodeList4
        , List<String> sampleCodeList5
        , List<String> sampleCodeList6
        , List<String> sampleCodeList7
        , List<String> sampleCodeList8
        , List<String> sampleCodeList9
        , List<String> sampleCodeList10
    );

    @Query("SELECT t from FrozenTube t where t.frozenBoxCode = ?1 and t.frozenTubeState = ?2 and t.status !='"+ Constants.INVALID+"'")
    List<FrozenTube> findByFrozenBoxCodeAndFrozenTubeState(String frozenBoxCode, String frozenTubeState);

    @Query(value = "select t.sample_code,t.sample_type_code,b.id from frozen_tube t left join frozen_box b on t.frozen_box_id = b.id " +
        " where (b.frozen_box_code_1d in ?1 " +
        " or b.frozen_box_code in ?1)" +
        "and t.sample_type_code = ?2 and t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"' ",nativeQuery = true)
    List<Object[]> findByFrozenBoxCode1DInAndSampleType(List<String> boxCodeListEach1000, String type);

    @Query(value = "select count(1) from frozen_tube t left join frozen_box b on t.frozen_box_id = b.id " +
        " where (b.frozen_box_code_1d in ?1 " +
        " or b.frozen_box_code in ?1 )" +
        "and t.sample_type_code = ?2 and t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"'",nativeQuery = true)
    Long countByFrozenBoxCode1DInAndSampleType(List<String> boxCodeListEach1000, String type);

    @Query(value = "select count(1) from frozen_tube t left join frozen_box b on t.frozen_box_id = b.id " +
        " where (b.frozen_box_code_1d in ?1 " +
        " or b.frozen_box_code in ?1 )" +
        "and t.sample_type_code = ?2 and t.sample_classification_code = ?3 and t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"'",nativeQuery = true)
    Long countByFrozenBoxCode1DInAndSampleTypeAndSampleClassification(List<String> boxCodeListEach1000, String type, String classCode);

    List<FrozenTube> findByFrozenBoxCodeIn(List<String> frozenBoxCodeStr);

    //只用于测试，不能用于业务
    @Query(value = "  select cast(s.tranship_code as varchar2(255)) as transhipCode," +
        "cast(t.frozen_box_code as varchar2(255)) as frozenBoxCode,count(1) " +
        "from (select * from frozen_tube where project_code = ?1 and frozen_tube_state='"+ Constants.FROZEN_BOX_TRANSHIP_COMPLETE+"') t \n" +
        "        left join (select * from tranship_tube ) f on f.frozen_tube_id = t.id  " +
        "         left join tranship_box tb on  f.tranship_box_id = tb.id  " +
        "         left join tranship s on  tb.tranship_id = s.id  " +
        "    where f.id is not null group by s.tranship_code,t.frozen_box_code" , nativeQuery = true)
    List<Object[]> countTubeByProjectCodeGroupByTranshipCode(String projectCode);

    @Query(value = "SELECT T.FROZEN_BOX_ID,COUNT(T.ID) AS NOO FROM FROZEN_TUBE T LEFT JOIN FROZEN_BOX B ON B.ID = T.FROZEN_BOX_ID WHERE T.FROZEN_BOX_ID IN ?1  " +
        " AND T.STATUS not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"')" +
        " AND T.FROZEN_TUBE_STATE = B.STATUS" +
        " GROUP BY T.FROZEN_BOX_ID ",nativeQuery = true)
    List<Object[]> countGroupByFrozenBoxId(List<Long> boxIds);

    @Query("select t from FrozenTube t where (t.sampleCode in ?1 or t.sampleTempCode in ?1) and t.projectCode=?2 and t.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_TUBE_DESTROY+"')")
    List<FrozenTube> findBySampleCodeInAndProjectCode(List<String> sampleCodeStr, String projectCode);

    @Query("select t from FrozenTube t where t.frozenBoxCode in ?1 and t.frozenTubeState = '"+ Constants.FROZEN_BOX_STOCKED+"' and  t.status not in (?2,"+ Constants.FROZEN_TUBE_DESTROY+")")
    List<FrozenTube> findByFrozenBoxCodeInAndStatusNotForInStocked(List<String> boxCodeStr, String status);

    @Query(value = "select t.sample_temp_code,count(t.sample_temp_code) as noo from frozen_tube t " +
        " where t.frozen_box_id in ?1 and t.status!='"+ Constants.INVALID+"' and t.sample_code is null " +
        " GROUP BY t.sample_temp_code" ,nativeQuery = true)
    List<Object[]> countByFrozenBoxCodeStrAndGroupBySampleTempCode(List<Long> boxIds);

    @Query(value = "select count(t)  from FrozenTube t where t.frozenBox.id = ?1 and t.status!= ?2 and t.frozenTubeState = t.frozenBox.status" )
    Long countByFrozenBoxIdAndStatusNot(Long id, String status);

    @Modifying
    @Query("update FrozenTube b set b.status='"+ Constants.INVALID+"' where b.id not in ?1 and b.frozenTubeState in ('"+ Constants.FROZEN_BOX_NEW+"','"+ Constants.FROZEN_BOX_STOCKING+"') and b.frozenBox.id = ?2")
    void updateStatusByFrozenBoxIdNotInAndFrozenTubeStateAndFrozenBox(List<Long> frozenTubeIdsOld, Long frozenBoxId);

    @Query(value = "select * from frozen_tube t where t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"' " +
        " and t.status!='"+ Constants.INVALID+"'" +
        " and (t.sample_code in ?3" +
        " or t.sample_code in ?4" +
        " or t.sample_code in ?5" +
        " or t.sample_code in ?6" +
        " or t.sample_code in ?7" +
        " or t.sample_code in ?8" +
        " or t.sample_code in ?9" +
        " or t.sample_code in ?10" +
        " or t.sample_code in ?11" +
        " or t.sample_code in ?12" +
        " ) " +
        " and t.project_id in ?2 and t.sample_type_code = ?1 " +
        " and (?13 = 0  or t.frozen_box_id = ?13)"
        ,nativeQuery = true)
    List<FrozenTube> findBySampleTypeCodeAndProjectInAndSampleCodeInAndFrozenBoxId(
        String sampleType, List<Long> projectIds, List<String> strings, List<String> strings1, List<String> strings2, List<String> strings3,
        List<String> strings4, List<String> strings5, List<String> strings6, List<String> strings7, List<String> strings8, List<String> strings9, Long frozenBoxId);

    @Query(value = "select * from frozen_tube t where (t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"' or t.frozen_tube_state = '"+ Constants.FROZEN_BOX_TRANSHIP_COMPLETE+"') " +
        " and t.status!='"+ Constants.INVALID+"'" +
        " and ( t.sample_code in ?2 " +
        " or t.sample_code in ?3" +
        " or t.sample_code in ?4" +
        " or t.sample_code in ?5" +
        " or t.sample_code in ?6" +
        " or t.sample_code in ?7" +
        " or t.sample_code in ?8" +
        " or t.sample_code in ?9" +
        " or t.sample_code in ?10" +
        " or t.sample_code in ?11" +
        " ) " +
        " and t.sample_type_code = ?1  and (?13 is null or t.sample_classification_code = ?13)" +
        " and (?12 = 0  or t.frozen_box_id = ?12)"
        ,nativeQuery = true)
    List<FrozenTube> findBySampleTypeCodeAndSampleCodeInAndFrozenBoxIdAndSampleClassificationCode(
        String sampleType,  List<String> strings, List<String> strings1, List<String> strings2, List<String> strings3,
        List<String> strings4, List<String> strings5, List<String> strings6, List<String> strings7, List<String> strings8, List<String> strings9, Long frozenBoxId, String sampleClassificationCode);

    @Query(value = "select t.* from frozen_tube t left join frozen_box b on t.frozen_box_id = b.id " +
        " where t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"' and  t.sample_type_code = ?2  and (b.frozen_box_code_1d in ?1 " +
        " or b.frozen_box_code in ?1)" +
        "and t.project_id in ?3 ",nativeQuery = true)
    List<FrozenTube> findByFrozenBoxCode1DInOrFrozenBoxCodeInAndSampleTypeAndProjectIdsIn(List<String> code, String type, List<Long> projectIds);

    @Query(value = "select t.* from frozen_tube t left join frozen_box b on t.frozen_box_id = b.id " +
        " where t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"' and  t.sample_type_code = ?2 " +
        "and  (?3 is null or t.sample_classification_code = ?3 )and (b.frozen_box_code_1d in ?1 " +
        " or b.frozen_box_code in ?1)",nativeQuery = true)
    List<FrozenTube> findByFrozenBoxCode1DInOrFrozenBoxCodeInAndSampleTypeAndSampleClassification(List<String> code, String type, String classCode);


    List<FrozenTube> findBySampleCodeInAndStatusNot(List<String> sampleCodeStr, String invalid);

    @Modifying
    @Query("update FrozenTube t set t.status = ?1  where t.frozenBox.id = ?2 and t.frozenTubeState = '"+ Constants.FROZEN_BOX_RETURN_BACK+"'")
    void updateStatusByFrozenBoxId(String status, Long id);

    @Query("select count(1) from FrozenTube t where t.projectSite.id = ?1 and t.status not in ('"+ Constants.INVALID+"') ")
    Long countByProjectSiteId(Long id);

    @Query(value = "select t.id from frozen_tube t where t.project_site_id = ?1 and t.status not in ('"+ Constants.INVALID+"') and rownum = 1" ,nativeQuery = true)
    Object findLimitOneByProjectSiteId(Long id);

    @Query("select count(t.id) from FrozenTube t where t.sampleClassification.id = ?1 and t.status not in ('"+ Constants.INVALID+"')")
    Long countBySampleClassificationId(Long id);

    @Query(value = "select * from frozen_tube t where t.frozen_box_id = ?1 and t.sample_code = ?2 and (t.tube_rows||t.tube_columns)=?3" , nativeQuery = true)
    FrozenTube findByFrozenBoxIdAndSampleCodeAndPos(Long boxId, String sampleCode, String pos);

    @Query("SELECT t from FrozenTube t where t.id in ?1 and t.status != '0000' and t.frozenTubeState = ?2")
    List<FrozenTube> findByIdInAndFrozenTubeState(List<Long> frozenTubeIds, String state);

    @Query("SELECT t from FrozenTube t where t.id in ?1 and t.status != '0000'")
    List<FrozenTube> findByIdIn(List<Long> frozenTubeIds);


    @Query(value = "select sample_type_id,sample_classification_id,count(1) from frozen_tube where status != '0000' and project_id = ?1 group by sample_type_id,sample_classification_id ",nativeQuery = true)
    List<Object[]> countSampleGroupBySampleTypeIdAndSampleClassificationId(Long projectId);

    @Query(value = "select id from frozen_tube where status != '0000' and project_id = ?1 and sample_classification_id = ?2 and rownum = 1",nativeQuery = true)
    Object findOneSampleByProjectIdAndSampleClassificationId(Long projectId, Long sampleClassificationId);
    @Query( "select t from FrozenTube t where t.status != '0000' and t.frozenBox.frozenBoxCode1D = ?1")
    List<FrozenTube> findByFrozenBoxCode1D(String frozenBoxCode1D);

    List<FrozenTube> findListBySampleCodeAndSampleTypeCode(String sampleCode, String sampleTypeCode);

    @Query("SELECT COUNT(t.id) FROM FrozenTube t WHERE t.sampleType.id = ?1 and t.status!='"+ Constants.INVALID+"'")
    Long countBySampleTypeId(Long id);
    @Query(value = "select * from ( " +
        "select t.* from  frozen_tube t " +
        "         where t.frozen_box_code = ?1 and t.frozen_tube_state = ?2" +
        "         order by t.frozen_box_code,t.tube_rows,LPAD(t.tube_columns,2) asc) where ROWNUM=1",nativeQuery = true)
    FrozenTube findByFrozenBoxCodeAndFrozenTubeStateTopOne(String boxCode2, String frozenBoxStocked);

    @Query( value = "select t.id from frozen_tube t where t.status != '0000' and t.frozen_box_id = ?1 and rownum = 1" ,nativeQuery = true)
    Long findByFrozenBoxId(Long id);

    @Query( value = "select t.* from frozen_tube t where t.frozen_box_code = ?1 and t.frozen_tube_state = ?2 and t.sample_classification_code = ?3 and rownum = 1" ,nativeQuery = true)
    FrozenTube findByFrozenBoxCodeAndFrozenTubeStateAndSampleClassificationCodeTopOne(String boxCode2, String frozenBoxStocked, String classCode);

    @Query( value = "select t.* from frozen_tube t where t.sample_code in ?1 and t.frozen_tube_state = ?2 and t.sample_classification_code = ?3" ,nativeQuery = true)
    List<FrozenTube> findBySampleCodeInAndFrozenTubeStateAndSampleClassificationCode(List<String> sampleCodes, String frozenBoxStocked, String classCode);

    List<FrozenTube> findFrozenTubeListByFrozenBoxId(Long id);

    List<FrozenTube> findBySampleCodeInAndSampleTypeCode(List<String> sampleCodeList, String sampleTypeCode);

    FrozenTube findBySampleCodeAndSampleClassificationCode(String sampleCode, String sampleClassificationCode);

    List<FrozenTube> findBySampleCode(String sampleCode);

    @Modifying
    @Query("update FrozenTube t set t.bussinessId = null,t.bussinessType = null,t.lockFlag = 0  where t.bussinessId in ?1 and t.status!='"+Constants.INVALID+"'")
    void updateBussinessMsgByBussinessId(List<Long> bussinessIds);
}
