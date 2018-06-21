package org.fwoxford.service.impl;

import org.fwoxford.service.ReplyDetailsService;
import org.fwoxford.domain.ReplyDetails;
import org.fwoxford.repository.ReplyDetailsRepository;
import org.fwoxford.service.dto.ReplyDetailsDTO;
import org.fwoxford.service.mapper.ReplyDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ReplyDetails.
 */
@Service
@Transactional
public class ReplyDetailsServiceImpl implements ReplyDetailsService {

    private final Logger log = LoggerFactory.getLogger(ReplyDetailsServiceImpl.class);

    private final ReplyDetailsRepository replyDetailsRepository;

    private final ReplyDetailsMapper replyDetailsMapper;

    public ReplyDetailsServiceImpl(ReplyDetailsRepository replyDetailsRepository, ReplyDetailsMapper replyDetailsMapper) {
        this.replyDetailsRepository = replyDetailsRepository;
        this.replyDetailsMapper = replyDetailsMapper;
    }

    /**
     * Save a replyDetails.
     *
     * @param replyDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ReplyDetailsDTO save(ReplyDetailsDTO replyDetailsDTO) {
        log.debug("Request to save ReplyDetails : {}", replyDetailsDTO);
        ReplyDetails replyDetails = replyDetailsMapper.toEntity(replyDetailsDTO);
        replyDetails = replyDetailsRepository.save(replyDetails);
        return replyDetailsMapper.toDto(replyDetails);
    }

    /**
     * Get all the replyDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReplyDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReplyDetails");
        return replyDetailsRepository.findAll(pageable)
            .map(replyDetailsMapper::toDto);
    }

    /**
     * Get one replyDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ReplyDetailsDTO findOne(Long id) {
        log.debug("Request to get ReplyDetails : {}", id);
        ReplyDetails replyDetails = replyDetailsRepository.findOne(id);
        return replyDetailsMapper.toDto(replyDetails);
    }

    /**
     * Delete the replyDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReplyDetails : {}", id);
        replyDetailsRepository.delete(id);
    }
}
