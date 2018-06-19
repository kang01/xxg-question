package org.fwoxford.service.impl;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.Delegate;
import org.fwoxford.repository.DelegateRepository;
import org.fwoxford.service.DelegateService;
import org.fwoxford.service.dto.response.DelegateResponse;
import org.fwoxford.service.mapper.DelegateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Delegate.
 */
@Service
@Transactional
public class DelegateServiceImpl implements DelegateService {

    private final Logger log = LoggerFactory.getLogger(DelegateServiceImpl.class);
    @Autowired
    DelegateRepository delegateRepository;

    @Autowired
    DelegateMapper delegateMapper;

    @Override
    public List<DelegateResponse> findAllRelatedAgencys() {
        List<Delegate> delegates = delegateRepository.findByStatusNot(Constants.INVALID);
        return delegateMapper.delegatesToDelegateResponses(delegates);
    }
}
