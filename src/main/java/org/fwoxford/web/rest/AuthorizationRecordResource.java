package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.AuthorizationRecordService;
import org.fwoxford.web.rest.errors.BadRequestAlertException;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.AuthorizationRecordDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing AuthorizationRecord.
 */
@RestController
@RequestMapping("/api")
public class AuthorizationRecordResource {

    private final Logger log = LoggerFactory.getLogger(AuthorizationRecordResource.class);

    private static final String ENTITY_NAME = "authorizationRecord";

    private final AuthorizationRecordService authorizationRecordService;

    public AuthorizationRecordResource(AuthorizationRecordService authorizationRecordService) {
        this.authorizationRecordService = authorizationRecordService;
    }

    /**
     * POST  /authorization-records : Create a new authorizationRecord.
     *
     * @param authorizationRecordDTO the authorizationRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new authorizationRecordDTO, or with status 400 (Bad Request) if the authorizationRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/authorization-records")
    @Timed
    public ResponseEntity<AuthorizationRecordDTO> createAuthorizationRecord(@Valid @RequestBody AuthorizationRecordDTO authorizationRecordDTO) throws URISyntaxException {
        log.debug("REST request to save AuthorizationRecord : {}", authorizationRecordDTO);
        if (authorizationRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new authorizationRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AuthorizationRecordDTO result = authorizationRecordService.save(authorizationRecordDTO);
        return ResponseEntity.created(new URI("/api/authorization-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /authorization-records : Updates an existing authorizationRecord.
     *
     * @param authorizationRecordDTO the authorizationRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated authorizationRecordDTO,
     * or with status 400 (Bad Request) if the authorizationRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the authorizationRecordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/authorization-records")
    @Timed
    public ResponseEntity<AuthorizationRecordDTO> updateAuthorizationRecord(@Valid @RequestBody AuthorizationRecordDTO authorizationRecordDTO) throws URISyntaxException {
        log.debug("REST request to update AuthorizationRecord : {}", authorizationRecordDTO);
        if (authorizationRecordDTO.getId() == null) {
            return createAuthorizationRecord(authorizationRecordDTO);
        }
        AuthorizationRecordDTO result = authorizationRecordService.save(authorizationRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, authorizationRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /authorization-records : get all the authorizationRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of authorizationRecords in body
     */
    @GetMapping("/authorization-records")
    @Timed
    public ResponseEntity<List<AuthorizationRecordDTO>> getAllAuthorizationRecords(Pageable pageable) {
        log.debug("REST request to get a page of AuthorizationRecords");
        Page<AuthorizationRecordDTO> page = authorizationRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/authorization-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /authorization-records/:id : get the "id" authorizationRecord.
     *
     * @param id the id of the authorizationRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the authorizationRecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/authorization-records/{id}")
    @Timed
    public ResponseEntity<AuthorizationRecordDTO> getAuthorizationRecord(@PathVariable Long id) {
        log.debug("REST request to get AuthorizationRecord : {}", id);
        AuthorizationRecordDTO authorizationRecordDTO = authorizationRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(authorizationRecordDTO));
    }

    /**
     * DELETE  /authorization-records/:id : delete the "id" authorizationRecord.
     *
     * @param id the id of the authorizationRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/authorization-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteAuthorizationRecord(@PathVariable Long id) {
        log.debug("REST request to delete AuthorizationRecord : {}", id);
        authorizationRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
