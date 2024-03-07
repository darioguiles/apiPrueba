package org.iesvdm.apitest.exception;

public class TrabajadorNotFoundException extends RuntimeException {
    public TrabajadorNotFoundException (Long id){
        super("Not found Worker with id: " + id);
    }
}
