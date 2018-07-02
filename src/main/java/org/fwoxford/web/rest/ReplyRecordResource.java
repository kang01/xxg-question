package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.ReplyRecordService;
import org.fwoxford.service.dto.ReplyDetailsDTO;
import org.fwoxford.web.rest.errors.BadRequestAlertException;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.ReplyRecordDTO;
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
 * REST controller for managing ReplyRecord.
 */
@RestController
@RequestMapping("/api")
public class ReplyRecordResource {

    private final Logger log = LoggerFactory.getLogger(ReplyRecordResource.class);

    private static final String ENTITY_NAME = "replyRecord";

    private final ReplyRecordService replyRecordService;

    public ReplyRecordResource(ReplyRecordService replyRecordService) {
        this.replyRecordService = replyRecordService;
    }

    /**
     * POST  /reply-records : Create a new replyRecord.
     * 回复问题
     * @param replyDetailsDTOS the replyRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new replyRecordDTO, or with status 400 (Bad Request) if the replyRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reply-records/sendRecord/{id}")
    @Timed
    public ResponseEntity<List<ReplyDetailsDTO>> createReplyRecordList(@Valid @PathVariable Long id , @Valid @RequestBody List<ReplyDetailsDTO> replyDetailsDTOS) throws URISyntaxException {
        log.debug("REST request to save ReplyRecordList For One Question : {}", replyDetailsDTOS);

        List<ReplyDetailsDTO> result = replyRecordService.saveReplyQuestionList(id , replyDetailsDTOS);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * 完成回复并且停止任务
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/reply-records/sendRecord/{id}/completed")
    @Timed
    public ResponseEntity<ReplyRecordDTO> completedReplyRecord(@Valid @PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to update ReplyRecord : {}", id);
        ReplyRecordDTO result = replyRecordService.completedReplyRecord(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * GET  /reply-records : get all the replyRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of replyRecords in body
     */
    @GetMapping("/reply-records")
    @Timed
    public ResponseEntity<List<ReplyRecordDTO>> getAllReplyRecords(Pageable pageable) {
        log.debug("REST request to get a page of ReplyRecords");
        Page<ReplyRecordDTO> page = replyRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reply-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reply-records/:id : get the "id" replyRecord.
     *
     * @param id the id of the replyRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the replyRecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/reply-records/{id}")
    @Timed
    public ResponseEntity<ReplyRecordDTO> getReplyRecord(@PathVariable Long id) {
        log.debug("REST request to get ReplyRecord : {}", id);
        ReplyRecordDTO replyRecordDTO = replyRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(replyRecordDTO));
    }

    /**
     * DELETE  /reply-records/:id : delete the "id" replyRecord.
     *
     * @param id the id of the replyRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reply-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteReplyRecord(@PathVariable Long id) {
        log.debug("REST request to delete ReplyRecord : {}", id);
        replyRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
