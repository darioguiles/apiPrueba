package org.iesvdm.apitest.service;


import org.iesvdm.apitest.domain.Trabajador;
import org.iesvdm.apitest.exception.TrabajadorNotFoundException;
import org.iesvdm.apitest.repository.TrabajadorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TrabajadorService {

    private final TrabajadorRepository trabajadorRepository;

    public TrabajadorService(TrabajadorRepository trabajadorRepository){ this.trabajadorRepository= trabajadorRepository;}

    public List<Trabajador> all() {
        return this.trabajadorRepository.findAll();
    }

    public Map<String, Object> findAll(int pagina, int tamanio) {

        //  org.springframework.data.domain public interface Pageable  Maven: org.springframework.data

        Pageable paginado = PageRequest.of(pagina, tamanio, Sort.by("id_trabajador" ).ascending());
        //Interfaces
        Page<Trabajador> pageAll = this.trabajadorRepository.findAll(paginado);

        Map<String,Object> response = new HashMap<>();

        response.put("trabajadores", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;

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

    public List<Trabajador> allByQueryFiltersStream(Optional<String> buscar, Optional<String>ordenar)
    {
        //Tenemos diferentes formas, en este caso vamos a seguir
        //Una forma con la cual no necesitamos Entity Manager
        //Ni usamos codigo JPQL, sino con la sintaxis del Repository
        
       List<Trabajador> resultado = null;

        if (buscar.isPresent())
        {
            //queryBuilder.append(" ").append("WHERE C.nombre like :nombre");
            resultado = trabajadorRepository.findByNombreContainingIgnoreCase(buscar.get());
        }
        if (ordenar.isPresent())
        {
            if (buscar.isPresent() && "asc".equalsIgnoreCase(ordenar.get()) ) {
                // queryBuilder.append(" ").append("ORDER BY C.nombre ASC");
                resultado = trabajadorRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(buscar.get());

            } else if(buscar.isPresent() && "desc".equalsIgnoreCase(ordenar.get())) {
                //queryBuilder.append(" ").append("ORDER BY C.nombre DESC");
                resultado = trabajadorRepository.findByNombreContainingIgnoreCaseOrderByNombreDesc(buscar.get());
            }
        }

        return resultado;
    }

}
