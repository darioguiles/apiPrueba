package org.iesvdm.apitest.service;


import org.iesvdm.apitest.domain.AnuncioEmpresa;
import org.iesvdm.apitest.domain.AnuncioTrabajador;
import org.iesvdm.apitest.exception.AnuncioEmpresaNotFoundException;
import org.iesvdm.apitest.repository.AnuncioEmpresaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AnuncioEmpresaService {

    private final AnuncioEmpresaRepository anuncioEmpresaRepository;

    public AnuncioEmpresaService(AnuncioEmpresaRepository anuncioEmpresaRepository){
        this.anuncioEmpresaRepository=anuncioEmpresaRepository;
    }

    public List<Map<String, Object>> allFiltered(String descripcion, String nombre) {
        List<AnuncioEmpresa> anuncios = this.anuncioEmpresaRepository.findAnuncioTod(descripcion,nombre);

        return getMaps(anuncios);
    }

    public List<Map<String, Object>> allF(){
        List<AnuncioEmpresa> anuncios = this.anuncioEmpresaRepository.findAllWithEmpresa();


        return getMaps(anuncios);
    }

    private List<Map<String, Object>> getMaps(List<AnuncioEmpresa> anuncios) {
        return anuncios.stream().map(anuncio -> {
            Map<String, Object> anuncioMap = new HashMap<>();
            anuncioMap.put("idAnuncioEmpresa", anuncio.getIdAnuncioEmpresa());
            anuncioMap.put("descripcion", anuncio.getDescripcion());
            anuncioMap.put("cantidadPuestos", anuncio.getCantidadPuestos());
            anuncioMap.put("numAdscritos", anuncio.getNumAdscritos());
            anuncioMap.put("fechaInicio", anuncio.getFechaInicio() != null ? anuncio.getFechaInicio() : null);
            anuncioMap.put("fechaFin", anuncio.getFechaFin() != null ? anuncio.getFechaFin() : null );

            if (anuncio.getEmpresa() != null) {
                Map<String, Object> empresaMap = new HashMap<>();
                empresaMap.put("id_empresa", anuncio.getEmpresa().getId_empresa());
                empresaMap.put("nombre", anuncio.getEmpresa().getNombre());
                empresaMap.put("descripcion", anuncio.getEmpresa().getDescripcion());
                empresaMap.put("telefono", anuncio.getEmpresa().getTelefono());

                anuncioMap.put("empresa", empresaMap);
            }

            return anuncioMap;
        }).toList();
    }

    public List<Map<String, Object>> allId(Long id){
        List<AnuncioEmpresa> anuncios = this.anuncioEmpresaRepository.findAllByEmpresaIdEmpresa(id);

        return getMaps(anuncios);
    }

    public List<AnuncioEmpresa> all(){
        return this.anuncioEmpresaRepository.findAll();
    }

    public Map<String, Object> findAll(int pagina, int tamanio) {

        //  org.springframework.data.domain public interface Pageable  Maven: org.springframework.data
        Pageable paginado = PageRequest.of(pagina, tamanio, Sort.by("idAnuncioEmpresa" ).ascending());
        //Interfaces
        Page<AnuncioEmpresa> pageAll = this.anuncioEmpresaRepository.findAllWithEmpresa(paginado);

        List<Map<String, Object>> anunciosConEmpresa = pageAll.getContent().stream().map(anuncio -> {
            Map<String, Object> anuncioMap = new HashMap<>();
            anuncioMap.put("idAnuncioEmpresa", anuncio.getIdAnuncioEmpresa());
            anuncioMap.put("descripcion", anuncio.getDescripcion());
            anuncioMap.put("fechaInicio", anuncio.getFechaInicio());
            anuncioMap.put("fechaFin", anuncio.getFechaFin()!=null ? anuncio.getFechaFin() : null);

            if (anuncio.getEmpresa() != null) {
                Map<String, Object> empresaMap = new HashMap<>();
                empresaMap.put("id_empresa", anuncio.getEmpresa().getId_empresa());
                empresaMap.put("nombre", anuncio.getEmpresa().getNombre());
                empresaMap.put("telefono", anuncio.getEmpresa().getTelefono());
                anuncioMap.put("empresa", empresaMap);
            }
            return anuncioMap;
        }).toList();
        
        Map<String,Object> response = new HashMap<>();
        response.put("anuncioEmpresa", anunciosConEmpresa);
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;

    }

    public Map<String, Object> buscarAnunciosFiltrados(String descripcion, String nombre,  Pageable pageable) {
        Page<AnuncioEmpresa> pageAnuncios = anuncioEmpresaRepository.findBecauseFilter(descripcion, nombre,  pageable);

        List<Map<String, Object>> anunciosConEmpresa = pageAnuncios.getContent().stream().map(anuncio -> {
            Map<String, Object> anuncioMap = new HashMap<>();
            anuncioMap.put("idAnuncioEmpresa", anuncio.getIdAnuncioEmpresa());
            anuncioMap.put("descripcion", anuncio.getDescripcion());
            anuncioMap.put("fechaInicio", anuncio.getFechaInicio());
            anuncioMap.put("fechaFin", anuncio.getFechaFin());

            if (anuncio.getEmpresa() != null) {
                Map<String, Object> empresaMap = new HashMap<>();
                empresaMap.put("id_empresa", anuncio.getEmpresa().getId_empresa());
                empresaMap.put("nombre", anuncio.getEmpresa().getNombre());
                empresaMap.put("telefono", anuncio.getEmpresa().getTelefono());
                anuncioMap.put("empresa", empresaMap);
            }

            return anuncioMap;
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("anuncioEmpresa", anunciosConEmpresa);
        response.put("currentPage", pageAnuncios.getNumber());
        response.put("totalItems", pageAnuncios.getTotalElements());
        response.put("totalPages", pageAnuncios.getTotalPages());

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
