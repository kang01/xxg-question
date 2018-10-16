package org.fwoxford.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Equipment.
 */
@Entity
@Table(name = "equipment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Equipment extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_equipment")
    @SequenceGenerator(name = "seq_equipment",sequenceName = "seq_equipment",allocationSize = 1,initialValue = 1)
    private Long id;
    /**
     * 设备编码
     */
    @NotNull
    @Size(max = 100)
    @Column(name = "equipment_code", length = 100, nullable = false ,unique = true)
    private String equipmentCode;
    /**
     * 设备地址
     */
    @NotNull
    @Size(max = 255)
    @Column(name = "equipment_address", length = 255, nullable = false)
    private String equipmentAddress;
    /**
     * 存放最大冻存管数量
     */
    @NotNull
    @Column(name = "ampoules_max", nullable = false)
    private Integer ampoulesMax;
    /**
     * 存放最小冻存管数量
     */
    @NotNull
    @Column(name = "ampoules_min", nullable = false)
    private Integer ampoulesMin;
    /**
     * 工作最大温度
     */
    @Max(value = 100)
    @Column(name = "working_max_t")
    private Integer workingMaxT;
    /**
     * 工作最小温度
     */
    @Size(max = 100)
    @Column(name = "working_min_t", length = 100)
    private Integer workingMinT;
    /**
     * 报警最大温度
     */
    @Size(max = 100)
    @Column(name = "warning_max_t", length = 100)
    private Integer warningMaxT;
    /**
     * 报警最小温度
     */
    @Size(max = 100)
    @Column(name = "warning_min_t", length = 100)
    private Integer warningMinT;
    /**
     * 设备归属
     */
    @Column(name = "equipment_affiliation", length = 100)
    private String equipmentAffiliation;
    /**
     * 采购日期
      */
    @Column(name = "date_of_decision")
    private LocalDate dateOfDecision;
    /**
     * 出厂日期
     */
    @Column(name = "date_of_production")
    private LocalDate dateOfProduction;
    /**
     * 启用日期
     */
    @Column(name = "date_of_opening")
    private LocalDate dateOfOpening;
    /**
     * 报废年限 单位按照年，月，日选择，提交数据时统一单位为日
     */
    @Max(value = 2191)
    @Column(name = "end_of_life")
    private Integer endOfLife;
    /**
     * 保养周期 单位按照年，月，日选择，提交数据时统一单位为日
     */
    @Max(value = 2191)
    @Column(name = "maintenance_period")
    private Integer maintenancePeriod;
    /**
     * 分区数
     */
    @NotNull
    @Max(value = 255)
    @Column(name = "count_of_area", nullable = false)
    private Integer countOfArea;
    /**
     * 生产厂家
     */
    @Column(name = "manufacturer", length = 255)
    private String manufacturer;
    /**
     * 联系地址
     */
    @Column(name = "contact_address", length = 255)
    private String contactAddress;
    /**
     * 联系电话
     */
    @Column(name = "contact_phone", length = 255)
    private String contactPhone;
    /**
     * 备注
     */
    @Size(max = 1024)
    @Column(name = "memo", length = 1024)
    private String memo;
    /**
     * 状态
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "status", length = 20, nullable = false)
    private String status;
    /**
     * 设备组
     */
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "equipment_group_id")
    private EquipmentGroup equipmentGroup;
    /**
     * 设备型号
     */
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "equipment_model_id")
    private EquipmentModel equipmentModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public Equipment equipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
        return this;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }


    public String getEquipmentAddress() {
        return equipmentAddress;
    }

    public Equipment equipmentAddress(String equipmentAddress) {
        this.equipmentAddress = equipmentAddress;
        return this;
    }

    public void setEquipmentAddress(String equipmentAddress) {
        this.equipmentAddress = equipmentAddress;
    }

    public Integer getAmpoulesMax() {
        return ampoulesMax;
    }

    public Equipment ampoulesMax(Integer ampoulesMax) {
        this.ampoulesMax = ampoulesMax;
        return this;
    }

    public void setAmpoulesMax(Integer ampoulesMax) {
        this.ampoulesMax = ampoulesMax;
    }

    public Integer getAmpoulesMin() {
        return ampoulesMin;
    }

    public Equipment ampoulesMin(Integer ampoulesMin) {
        this.ampoulesMin = ampoulesMin;
        return this;
    }

    public void setAmpoulesMin(Integer ampoulesMin) {
        this.ampoulesMin = ampoulesMin;
    }

    public Integer getWorkingMaxT() {
        return workingMaxT;
    }
    public Equipment workingMaxT(Integer workingMaxT) {
        this.workingMaxT = workingMaxT;
        return this;
    }
    public void setWorkingMaxT(Integer workingMaxT) {
        this.workingMaxT = workingMaxT;
    }

    public Integer getWorkingMinT() {
        return workingMinT;
    }
    public Equipment workingMinT(Integer workingMinT) {
        this.workingMinT = workingMinT;
        return this;
    }
    public void setWorkingMinT(Integer workingMinT) {
        this.workingMinT = workingMinT;
    }

    public Integer getWarningMaxT() {
        return warningMaxT;
    }
    public Equipment warningMaxT(Integer warningMaxT) {
        this.warningMaxT = warningMaxT;
        return this;
    }
    public void setWarningMaxT(Integer warningMaxT) {
        this.warningMaxT = warningMaxT;
    }

    public Integer getWarningMinT() {
        return warningMinT;
    }
    public Equipment warningMinT(Integer warningMinT) {
        this.warningMinT = warningMinT;
        return this;
    }
    public void setWarningMinT(Integer warningMinT) {
        this.warningMinT = warningMinT;
    }

    public String getEquipmentAffiliation() {
        return equipmentAffiliation;
    }
    public Equipment equipmentAffiliation(String equipmentAffiliation) {
        this.equipmentAffiliation = equipmentAffiliation;
        return this;
    }
    public void setEquipmentAffiliation(String equipmentAffiliation) {
        this.equipmentAffiliation = equipmentAffiliation;
    }

    public LocalDate getDateOfDecision() {
        return dateOfDecision;
    }
    public Equipment dateOfDecision(LocalDate dateOfDecision) {
        this.dateOfDecision = dateOfDecision;
        return this;
    }
    public void setDateOfDecision(LocalDate dateOfDecision) {
        this.dateOfDecision = dateOfDecision;
    }

    public LocalDate getDateOfProduction() {
        return dateOfProduction;
    }
    public Equipment dateOfProduction(LocalDate dateOfProduction) {
        this.dateOfProduction = dateOfProduction;
        return this;
    }
    public void setDateOfProduction(LocalDate dateOfProduction) {
        this.dateOfProduction = dateOfProduction;
    }

    public LocalDate getDateOfOpening() {
        return dateOfOpening;
    }
    public Equipment dateOfOpening(LocalDate dateOfOpening) {
        this.dateOfOpening = dateOfOpening;
        return this;
    }
    public void setDateOfOpening(LocalDate dateOfOpening) {
        this.dateOfOpening = dateOfOpening;
    }

    public Integer getEndOfLife() {
        return endOfLife;
    }
    public Equipment endOfLife(Integer endOfLife) {
        this.endOfLife = endOfLife;
        return this;
    }
    public void setEndOfLife(Integer endOfLife) {
        this.endOfLife = endOfLife;
    }

    public Integer getMaintenancePeriod() {
        return maintenancePeriod;
    }
    public Equipment maintenancePeriod(Integer maintenancePeriod) {
        this.maintenancePeriod = maintenancePeriod;
        return this;
    }
    public void setMaintenancePeriod(Integer maintenancePeriod) {
        this.maintenancePeriod = maintenancePeriod;
    }

    public Integer getCountOfArea() {
        return countOfArea;
    }
    public Equipment countOfArea(Integer countOfArea) {
        this.countOfArea = countOfArea;
        return this;
    }
    public void setCountOfArea(Integer countOfArea) {
        this.countOfArea = countOfArea;
    }

    public String getManufacturer() {
        return manufacturer;
    }
    public Equipment manufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getContactAddress() {
        return contactAddress;
    }
    public Equipment contactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
        return this;
    }
    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getContactPhone() {
        return contactPhone;
    }
    public Equipment contactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        return this;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getMemo() {
        return memo;
    }

    public Equipment memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStatus() {
        return status;
    }

    public Equipment status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EquipmentGroup getEquipmentGroup() {
        return equipmentGroup;
    }

    public Equipment equipmentGroup(EquipmentGroup equipmentGroup) {
        this.equipmentGroup = equipmentGroup;
        return this;
    }

    public void setEquipmentGroup(EquipmentGroup equipmentGroup) {
        this.equipmentGroup = equipmentGroup;
    }

    public EquipmentModel getEquipmentModel() {
        return equipmentModel;
    }

    public Equipment equipmentModle(EquipmentModel equipmentModel) {
        this.equipmentModel = equipmentModel;
        return this;
    }

    public void setEquipmentModel(EquipmentModel equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Equipment equipment = (Equipment) o;
        if (equipment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, equipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Equipment{" +
            "id=" + id +
            ", equipmentCode='" + equipmentCode + "'" +
            ", equipmentAddress='" + equipmentAddress + "'" +
            ", ampoulesMax='" + ampoulesMax + "'" +
            ", ampoulesMin='" + ampoulesMin + "'" +
            ", memo='" + memo + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
