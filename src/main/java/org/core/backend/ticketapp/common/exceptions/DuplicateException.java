package org.core.backend.ticketapp.common.exceptions;

public class DuplicateException extends AbstractException{

	private static final long serialVersionUID = 1L;
    public DuplicateException(String code, String message){
        super(code,message);
    }
}
