package org.core.backend.ticketapp.marketing.service;

import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.marketing.entity.FormData;

public interface FormDataService extends IService<FormData> {
    FormData getByCode(String code);
}
