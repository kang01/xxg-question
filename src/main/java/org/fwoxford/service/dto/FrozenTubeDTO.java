package org.fwoxford.service.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FrozenTube entity.
 */
public class FrozenTubeDTO extends FrozenTubeLabelDTO implements Serializable {

    private Long id;
    /**
     * 冻存管编码
     */
    @Size(max = 100)
    private String frozenTubeCode;
    /**
     * 样本临时编码
     */
    @Size(max = 100)
    private String sampleTempCode;
    /**
     * 样本编码
     */
    @Size(max = 100)
    private String sampleCode;
    /**
     * 冻存管类型编码
     */
    @Size(max = 100)
    private String frozenTubeTypeCode;
    /**
     * 冻存管类型名称
     */
    @NotNull
    @Size(max = 255)
    private String frozenTubeTypeName;
    /**
     * 样本最多使用次数
     */
    @Max(value = 20)
    private Integer sampleUsedTimesMost;
    /**
     * 样本已使用次数
     */
    @Max(value = 20)
    private Integer sampleUsedTimes;
    /**
     * 冻存管容量值
     */
    @Max(value = 20)
    private Double frozenTubeVolumns;
    /**
     * 冻存管容量值单位
     */
    @Size(max = 20)
    private String frozenTubeVolumnsUnit;

    private Double sampleVolumns;
    /**
     * 行数
     */
    @NotNull
    @Size(max = 20)
    private String tubeRows;
    /**
     * 列数
     */
    @NotNull
    @Size(max = 20)
    private String tubeColumns;
    /**
     * 备注
     */
    @Size(max = 1024)
    private String memo;
    /**
     * 错误类型：6001：位置错误，6002：样本类型错误，6003：其他
     */
    @Size(max = 20)
    private String errorType;
    /**
     * 状态
     */
    @Size(max = 20)
    private String frozenTubeState;
    /**
     * 状态：3001：正常，3002：空管，3003：空孔；3004：异常;3005:半管
     */
    @NotNull
    @Size(max = 20)
    private String status;
    /**
     * 冻存盒编码
     */
    @Size(max = 100)
    private String frozenBoxCode;

    /**
     * 冻存管类型ID
     */
    private Long frozenTubeTypeId;

    /**
     * 冻存盒ID
     */
    private Long frozenBoxId;


    private String position;

    /**
     * 管子标识，2：原盒原库存，1：盒内新加入的冻存管
     */
    private String flag;
    /**
     * 出库标识:1:出库,2:取消出库
     */
    private Integer stockOutFlag;
    /**
     * 取消出库的原因
     */
    private String repealReason;

    private Long parentSampleId;
    private String parentSampleCode;
    private String frozenBoxCode1D;
    private String dangerCoefficient;
    private Integer lockFlag;
    /**
     * 锁业务类型 2：申请销毁，3：创建问题
     */
    private Integer bussinessType;
    /**
     * 锁于业务的ID
     */
    private Long bussinessId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getFrozenTubeCode() {
        return frozenTubeCode;
    }

    public void setFrozenTubeCode(String frozenTubeCode) {
        this.frozenTubeCode = frozenTubeCode;
    }
    public String getSampleTempCode() {
        return sampleTempCode;
    }

    public void setSampleTempCode(String sampleTempCode) {
        this.sampleTempCode = sampleTempCode;
    }
    public String getSampleCode() {
        return sampleCode;
    }

    public void setSampleCode(String sampleCode) {
        this.sampleCode = sampleCode;
    }
    public String getFrozenTubeTypeCode() {
        return frozenTubeTypeCode;
    }

    public void setFrozenTubeTypeCode(String frozenTubeTypeCode) {
        this.frozenTubeTypeCode = frozenTubeTypeCode;
    }
    public String getFrozenTubeTypeName() {
        return frozenTubeTypeName;
    }

    public void setFrozenTubeTypeName(String frozenTubeTypeName) {
        this.frozenTubeTypeName = frozenTubeTypeName;
    }

    public Integer getSampleUsedTimesMost() {
        return sampleUsedTimesMost;
    }

    public void setSampleUsedTimesMost(Integer sampleUsedTimesMost) {
        this.sampleUsedTimesMost = sampleUsedTimesMost;
    }
    public Integer getSampleUsedTimes() {
        return sampleUsedTimes;
    }

    public void setSampleUsedTimes(Integer sampleUsedTimes) {
        this.sampleUsedTimes = sampleUsedTimes;
    }
    public Double getFrozenTubeVolumns() {
        return frozenTubeVolumns;
    }

    public void setFrozenTubeVolumns(Double frozenTubeVolumns) {
        this.frozenTubeVolumns = frozenTubeVolumns;
    }
    public String getFrozenTubeVolumnsUnit() {
        return frozenTubeVolumnsUnit;
    }

    public void setFrozenTubeVolumnsUnit(String frozenTubeVolumnsUnit) {
        this.frozenTubeVolumnsUnit = frozenTubeVolumnsUnit;
    }

    public Double getSampleVolumns() {
        return sampleVolumns;
    }

    public void setSampleVolumns(Double sampleVolumns) {
        this.sampleVolumns = sampleVolumns;
    }

    public String getTubeRows() {
        return tubeRows;
    }

    public void setTubeRows(String tubeRows) {
        this.tubeRows = tubeRows;
    }
    public String getTubeColumns() {
        return tubeColumns;
    }

    public void setTubeColumns(String tubeColumns) {
        this.tubeColumns = tubeColumns;
    }
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getFrozenTubeState() {
        return frozenTubeState;
    }

    public void setFrozenTubeState(String frozenTubeState) {
        this.frozenTubeState = frozenTubeState;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getFrozenBoxCode() {
        return frozenBoxCode;
    }

    public void setFrozenBoxCode(String frozenBoxCode) {
        this.frozenBoxCode = frozenBoxCode;
    }

    public Long getFrozenTubeTypeId() {
        return frozenTubeTypeId;
    }

    public void setFrozenTubeTypeId(Long frozenTubeTypeId) {
        this.frozenTubeTypeId = frozenTubeTypeId;
    }


    public Long getFrozenBoxId() {
        return frozenBoxId;
    }

    public void setFrozenBoxId(Long frozenBoxId) {
        this.frozenBoxId = frozenBoxId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FrozenTubeDTO frozenTubeDTO = (FrozenTubeDTO) o;

        if ( ! Objects.equals(id, frozenTubeDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FrozenTubeDTO{" +
            "id=" + id +
            ", frozenTubeCode='" + frozenTubeCode + "'" +
            ", sampleTempCode='" + sampleTempCode + "'" +
            ", sampleCode='" + sampleCode + "'" +
            ", frozenTubeTypeCode='" + frozenTubeTypeCode + "'" +
            ", frozenTubeTypeName='" + frozenTubeTypeName + "'" +
            ", sampleUsedTimesMost='" + sampleUsedTimesMost + "'" +
            ", sampleUsedTimes='" + sampleUsedTimes + "'" +
            ", frozenTubeVolumns='" + frozenTubeVolumns + "'" +
            ", frozenTubeVolumnsUnit='" + frozenTubeVolumnsUnit + "'" +
            ", sampleVolumns='" + sampleVolumns + "'" +
            ", tubeRows='" + tubeRows + "'" +
            ", tubeColumns='" + tubeColumns + "'" +
            ", memo='" + memo + "'" +
            ", errorType='" + errorType + "'" +
            ", frozenTubeState ='"+ frozenTubeState + "'" +
            ", status='" + status + "'" +
            ", frozenBoxCode='" + frozenBoxCode + "'" +
            ", position='" + position + "'" +
            '}';
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getStockOutFlag() {
        return stockOutFlag;
    }

    public void setStockOutFlag(Integer stockOutFlag) {
        this.stockOutFlag = stockOutFlag;
    }

    public String getRepealReason() {
        return repealReason;
    }

    public void setRepealReason(String repealReason) {
        this.repealReason = repealReason;
    }

    public Long getParentSampleId() {
        return parentSampleId;
    }

    public void setParentSampleId(Long parentSampleId) {
        this.parentSampleId = parentSampleId;
    }

    public String getParentSampleCode() {
        return parentSampleCode;
    }

    public void setParentSampleCode(String parentSampleCode) {
        this.parentSampleCode = parentSampleCode;
    }


    public String getDangerCoefficient() {
        return dangerCoefficient;
    }

    public void setDangerCoefficient(String dangerCoefficient) {
        this.dangerCoefficient = dangerCoefficient;
    }

    public String getFrozenBoxCode1D() {
        return frozenBoxCode1D;
    }

    public void setFrozenBoxCode1D(String frozenBoxCode1D) {
        this.frozenBoxCode1D = frozenBoxCode1D;
    }

    public Integer getLockFlag() {
        return lockFlag;
    }

    public void setLockFlag(Integer lockFlag) {
        this.lockFlag = lockFlag;
    }

    public Integer getBussinessType() {
        return bussinessType;
    }

    public void setBussinessType(Integer bussinessType) {
        this.bussinessType = bussinessType;
    }

    public Long getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(Long bussinessId) {
        this.bussinessId = bussinessId;
    }
}
