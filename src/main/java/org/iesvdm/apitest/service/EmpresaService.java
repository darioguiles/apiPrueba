package org.iesvdm.apitest.service;

import org.iesvdm.apitest.domain.Empresa;
import org.iesvdm.apitest.exception.EmpresaNotFoundException;
import org.iesvdm.apitest.repository.EmpresaRepository;
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
public class EmpresaService {
    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    public List<Empresa> all() {
        return this.empresaRepository.findAll();
    }

    public Map<String, Object> findAll(int pagina, int tamanio) {

        //  org.springframework.data.domain public interface Pageable  Maven: org.springframework.data
        Pageable paginado = PageRequest.of(pagina, tamanio, Sort.by("id_empresas" ).ascending());
        //Interfaces
        Page<Empresa> pageAll = this.empresaRepository.findAll(paginado);

        Map<String,Object> response = new HashMap<>();
        response.put("empresas", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());
        return response;
    }


    public Empresa save(Empresa empresa) {
        return this.empresaRepository.save(empresa);
    }

    public Empresa one(Long id) {
        return this.empresaRepository.findById(id)
                .orElseThrow(() -> new EmpresaNotFoundException(id));
    }

    public Empresa replace(Long id, Empresa empresa) {

        return this.empresaRepository.findById(id).map( p -> (id.equals(empresa.getId_empresa())  ?
                        this.empresaRepository.save(empresa) : null))
                .orElseThrow(() -> new EmpresaNotFoundException(id));

    }

    public void delete(Long id) {
        this.empresaRepository.findById(id).map(p -> {this.empresaRepository.delete(p);
                    return p;})
                .orElseThrow(() -> new EmpresaNotFoundException(id));
    }

    public List<Empresa> allByQueryFiltersStream(Optional<String> buscar, Optional<String>ordenar)
    {
        //Tenemos diferentes formas, en este caso vamos a seguir
        //Una forma con la cual no necesitamos Entity Manager
        //Ni usamos codigo JPQL, sino con la sintaxis del Repository

        List<Empresa> resultado = null;

        if (buscar.isPresent())
        {
            resultado = empresaRepository.findByNombreContainingIgnoreCase(buscar.get());
        }
        if (ordenar.isPresent())
        {
            if (buscar.isPresent() && "asc".equalsIgnoreCase(ordenar.get()) ) {
                resultado = empresaRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(buscar.get());

            } else if(buscar.isPresent() && "desc".equalsIgnoreCase(ordenar.get())) {
                resultado = empresaRepository.findByNombreContainingIgnoreCaseOrderByNombreDesc(buscar.get());
            }
        }

        return resultado;
    }


}
