package org.fwoxford.service.dto.response;

/**
 * Created by gengluying on 2017/5/15.
 */
public class DelegateResponse {
    private Long id;

    private String relatedAgency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelatedAgency() {
        return relatedAgency;
    }

    public void setRelatedAgency(String relatedAgency) {
        this.relatedAgency = relatedAgency;
    }
}
