package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EquipmentModel.
 */
@Entity
@Table(name = "equipment_model")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EquipmentModel extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_equipment_modle")
    @SequenceGenerator(name = "seq_equipment_modle",sequenceName = "seq_equipment_modle",allocationSize = 1,initialValue = 1)
    private Long id;
    /**
     * 设备型号编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "equipment_model_code", length = 100, nullable = false)
    private String equipmentModelCode;
    /**
     * 设备型号名称
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "equipment_model_name", length = 255, nullable = false)
    private String equipmentModelName;
    /**
     * 设备类型：液氮，冰箱。
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "equipment_type", length = 20, nullable = false)
    private String equipmentType;
    /**
     * 区域数量
     */
    @NotNull
    @Max(value = 255)
    @Column(name = "area_number", nullable = false)
    private Integer areaNumber;
    /**
     * 备注
     */
    @Column(name = "memo")
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 最高温度
     */
    @Max(value = 100)
    @Column(name = "temperature_max")
    private Integer temperatureMax;
    /**
     * 最低温度
     */
    @Max(value = 100)
    @Column(name = "temperature_min")
    private Integer temperatureMin;
    /**
     * 缩写
     */
    @NotNull
    @Size(max = 50)
    @Column(name = "abbr")
    private String abbr;
    /**
     * 报废年限 单位按照年，月，日选择，提交数据时统一单位为日
     */
    @Max(value = 2191)
    @Column(name = "discarded")
    private Integer discarded;
    /**
     * 保养周期 单位按照年，月，日选择，提交数据时统一单位为日
     */
    @Max(value = 2191)
    @Column(name = "maintenance_period")
    private Integer maintenancePeriod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentModelCode() {
        return equipmentModelCode;
    }

    public EquipmentModel equipmentModelCode(String equipmentModelCode) {
        this.equipmentModelCode = equipmentModelCode;
        return this;
    }

    public void setEquipmentModelCode(String equipmentModelCode) {
        this.equipmentModelCode = equipmentModelCode;
    }

    public String getEquipmentModelName() {
        return equipmentModelName;
    }

    public EquipmentModel equipmentModelName(String equipmentModelName) {
        this.equipmentModelName = equipmentModelName;
        return this;
    }

    public void setEquipmentModelName(String equipmentModelName) {
        this.equipmentModelName = equipmentModelName;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public EquipmentModel equipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
        return this;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Integer getAreaNumber() {
        return areaNumber;
    }

    public EquipmentModel areaNumber(Integer areaNumber) {
        this.areaNumber = areaNumber;
        return this;
    }

    public void setAreaNumber(Integer areaNumber) {
        this.areaNumber = areaNumber;
    }

    public String getMemo() {
        return memo;
    }

    public EquipmentModel memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public EquipmentModel status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTemperatureMax() {
        return temperatureMax;
    }

    public EquipmentModel temperatureMax(Integer temperatureMax) {
        this.temperatureMax = temperatureMax;
        return this;
    }

    public void setTemperatureMax(Integer temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Integer getTemperatureMin() {
        return temperatureMin;
    }

    public EquipmentModel temperatureMin(Integer temperatureMin) {
        this.temperatureMin = temperatureMin;
        return this;
    }

    public void setTemperatureMin(Integer temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public Integer getDiscarded() {
        return discarded;
    }
    public EquipmentModel discarded(Integer discarded) {
        this.discarded = discarded;
        return this;
    }

    public void setDiscarded(Integer discarded) {
        this.discarded = discarded;
    }

    public Integer getMaintenancePeriod() {
        return maintenancePeriod;
    }
    public EquipmentModel maintenancePeriod(Integer maintenancePeriod) {
        this.maintenancePeriod = maintenancePeriod;
        return this;
    }
    public void setMaintenancePeriod(Integer maintenancePeriod) {
        this.maintenancePeriod = maintenancePeriod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EquipmentModel equipmentModel = (EquipmentModel) o;
        if (equipmentModel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, equipmentModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EquipmentModel{" +
            "id=" + id +
            ", equipmentModelCode='" + equipmentModelCode + "'" +
            ", equipmentModelName='" + equipmentModelName + "'" +
            ", equipmentType='" + equipmentType + "'" +
            ", areaNumber='" + areaNumber + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            ", temperatureMax='" + temperatureMax + "'" +
            ", temperatureMin='" + temperatureMin + "'" +
            '}';
    }
}
