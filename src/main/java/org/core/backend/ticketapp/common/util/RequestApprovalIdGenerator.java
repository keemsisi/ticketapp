package org.core.backend.ticketapp.common.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Adewale Azeez <azeezadewale98@gmail.com>
 * @date 21-Dec-20 07:32 AM
 */
public class RequestApprovalIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {

        String prefix = "REQUESTAPPROVAL";
        String date = new SimpleDateFormat("MMddyyyyHHmmss").format(new Date());
        String randomStr = Helpers.GetSaltString("TICKETAPP", 5);
        return prefix + date + randomStr;
    }

}