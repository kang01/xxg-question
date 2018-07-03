package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.SendRecordService;
import org.fwoxford.web.rest.errors.BadRequestAlertException;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.web.rest.util.PaginationUtil;
import org.fwoxford.service.dto.SendRecordDTO;
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
 * REST controller for managing SendRecord.
 */
@RestController
@RequestMapping("/api")
public class SendRecordResource {

    private final Logger log = LoggerFactory.getLogger(SendRecordResource.class);

    private static final String ENTITY_NAME = "sendRecord";

    private final SendRecordService sendRecordService;

    public SendRecordResource(SendRecordService sendRecordService) {
        this.sendRecordService = sendRecordService;
    }

    /**
     * POST  /send-records : Create a new sendRecord.
     *
     * @param sendRecordDTO the sendRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sendRecordDTO, or with status 400 (Bad Request) if the sendRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/send-records")
    @Timed
    public ResponseEntity<SendRecordDTO> createSendRecord(@Valid @RequestBody SendRecordDTO sendRecordDTO) throws URISyntaxException {
        log.debug("REST request to save SendRecord : {}", sendRecordDTO);
        if (sendRecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new sendRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SendRecordDTO result = sendRecordService.save(sendRecordDTO);
        return ResponseEntity.created(new URI("/api/send-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /send-records : Updates an existing sendRecord.
     *
     * @param sendRecordDTO the sendRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sendRecordDTO,
     * or with status 400 (Bad Request) if the sendRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the sendRecordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/send-records")
    @Timed
    public ResponseEntity<SendRecordDTO> updateSendRecord(@Valid @RequestBody SendRecordDTO sendRecordDTO) throws URISyntaxException {
        log.debug("REST request to update SendRecord : {}", sendRecordDTO);
        if (sendRecordDTO.getId() == null) {
            return createSendRecord(sendRecordDTO);
        }
        SendRecordDTO result = sendRecordService.save(sendRecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sendRecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /send-records : get all the sendRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sendRecords in body
     */
    @GetMapping("/send-records")
    @Timed
    public ResponseEntity<List<SendRecordDTO>> getAllSendRecords(Pageable pageable) {
        log.debug("REST request to get a page of SendRecords");
        Page<SendRecordDTO> page = sendRecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/send-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /send-records/:id : get the "id" sendRecord.
     *
     * @param id the id of the sendRecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sendRecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/send-records/{id}")
    @Timed
    public ResponseEntity<SendRecordDTO> getSendRecord(@PathVariable Long id) {
        log.debug("REST request to get SendRecord : {}", id);
        SendRecordDTO sendRecordDTO = sendRecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sendRecordDTO));
    }

    /**
     * DELETE  /send-records/:id : delete the "id" sendRecord.
     *
     * @param id the id of the sendRecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/send-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteSendRecord(@PathVariable Long id) {
        log.debug("REST request to delete SendRecord : {}", id);
        sendRecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * 查询发送记录
     * @param id
     * @return
     */
    @GetMapping("/send-records/question/{id}")
    @Timed
    public ResponseEntity<List<SendRecordDTO>> getSendRecordByQuestionId(@PathVariable Long id) {
        log.debug("REST request to get SendRecord : {}", id);
        List<SendRecordDTO> sendRecordDTOs = sendRecordService.findSendRecordByQuestionId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sendRecordDTOs));
    }

    /**
     * 重发
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/send-records/{id}/re-send")
    @Timed
    public ResponseEntity<SendRecordDTO> createNewSendRecord(@Valid @PathVariable Long id ) throws URISyntaxException {
        log.debug("REST request to update SendRecord : {}", id);
        SendRecordDTO result = sendRecordService.saveSendRecordForReSend(id);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, id.toString()))
            .body(result);
    }

    /**
     * 申请加时
     * @param id
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/send-records/{id}/increase-time")
    @Timed
    public ResponseEntity<SendRecordDTO> increaseTimeSendRecord(@Valid @PathVariable Long id ) throws URISyntaxException {
        log.debug("REST request to update SendRecord : {}", id);
        SendRecordDTO result = sendRecordService.increaseTimeSendRecord(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, id.toString()))
                .body(result);
    }

}
