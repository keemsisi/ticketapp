package com.project.core.ticketapp.exceptions;

/**
 * Created by Lewis.Aguh on 19/08/2020.
 */
public class DuplicateException extends AbstractException{

	private static final long serialVersionUID = 1L;
    public DuplicateException(String code, String message){
        super(code,message);
    }
}
