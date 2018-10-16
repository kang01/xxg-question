package org.fwoxford.service.dto.response;

import javax.validation.constraints.Size;

/**
 * Created by gengluying on 2018/8/20.
 */
//@MappedSuperclass
//@Audited
//@EntityListeners({AuditingEntityListener.class, EntityAuditEventListener.class})
public abstract class CommonEntity {
    /**
     * 项目编码
     */
    @Size(max = 100)
    private String projectCode;
    /**
     * 项目名称
     */
    @Size(max = 255)
    private String projectName;
    /**
     * 项目点编码
     */
    @Size(max = 100)
    private String projectSiteCode;
    /**
     * 项目点名称
     */
    @Size(max = 255)
    private String projectSiteName;
    /**
     * 样本类型编码
     */
    @Size(max = 100)
    private String sampleTypeCode;
    /**
     * 样本类型名称
     */
    @Size(max = 255)
    private String sampleTypeName;
    private String sampleClassificationCode;
    private String sampleClassificationName;

    /**
     * 样本类型ID
     */
    private Long sampleTypeId;
    /**
     *样本分类ID
     */
    private Long sampleClassificationId;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目点ID
     */
    private Long projectSiteId;
    private Integer isMixed;
    private String frontColor;
    private String backColor;
    private String frontColorForClass;
    private String backColorForClass;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSiteCode() {
        return projectSiteCode;
    }

    public void setProjectSiteCode(String projectSiteCode) {
        this.projectSiteCode = projectSiteCode;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public String getSampleTypeCode() {
        return sampleTypeCode;
    }

    public void setSampleTypeCode(String sampleTypeCode) {
        this.sampleTypeCode = sampleTypeCode;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getSampleClassificationCode() {
        return sampleClassificationCode;
    }

    public void setSampleClassificationCode(String sampleClassificationCode) {
        this.sampleClassificationCode = sampleClassificationCode;
    }

    public String getSampleClassificationName() {
        return sampleClassificationName;
    }

    public void setSampleClassificationName(String sampleClassificationName) {
        this.sampleClassificationName = sampleClassificationName;
    }

    public Long getSampleTypeId() {
        return sampleTypeId;
    }

    public void setSampleTypeId(Long sampleTypeId) {
        this.sampleTypeId = sampleTypeId;
    }

    public Long getSampleClassificationId() {
        return sampleClassificationId;
    }

    public void setSampleClassificationId(Long sampleClassificationId) {
        this.sampleClassificationId = sampleClassificationId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectSiteId() {
        return projectSiteId;
    }

    public void setProjectSiteId(Long projectSiteId) {
        this.projectSiteId = projectSiteId;
    }

    public Integer getIsMixed() {
        return isMixed;
    }

    public void setIsMixed(Integer isMixed) {
        this.isMixed = isMixed;
    }

    public String getFrontColor() {
        return frontColor;
    }

    public void setFrontColor(String frontColor) {
        this.frontColor = frontColor;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public String getFrontColorForClass() {
        return frontColorForClass;
    }

    public void setFrontColorForClass(String frontColorForClass) {
        this.frontColorForClass = frontColorForClass;
    }

    public String getBackColorForClass() {
        return backColorForClass;
    }

    public void setBackColorForClass(String backColorForClass) {
        this.backColorForClass = backColorForClass;
    }
}
