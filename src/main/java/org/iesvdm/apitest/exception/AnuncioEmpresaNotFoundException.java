package org.iesvdm.apitest.exception;

public class AnuncioEmpresaNotFoundException extends RuntimeException {
    public AnuncioEmpresaNotFoundException(Long id) { super("Not found Company Ad with id: " + id);}
}
