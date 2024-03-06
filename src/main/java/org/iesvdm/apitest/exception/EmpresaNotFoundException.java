package org.iesvdm.apitest.exception;

public class EmpresaNotFoundException extends RuntimeException {
    public EmpresaNotFoundException (Long id){
        super("Not found Company with id: " + id);
    }

}
