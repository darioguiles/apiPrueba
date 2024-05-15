package org.iesvdm.apitest.service;


import org.iesvdm.apitest.domain.AnuncioEmpresa;
import org.iesvdm.apitest.exception.AnuncioEmpresaNotFoundException;
import org.iesvdm.apitest.repository.AnuncioEmpresaRepository;
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
public class AnuncioEmpresaService {

    private final AnuncioEmpresaRepository anuncioEmpresaRepository;

    public AnuncioEmpresaService(AnuncioEmpresaRepository anuncioEmpresaRepository){
        this.anuncioEmpresaRepository=anuncioEmpresaRepository;
    }

    public List<AnuncioEmpresa> all(){
        return this.anuncioEmpresaRepository.findAll();
    }

    public Map<String, Object> findAll(int pagina, int tamanio) {

        //  org.springframework.data.domain public interface Pageable  Maven: org.springframework.data

        Pageable paginado = PageRequest.of(pagina, tamanio, Sort.by("idAnuncioEmpresa" ).ascending());
        //Interfaces
        Page<AnuncioEmpresa> pageAll = this.anuncioEmpresaRepository.findAll(paginado);

        Map<String,Object> response = new HashMap<>();

        response.put("anuncioEmpresa", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;

    }


    public AnuncioEmpresa save(AnuncioEmpresa anuncioEmpresa) {
        return this.anuncioEmpresaRepository.save(anuncioEmpresa);
    }

    public AnuncioEmpresa one(Long id) {
        return this.anuncioEmpresaRepository.findById(id)
                .orElseThrow(() -> new AnuncioEmpresaNotFoundException(id));
    }

    public AnuncioEmpresa replace(Long id, AnuncioEmpresa anuncioEmpresa) {

        return this.anuncioEmpresaRepository.findById(id).map( p ->
                        (id.equals(anuncioEmpresa.getIdAnuncioEmpresa())  ?
                        this.anuncioEmpresaRepository.save(anuncioEmpresa) : null))
                .orElseThrow(() -> new AnuncioEmpresaNotFoundException(id));
    }

    public void delete(Long id) {
        this.anuncioEmpresaRepository.findById(id).map(p -> {this.anuncioEmpresaRepository.delete(p);
                    return p;})
                .orElseThrow(() -> new AnuncioEmpresaNotFoundException(id));
    }

    public List<AnuncioEmpresa> allByQueryFiltersStream(Optional<String> buscar, Optional<String>ordenar)
    {
        //Tenemos diferentes formas, en este caso vamos a seguir
        //Una forma con la cual no necesitamos Entity Manager
        //Ni usamos codigo JPQL, sino con la sintaxis del Repository

        List<AnuncioEmpresa> resultado = null;
        if (buscar.isPresent()) // ** Buscamos por la *DESCRIPCION* **
        {
            //queryBuilder.append(" ").append("WHERE C.nombre like :nombre");
            resultado = anuncioEmpresaRepository.findByDescripcionContainingIgnoreCase(buscar.get());
        }
        if (ordenar.isPresent())
        {
            if (buscar.isPresent() && "asc".equalsIgnoreCase(ordenar.get()) ) {
                // queryBuilder.append(" ").append("ORDER BY C.nombre ASC");
                resultado = anuncioEmpresaRepository
                        .findAllByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(buscar.get());

            } else if(buscar.isPresent() && "desc".equalsIgnoreCase(ordenar.get())) {
                //queryBuilder.append(" ").append("ORDER BY C.nombre DESC");
                resultado = anuncioEmpresaRepository
                        .findAllByDescripcionContainingIgnoreCaseOrderByDescripcionDesc(buscar.get());
            }
        }
        return resultado;
    }


}
