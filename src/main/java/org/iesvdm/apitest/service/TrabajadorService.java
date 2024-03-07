package org.iesvdm.apitest.service;


import org.iesvdm.apitest.domain.Trabajador;
import org.iesvdm.apitest.exception.TrabajadorNotFoundException;
import org.iesvdm.apitest.repository.TrabajadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrabajadorService {

    private final TrabajadorRepository trabajadorRepository;

    public TrabajadorService(TrabajadorRepository trabajadorRepository){ this.trabajadorRepository= trabajadorRepository;}

    public List<Trabajador> all() {
        return this.trabajadorRepository.findAll();
    }

    public Trabajador save(Trabajador trabajador) {
        return this.trabajadorRepository.save(trabajador);
    }

    public Trabajador one(Long id) {
        return this.trabajadorRepository.findById(id)
                .orElseThrow(() -> new TrabajadorNotFoundException(id));
    }

    public Trabajador replace(Long id, Trabajador trabajador) {

        return this.trabajadorRepository.findById(id).map( p -> (id.equals(trabajador.getId_trabajador())  ?
                        this.trabajadorRepository.save(trabajador) : null))
                .orElseThrow(() -> new TrabajadorNotFoundException(id));

    }

    public void delete(Long id) {
        this.trabajadorRepository.findById(id).map(p -> {this.trabajadorRepository.delete(p);
                    return p;})
                .orElseThrow(() -> new TrabajadorNotFoundException(id));
    }

}
