package org.iesvdm.apitest.exception;


/**
 * Esta clase existe para controlar la excepcion de un Usuario que no existe
 * Con esta como Padre, nos ahorramos las de Empresa y Trabajador,
 * Pudiendo usarse para controlar esas clases también
 *
 * @author  Dario Guiles Cuesta
 *
 */
public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Long id){
        super("Not found User with id: " + id);
    }

}
