package org.fwoxford.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.fwoxford.service.AuthorizationRecordService;
import org.fwoxford.web.rest.util.HeaderUtil;
import org.fwoxford.service.dto.AuthorizationRecordDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
     * 保存授权码
     * @param authorizationRecordDTOs the authorizationRecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new authorizationRecordDTO, or with status 400 (Bad Request) if the authorizationRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/authorization-records/questionId/{id}/send")
    @Timed
    public ResponseEntity<List<AuthorizationRecordDTO>> createAuthorizationRecords(@Valid @PathVariable Long id ,@Valid @RequestBody List<AuthorizationRecordDTO> authorizationRecordDTOs) throws URISyntaxException {
        log.debug("REST request to save AuthorizationRecord List : {}", authorizationRecordDTOs);
        for(AuthorizationRecordDTO authorizationRecordDTO : authorizationRecordDTOs){
            if (authorizationRecordDTO.getId() != null) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new authorization_records cannot already have an ID")).body(null);

            }
        }
        List<AuthorizationRecordDTO> result = authorizationRecordService.saveAuthorizationRecords(id,authorizationRecordDTOs);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * PUT  /authorization-records : Updates an existing authorizationRecord.
     *
     * @param authorizationRecordDTOs the authorizationRecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated authorizationRecordDTO,
     * or with status 400 (Bad Request) if the authorizationRecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the authorizationRecordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/authorization-records/questionId/{id}/send")
    @Timed
    public ResponseEntity<List<AuthorizationRecordDTO>> updateAuthorizationRecord(@Valid @PathVariable Long id ,@Valid @RequestBody List<AuthorizationRecordDTO> authorizationRecordDTOs) throws URISyntaxException {
        log.debug("REST request to update AuthorizationRecord List : {}", authorizationRecordDTOs);
//        for(AuthorizationRecordDTO authorizationRecordDTO :authorizationRecordDTOs){
//            if (authorizationRecordDTO.getId() == null) {
//                throw new BankServiceException("授权码ID不能为空！");
//            }
//        }

        List<AuthorizationRecordDTO> result = authorizationRecordService.saveAuthorizationRecords(id,authorizationRecordDTOs);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

    /**
     * GET  /authorization-records : get all the authorizationRecords.
     *
     * @param id the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of authorizationRecords in body
     */
    @GetMapping("/authorization-records/questionId/{id}")
    @Timed
    public ResponseEntity<List<AuthorizationRecordDTO>> getAllAuthorizationRecords(@Valid @PathVariable Long id) {
        log.debug("REST request to get all AuthorizationRecords of one question");
        List<AuthorizationRecordDTO> result = authorizationRecordService.findAllAuthorizationRecordsByQuestionId(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
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

    /**
     * 验证门户登陆的邮箱，问题编码，url，授权码
     * @param authorizationRecordDTO
     * @return
     */
    @PostMapping("/authorization-records/entity")
    @Timed
    public ResponseEntity<AuthorizationRecordDTO> getAuthorizationRecordByDTO(@Valid @RequestBody AuthorizationRecordDTO authorizationRecordDTO) {
        log.debug("REST request to get all AuthorizationRecords of one question");
        AuthorizationRecordDTO result = authorizationRecordService.findAuthorizationRecordByDTO(authorizationRecordDTO);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(result));
    }

}
