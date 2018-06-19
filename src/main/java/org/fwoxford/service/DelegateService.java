package org.fwoxford.service;
import org.fwoxford.service.dto.response.DelegateResponse;

import java.util.List;

/**
 * Service Interface for managing Delegate.
 */
public interface DelegateService {
    /**
     * 获取相关单位
     * @return
     */
    List<DelegateResponse> findAllRelatedAgencys();
}
