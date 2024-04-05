package org.iesvdm.apitest.exception;


/**
 * Esta clase existe para controlar la excepcion de un Usuario que no existe
 * @author  Dario Guiles Cuesta
 *
 */
public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Long id){
        super("Not found User with id: " + id);
    }

}
