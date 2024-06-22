package com.project.core.ticketapp.exceptions;


public class NotFoundException extends AbstractException {
    private static final long serialVersionUID = 1L;

    public NotFoundException(String code, String message) {
        super(code, message);
    }

    public static NotFoundException forMutualFundWithId(long account) {
        return new NotFoundException("400", "Mutual Account for organisation {" + account + "} not found");
    }
}
