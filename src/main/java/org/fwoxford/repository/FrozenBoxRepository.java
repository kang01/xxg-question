package org.fwoxford.repository;

import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Data JPA repository for the FrozenBox entity.
 */
@SuppressWarnings("unused")
public interface FrozenBoxRepository extends JpaRepository<FrozenBox,Long> {
    @Query(value = "select t.* from frozen_box t left join tranship_box p on t.id=p.frozen_box_id where p.tranship_id=?1 and t.status!='"+ Constants.FROZEN_BOX_INVALID+"' and t.status!='"+ Constants.FROZEN_BOX_INVALID+"'",nativeQuery = true)
    List<FrozenBox> findAllFrozenBoxByTranshipId(Long transhipId);

    @Query("select box from FrozenBox box where box.frozenBoxCode = ?1 and box.status!='"+ Constants.INVALID+"' and box.status!='"+ Constants.FROZEN_BOX_INVALID+"'" )
    FrozenBox findFrozenBoxDetailsByBoxCode(String frozenBoxCode);

    @Query(value = "select * from frozen_box box where box.equipment_id = ?1" +
        " and box.area_id = ?2 " +
        " and box.support_rack_id = ?3 " +
        " and box.columns_in_shelf = ?4 " +
        " and box.rows_in_shelf = ?5 and box.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_BOX_SPLITED+"','"+ Constants.FROZEN_BOX_INVALID+"','"+ Constants.FROZEN_BOX_STOCK_OUT_COMPLETED+"','"+ Constants.FROZEN_BOX_STOCK_OUT_HANDOVER+"')" , nativeQuery = true)
    List<FrozenBox> findByEquipmentIdAndAreaIdAndSupportIdAndColumnAndRow(Long equipmentId, Long areaId, Long supportRackId, String column, String row);

    @Modifying
    @Query("update FrozenBox b set b.status=?2 where b.frozenBoxCode=?1")
    void updateStatusByFrozenBoxCode(String frozenBoxCode, String status);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.status in ('"+ Constants.FROZEN_BOX_STOCKED+"','"+ Constants.FROZEN_BOX_PUT_SHELVES+"') ")
    List<FrozenBox> findByEquipmentCode(String equipmentCode);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2 and box.status  in ('"+ Constants.FROZEN_BOX_PUT_SHELVES+"','"+ Constants.FROZEN_BOX_STOCKED+"')")
    List<FrozenBox> findByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);

    @Query("select count(box) from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2 and box.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_BOX_SPLITED+"','"+ Constants.FROZEN_BOX_INVALID+"','"+ Constants.FROZEN_BOX_STOCK_OUT_COMPLETED+"','"+ Constants.FROZEN_BOX_STOCK_OUT_HANDOVER+"')")
    Long countByEquipmentCodeAndAreaCode(String equipmentCode, String areaCode);

    @Query("select box from FrozenBox box where box.equipmentCode = ?1 and box.areaCode = ?2  and box.supportRackCode = ?3 " +
        " and box.columnsInShelf = ?4 and box.rowsInShelf = ?5" +
        " and box.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_BOX_SPLITED+"','"+ Constants.FROZEN_BOX_INVALID+"','"+ Constants.FROZEN_BOX_STOCK_OUT_COMPLETED+"','"+ Constants.FROZEN_BOX_STOCK_OUT_HANDOVER+"')")
    FrozenBox findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(String equipmentCode, String areaCode, String shelfCode, String columnsInShelf, String rowsInShelf);

    @Query(value = "select f.* from frozen_box f where f.equipment_code = ?1 and f.area_code =?2" +
        " and f.support_rack_code = ?3" +
        " and f.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_BOX_SPLITED+"','"+ Constants.FROZEN_BOX_INVALID+"','"+ Constants.FROZEN_BOX_STOCK_OUT_COMPLETED+"','"+ Constants.FROZEN_BOX_STOCK_OUT_HANDOVER+"','"+ Constants.FROZEN_BOX_DESTROY+"')" ,nativeQuery = true)
    List<FrozenBox> findByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String shelfCode);

    @Query(value = "select count(f.id) from frozen_box f where f.equipment_code = ?1 and f.area_code =?2" +
        " and f.support_rack_code = ?3" +
        " and f.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_BOX_SPLITED+"','"+ Constants.FROZEN_BOX_INVALID+"','"+ Constants.FROZEN_BOX_STOCK_OUT_COMPLETED+"','"+ Constants.FROZEN_BOX_STOCK_OUT_HANDOVER+"')" ,nativeQuery = true)
    Long countByEquipmentCodeAndAreaCodeAndSupportRackCode(String equipmentCode, String areaCode, String shelfCode);

    List<FrozenBox> findByProjectCodeAndSampleTypeCodeAndStatus(String projectCode, String sampleTypeCode, String status);

    @Query("SELECT t FROM FrozenBox t WHERE t.frozenBoxCode in ?1 AND t.status in ?2 ORDER BY t.createdDate")
    List<FrozenBox> findByFrozenBoxCodeInAndStatusIn(List<String> frozenBoxCodeStr, List<String> statusStr);

    @Query(value = "select f.* from frozen_box f where f.frozen_box_code in ?1 and f.status!='"+ Constants.FROZEN_BOX_INVALID+"' and f.status!='"+ Constants.INVALID+"'" ,nativeQuery = true)
    List<FrozenBox> findByFrozenBoxCodeIn(List<String> frozenBoxCodeStr);


    FrozenBox findBySupportRackIdAndColumnsInShelfAndRowsInShelf(Long id, String columnsInShelf, String rowsInShelf);

    List<FrozenBox> findProjectByEquipmentId(Long id);

    @Modifying
    @Query("update FrozenBox b set b.status=?1 where b.frozenBoxCode in ?2 and b.status not in ('"+ Constants.FROZEN_BOX_INVALID+"','"+ Constants.INVALID+"')")
    void updateStatusByFrozenBoxCodes(String frozenBoxTranshipComplete, List<String> frozenBoxCodes);

    @Query(value = "SELECT ROWNUM  as id," +
        " cast(temp.equipment_code as varchar2(255)) as equipment_code, " +
        " cast(temp.area_code as varchar2(255)) as area_code, " +
        " cast(temp.support_rack_code as varchar2(255)) as support_rack_code, " +
        " cast(temp.rows_in_shelf as varchar2(255)) as rows_in_shelf, " +
        " cast(temp.columns_in_shelf as varchar2(255)) as columns_in_shelf, " +
        " equipment_id,area_id,support_rack_id, " +
        " cast(temp.frozen_box_code as varchar2(255)) as frozen_box_code, " +
        " frozen_box_id,project_id, " +
        " cast(temp.project_code as varchar2(255)) as project_code, " +
        " project_site_id,sample_type_id, " +
        " cast(temp.sample_type_code as varchar2(255)) as sample_type_code, "+
        " cast(temp.sample_type_name as varchar2(255)) as sample_type_name, "+
        " sample_classification_id, " +
        " cast(temp.sample_classification_code as varchar2(255)) as sample_classification_code, "+
        " cast(temp.sample_classification_name as varchar2(255)) as sample_classification_name, "+
        " created_date,type " +
        " FROM" +
        "                (" +
        "                    SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.project_id,t.project_code,t.project_site_id,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,104 as type" +
        "                    FROM position_move_record t where t.move_type in (1,2) and frozen_box_id = ?1" +
        "        " +
        "                    UNION" +
        "           " +
        "                    SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.project_id,t.project_code,t.project_site_id,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,105 as type" +
        "                    FROM position_change_record t where t.change_type in (1,2) and frozen_box_id = ?1" +
        "            " +
        "                    UNION" +
        "            " +
        "                    SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.project_id,t.project_code,t.project_site_id,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,106 as type" +
        "                    FROM position_destroy_record t where t.destroy_type in (1,2) and frozen_box_id = ?1" +
        "            " +
        "            ) temp ORDER BY created_date DESC",nativeQuery = true)
    List<Object[]> findPositionHistory(Long id);

    @Query(value = "SELECT ROWNUM  as id," +
        " cast(temp.equipment_code as varchar2(255)) as equipment_code, " +
        " cast(temp.area_code as varchar2(255)) as area_code, " +
        " cast(temp.support_rack_code as varchar2(255)) as support_rack_code, " +
        " cast(temp.rows_in_shelf as varchar2(255)) as rows_in_shelf, " +
        " cast(temp.columns_in_shelf as varchar2(255)) as columns_in_shelf, " +
        " equipment_id,area_id,support_rack_id, " +
        " cast(temp.frozen_box_code as varchar2(255)) as frozen_box_code, " +
        " frozen_box_id,project_id, " +
        " cast(temp.project_code as varchar2(255)) as project_code, " +
        " project_site_id,sample_type_id, " +
        " cast(temp.sample_type_code as varchar2(255)) as sample_type_code, "+
        " cast(temp.sample_type_name as varchar2(255)) as sample_type_name, "+
        " sample_classification_id, " +
        " cast(temp.sample_classification_code as varchar2(255)) as sample_classification_code, "+
        " cast(temp.sample_classification_name as varchar2(255)) as sample_classification_name, "+
        " created_date,type " +
        " FROM" +
        "                (" +
        "                    select t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.project_id,t.project_code,t.project_name,t.project_site_id,t.project_site_name,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,101 as type" +
        "                    from tranship_box t where frozen_box_id = ?1" +
        "                    UNION" +
        "                    SELECT t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.project_id,t.project_code,t.project_name,t.project_site_id,t.project_site_name,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,102 as type" +
        "                    FROM stock_in_box t where frozen_box_id = ?1 and t.status = '2004'" +
        "                    UNION" +
        "                    SELECT t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.project_id,t.project_code,t.project_name,t.project_site_id,t.project_site_name,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,103 as type" +
        "                    FROM stock_out_box t where frozen_box_id = ?1" +
        "            ) temp ORDER BY created_date DESC",nativeQuery = true)
    List<Object[]> findFrozenBoxHistory(Long id);

    FrozenBox findByFrozenBoxCodeAndSampleTypeCode(String boxCode, String sampleTypeCode);

    @Query(value = " select support_rack_id,count(support_rack_id) as noo from frozen_box where status in ('"+ Constants.FROZEN_BOX_STOCKED+"','"+ Constants.FROZEN_BOX_PUT_SHELVES+"')" +
        " and support_rack_id in ?1 " +
        " group by support_rack_id ",nativeQuery = true)
    List<Object[]> findFrozenBoxGroupBySupportRack(List<Long> supportRackIds);

    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,b.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    ,t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,107 as type" +
        "                    FROM stock_out_handover_box t " +
        "                    LEFT JOIN stock_out_box b on t.stock_out_frozen_box_id = b.id" +
        "                    where b.frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxStockOutHandOverHistory(Long id);
    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,103 as type" +
        "                    FROM stock_out_box t where t.frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxStockOutHistory(Long id);

    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,102 as type" +
        "                    FROM stock_in_box t where t.frozen_box_id = ?1 and t.status = '2004'" ,nativeQuery = true)
    List<Object[]> findFrozenBoxStockInHistory(Long id);

    @Query(value = "select t.equipment_code,t.area_code,t.support_rack_code,t.ROWS_IN_SHELF,t.COLUMNS_IN_SHELF,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    t.frozen_box_type_id,t.frozen_box_type_code," +
        "                    t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date ,101 as type" +
        "                    from tranship_box t where frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxTranshipHistory(Long id);


    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    1 as frozen_box_type_id,1 as frozen_box_type_code," +
        "                    t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,104 as type" +
        "                    FROM position_move_record t where  frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxMoveHistory(Long id);

    @Query(value = "SELECT t.equipment_code,t.area_code,t.support_rack_code,t.rows_in_shelf,t.columns_in_shelf,t.equipment_id,t.area_id,t.support_rack_id,t.frozen_box_code,t.frozen_box_id ," +
        "                    1 as frozen_box_type_id,1 as frozen_box_type_code," +
        "                    t.sample_type_id,t.sample_type_code,t.sample_type_name,t.sample_classification_id,t.sample_classification_code,t.sample_classification_name" +
        "                    ,t.created_date,105 as type" +
        "                    FROM position_change_record t where frozen_box_id = ?1" ,nativeQuery = true)
    List<Object[]> findFrozenBoxChangeHistory(Long id);

    @Query(value = "select t.frozen_box_code from tranship_box t where t.status != '"+ Constants.FROZEN_BOX_INVALID+"' and t.status != '"+ Constants.INVALID+"' " +
        " and (?1=0 or t.project_id=?1) and (?2=0 or t.sample_type_id=?2) and (?3=0 or t.sample_classification_id=?3)" ,nativeQuery = true)
    List<String> findAllTranshipFrozenBoxCode(Long projectId, Long sampleTypeId, Long sampleClassId);

    @Query(value = "select t.frozen_box_code from stock_in_box t where t.status != '"+ Constants.FROZEN_BOX_INVALID+"' and t.status != '"+ Constants.INVALID+"' " +
        " and (?1=0 or t.project_id=?1) and (?2=0 or t.sample_type_id=?2) and (?3=0 or t.sample_classification_id=?3)" ,nativeQuery = true)
    List<String> findAllStockInFrozenBoxCode(Long projectId, Long sampleTypeId, Long sampleClassId);

    FrozenBox findByFrozenBoxCode1D(String boxCode1D);

    List<FrozenBox> findByProjectCodeAndStatus(String projectCode, String status);

    @Query(value = "SELECT t.ID,t.FROZEN_BOX_ROWS*t.FROZEN_BOX_COLUMNS AS ALLCOUNT,t.COUNT_OF_SAMPLE FROM FROZEN_BOX t WHERE t.PROJECT_ID = ?1 AND t.SAMPLE_CLASSIFICATION_ID in ?2 AND t.STATUS = '"+ Constants.FROZEN_BOX_STOCKED+"' AND  t.FROZEN_BOX_CODE != ?3 ORDER BY t.ID DESC  OFFSET ?4 ROWS FETCH NEXT ?5 ROWS ONLY",nativeQuery = true)
    List<Object[]> findIncompleteFrozenBoxIdBydProjectIdAnSampleClassificationIdAndBoxTypeId(Long projectId, ArrayList<Long> sampeClassTypeIds, String frozenBoxCode, Integer startPos, Integer length);


    //此方法仅供导入数据时使用
    @Query(nativeQuery = true,value = "select cast(frozen_box_code as varchar2(255)) as frozen_box_code from (\n" +
        "select frozen_box_code,count(1) as noo from (\n" +
        "select '入库' as id,b.frozen_box_code, b.frozen_box_code_1d,created_date from stock_in_box b where project_code = '0029'\n" +
        "union\n" +
        "select '出库'  as id,b.frozen_box_code, b.frozen_box_code_1d,created_date  from stock_out_box b where project_code = '0029'\n" +
        ") b  group by frozen_box_code) where noo>=3")
    List<Object> findFrozenBoxStockInAndOutRecord();

    //just used for test ..
    @Query(value = "select cast(id as varchar2(255)) as id," +
        " cast(frozen_box_code as varchar2(255)) as frozen_box_code," +
        " cast(frozen_box_code_1d as varchar2(255)) as frozen_box_code_1d," +
        " created_date" +
        " from (\n" +
        "select '入库' as id,b.frozen_box_code, b.frozen_box_code_1d,created_date from stock_in_box b where project_code = '0029'\n" +
        "union\n" +
        "select '出库'  as id,b.frozen_box_code, b.frozen_box_code_1d,created_date  from stock_out_box b where project_code = '0029')\n" +
        "\n" +
        "where frozen_box_code in ?1 order by frozen_box_code ,CREATED_DATE",nativeQuery = true)
    List<Object[]> findFrozenBoxStockInAndOutRecordByBoxCodeIn(List<String> frozenBoxCodeStr100);

    @Query(value = "SELECT B.ID FROM FROZEN_BOX B WHERE B.FROZEN_BOX_CODE = ?1 OR B.FROZEN_BOX_CODE_1D = ?1 " +
        "AND B.SAMPLE_TYPE_CODE = ?2  AND ( B.STATUS = '"+ Constants.FROZEN_BOX_STOCKED+"' or B.STATUS = '"+ Constants.FROZEN_BOX_TRANSHIP_COMPLETE+"') ",nativeQuery = true)
    Object findIdByFrozenBoxCodeAndSampleType(String boxCode1D, String sampleType);

    @Query("select box from FrozenBox box where box.supportRack.id = ?1 " +
            " and box.status not in ('"+ Constants.INVALID+"','"+ Constants.FROZEN_BOX_SPLITED+"','"+ Constants.FROZEN_BOX_INVALID+"','"+ Constants.FROZEN_BOX_STOCK_OUT_COMPLETED+"','"+ Constants.FROZEN_BOX_STOCK_OUT_HANDOVER+"')")
    List<FrozenBox> findBySupportRackId(Long id);

    @Query("select b from FrozenBox b where (b.frozenBoxCode = ?1 or b.frozenBoxCode1D = ?1 )and b.status = ?2" )
    FrozenBox findByFrozenBoxCodeAndStatus(String boxCode, String status);

    @Query(value = "SELECT b.id, (case when temp.coo is null THEN 0 else  temp.coo end) as noo" +
        " FROM frozen_box b" +
        " LEFT JOIN (select t.frozen_box_id , count(t.id) as coo from frozen_tube t " +
        " where t.status = '"+ Constants.FROZEN_TUBE_NORMAL+"' and t.frozen_tube_state = '"+ Constants.FROZEN_BOX_STOCKED+"' group by  t.frozen_box_id ) temp" +
        " on temp.frozen_box_id=b.id" +
        " WHERE b.project_id in ?1 AND  b.status = '"+ Constants.FROZEN_BOX_STOCKED+"'" +
        " and (?2 = 0 or b.sample_type_id=?2) " +
        " and (?3 = 0 or b.sample_classification_id=?3)  " +
        " ORDER BY noo desc,b.count_of_sample desc,b.equipment_code,b.area_code,b.support_rack_code,b.columns_in_shelf,b.rows_in_shelf offset ?4 rows fetch next ?5 rows only",nativeQuery = true)
    List<Object[]> getPageForStockOutProjectIdIn(List<Long> projectIds, Integer sampleTypeId, Integer samplyClassificationId, Integer startPos, Integer length);

    @Query(value = "select t.* from frozen_box t where t.frozen_box_code like ?1 and t.status = ?2" ,nativeQuery = true)
    List<FrozenBox> findByFrozenBoxCodeLikeAndStatus(String frozenBoxCodeLike, String status);

    @Query("SELECT t FROM FrozenBox t WHERE t.id in ?1 AND t.status !='"+ Constants.INVALID+"'")
    List<FrozenBox> findByIdIn(List<Long> frozenBoxIdLastList);


    @Query(value = "select f.*,(select count(tube.id) from frozen_tube tube left join stock_in_tube inTube " +
        " on inTube.frozen_tube_id = tube.id left join stock_in_box inBox on inTube.stock_in_box_id = inBox.id  and inBox.stock_in_code =?5 " +
        " where inTube.frozen_box_code =f.frozen_box_code and tube.frozen_box_code = f.frozen_box_code  and tube.status!='0000') as sampleNumber " +
        " from frozen_box f " +
        " where f.frozen_box_code != ?1 " +
        " and f.project_id=?2 " +
        " and f.sample_type_id in ?3 " +
        " and f.status=?4" +
        " and f.count_of_sample<(f.frozen_box_columns*f.frozen_box_rows) " +
        " and f.is_split = 0 " +
        " order by sampleNumber asc FETCH FIRST 10 ROWS ONLY",nativeQuery = true)
    List<FrozenBox> findIncompleteFrozenBoxBySampleTypeIdInAllStock(String frozenBoxCode, Long projectId, List sampleTypeIds, String status, String stockInCode);


    List<FrozenBox> findByFrozenBoxCode1DIn(List<String> alist);
    @Query(value = "select t.* from frozen_box t where t.frozen_box_code like ?1 and t.status in ?2" ,nativeQuery = true)
    List<FrozenBox> findByFrozenBoxCodeLikeAndStatusIn(String frozenBoxCodeLike, List<String> statusList);

    @Query("SELECT t FROM FrozenBox t where t.frozenBoxCode = ?1 or t.frozenBoxCode1D =?1")
    FrozenBox findByFrozenBoxCode(String boxCode);

    @Query(value = "select t.* from frozen_box t where t.project_id = ?1 and t.sample_type_id = ?2 and (?3 = 0 or t.sample_classification_id = ?3 )and t.status in ?4" ,nativeQuery = true)
    List<FrozenBox> findByProjectIdAndSampleTypeIdAndSampleClassificationIdAndStatusIn(Long projectId, Long sampleTypeId, Long sampleClassificationId, List<String> statusList);
    @Modifying
    @Query("update FrozenBox t set t.projectSiteName=?1 where t.projectSite.id=?2")
    void updateProjectSiteNameByProjectSiteId(String name, Long id);

    List<FrozenBox> findByProjectSiteId(Long id);

    @Query(value = "select t.* from frozen_box t where t.project_code = ?1 and t.count_of_sample >100" ,nativeQuery = true)
    List<FrozenBox> findWrongFrozenBoxCodeByProjectCode(String s);

    @Query(value = "select b.* from frozen_box b left join\n" +
        "(select equipment_code,area_code,support_rack_code,columns_in_shelf,rows_in_shelf from frozen_box where  project_code = '0031'\n" +
        " group by equipment_code,area_code,support_rack_code,columns_in_shelf,rows_in_shelf having count(1)>1) a \n" +
        " on a.equipment_code = b.equipment_code and a.area_code = b.area_code and a.support_rack_code = b.support_rack_code \n" +
        " and a.columns_in_shelf = b.columns_in_shelf and a.rows_in_shelf = b.rows_in_shelf where a.equipment_code is not null and \n" +
        "  b.count_of_sample=0 and b.project_code = '0031' and b.sample_type_code !='00' " ,nativeQuery = true)
    List<FrozenBox> findEmptyFrozenBoxForTest();
}

