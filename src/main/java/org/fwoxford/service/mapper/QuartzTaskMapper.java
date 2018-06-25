package org.fwoxford.service.mapper;

import org.fwoxford.domain.*;
import org.fwoxford.service.dto.QuartzTaskDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity QuartzTask and its DTO QuartzTaskDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuartzTaskMapper extends EntityMapper<QuartzTaskDTO, QuartzTask> {



    default QuartzTask fromId(Long id) {
        if (id == null) {
            return null;
        }
        QuartzTask quartzTask = new QuartzTask();
        quartzTask.setId(id);
        return quartzTask;
    }
}
