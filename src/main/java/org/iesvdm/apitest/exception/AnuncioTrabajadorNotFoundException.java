package org.iesvdm.apitest.exception;

public class AnuncioTrabajadorNotFoundException extends RuntimeException {
    public AnuncioTrabajadorNotFoundException(Long id) { super("Not found Worker Ad with id: " + id);}
}
