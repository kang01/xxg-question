package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.QuartsTaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity QuartsTask and its DTO QuartsTaskDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuartsTaskMapper extends EntityMapper<QuartsTaskDTO, QuartsTask> {



    default QuartsTask fromId(Long id) {
        if (id == null) {
            return null;
        }
        QuartsTask quartsTask = new QuartsTask();
        quartsTask.setId(id);
        return quartsTask;
    }
}
