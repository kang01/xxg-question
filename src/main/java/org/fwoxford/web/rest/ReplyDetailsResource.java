package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.ReplyDetailsService;
import org.fwoxford.web.rest.errors.BadRequestAlertException;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.ReplyDetailsDTO;
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
 * REST controller for managing ReplyDetails.
 */
@RestController
@RequestMapping("/api")
public class ReplyDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ReplyDetailsResource.class);

    private static final String ENTITY_NAME = "replyDetails";

    private final ReplyDetailsService replyDetailsService;

    public ReplyDetailsResource(ReplyDetailsService replyDetailsService) {
        this.replyDetailsService = replyDetailsService;
    }

    /**
     * POST  /reply-details : Create a new replyDetails.
     *
     * @param replyDetailsDTO the replyDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new replyDetailsDTO, or with status 400 (Bad Request) if the replyDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reply-details")
    @Timed
    public ResponseEntity<ReplyDetailsDTO> createReplyDetails(@Valid @RequestBody ReplyDetailsDTO replyDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save ReplyDetails : {}", replyDetailsDTO);
        if (replyDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new replyDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReplyDetailsDTO result = replyDetailsService.save(replyDetailsDTO);
        return ResponseEntity.created(new URI("/api/reply-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reply-details : Updates an existing replyDetails.
     *
     * @param replyDetailsDTO the replyDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated replyDetailsDTO,
     * or with status 400 (Bad Request) if the replyDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the replyDetailsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reply-details")
    @Timed
    public ResponseEntity<ReplyDetailsDTO> updateReplyDetails(@Valid @RequestBody ReplyDetailsDTO replyDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update ReplyDetails : {}", replyDetailsDTO);
        if (replyDetailsDTO.getId() == null) {
            return createReplyDetails(replyDetailsDTO);
        }
        ReplyDetailsDTO result = replyDetailsService.save(replyDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, replyDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reply-details : get all the replyDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of replyDetails in body
     */
    @GetMapping("/reply-details")
    @Timed
    public ResponseEntity<List<ReplyDetailsDTO>> getAllReplyDetails(Pageable pageable) {
        log.debug("REST request to get a page of ReplyDetails");
        Page<ReplyDetailsDTO> page = replyDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reply-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reply-details/:id : get the "id" replyDetails.
     *
     * @param id the id of the replyDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the replyDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reply-details/{id}")
    @Timed
    public ResponseEntity<ReplyDetailsDTO> getReplyDetails(@PathVariable Long id) {
        log.debug("REST request to get ReplyDetails : {}", id);
        ReplyDetailsDTO replyDetailsDTO = replyDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(replyDetailsDTO));
    }

    /**
     * DELETE  /reply-details/:id : delete the "id" replyDetails.
     *
     * @param id the id of the replyDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reply-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteReplyDetails(@PathVariable Long id) {
        log.debug("REST request to delete ReplyDetails : {}", id);
        replyDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
