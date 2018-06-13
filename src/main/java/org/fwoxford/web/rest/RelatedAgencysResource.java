package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.fwoxford.core.service.dto.response.DelegateResponse;
import org.fwoxford.service.DelegateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Delegate.
 */
@RestController
@RequestMapping("/api")
public class RelatedAgencysResource {

    private final Logger log = LoggerFactory.getLogger(RelatedAgencysResource.class);

    private static final String ENTITY_NAME = "delegate";

    private final DelegateService delegateService;

    public RelatedAgencysResource(DelegateService delegateService) {
        this.delegateService = delegateService;
    }


    /**
     * 获取相关单位
     * @return
     */
    @GetMapping("/relatedAgencys")
    @Timed
    public ResponseEntity<List<DelegateResponse>> getAllRelatedAgencys() {
        log.debug("REST request to get all FrozenTubeTypeResponse");
        List<DelegateResponse> list = delegateService.findAllRelatedAgencys();
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(list));
    }
}
