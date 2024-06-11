package com.project.core.ticketapp.exceptions;

/**
 * Created by Lewis.Aguh on 19/08/2020.
 */
public class BadRequestException extends AbstractException{

	private static final long serialVersionUID = 1L;

	public BadRequestException(String code, String message){
        super(code,message);
    }
}