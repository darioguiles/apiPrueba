package org.iesvdm.apitest.exception;

/*
Al ser relación identificativa podriamos suprimir el uso de estas 2 Excepciones por su superior con Usuario?
Puesto que comparten Id (Eso si, es cierto que un usuario puede existir sin tener una Empresa o Trabajador creado)
* */
public class EmpresaNotFoundException extends RuntimeException {
    public EmpresaNotFoundException (Long id){
        super("Not found Company with id: " + id);
    }

}
