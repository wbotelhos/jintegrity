package com.jintegrity.exception;

public class JIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1067122648064638438L;

	public JIntegrityException(String mensagem) {
		super(mensagem);
	}

	public JIntegrityException(String mensagem, Throwable e) {
		super(mensagem, e);
	}

}
