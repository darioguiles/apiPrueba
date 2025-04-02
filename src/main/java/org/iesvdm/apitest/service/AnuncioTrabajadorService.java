package org.iesvdm.apitest.service;

import org.iesvdm.apitest.domain.AnuncioTrabajador;
import org.iesvdm.apitest.exception.AnuncioTrabajadorNotFoundException;
import org.iesvdm.apitest.repository.AnuncioTrabajadorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AnuncioTrabajadorService {

    private final AnuncioTrabajadorRepository anuncioTrabajadorRepository;

    public AnuncioTrabajadorService(AnuncioTrabajadorRepository anuncioTrabajadorRepository){
        this.anuncioTrabajadorRepository=anuncioTrabajadorRepository;
    }

    public List<AnuncioTrabajador> all(){
        return this.anuncioTrabajadorRepository.findAll();
    }
    public List<Map<String, Object>> allF() {
        List<AnuncioTrabajador> anuncios = this.anuncioTrabajadorRepository.findAllWithTrabajador();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date subida = new Date(); // Creamos la hora a la que se publica el anuncio

        return anuncios.stream().map(anuncio -> {
            Map<String, Object> anuncioMap = new HashMap<>();
            anuncioMap.put("idAnuncioTrabajador", anuncio.getIdAnuncioTrabajador());
            anuncioMap.put("descripcion", anuncio.getDescripcion());
            anuncioMap.put("fechaPublicacion", dateFormat.format(subida));

            if (anuncio.getTrabajador() != null) {
                Map<String, Object> trabajadorMap = new HashMap<>();
                trabajadorMap.put("id_trabajador", anuncio.getTrabajador().getId_trabajador());
                trabajadorMap.put("nombre", anuncio.getTrabajador().getNombre());
                trabajadorMap.put("apellidos", anuncio.getTrabajador().getApellidos());
                trabajadorMap.put("telefono", anuncio.getTrabajador().getTelefono());

                anuncioMap.put("trabajador", trabajadorMap);
            }

            return anuncioMap;
        }).toList();
    }

    public Map<String, Object> findAll(int pagina, int tamanio) {

        //  **org.springframework.data.domain** public interface Pageable  Maven: org.springframework.data
        //  org.springframework.data.domain public interface Page  Maven: org.springframework.data
        Pageable paginado = PageRequest.of(pagina, tamanio, Sort.by("idAnuncioTrabajador").ascending());
        Page<AnuncioTrabajador> pageAll = this.anuncioTrabajadorRepository.findAllWithTrabajador(paginado);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date subida = new Date(); //Creamos la hora a la que se publica el anuncio

        List<Map<String, Object>> anunciosConTrabajador = pageAll.getContent().stream().map(anuncio -> {
            Map<String, Object> anuncioMap = new HashMap<>();
            anuncioMap.put("idAnuncioTrabajador", anuncio.getIdAnuncioTrabajador());
            anuncioMap.put("descripcion", anuncio.getDescripcion());
            anuncioMap.put("fechaPublicacion", dateFormat.format(subida));

            if (anuncio.getTrabajador() != null) {
                Map<String, Object> trabajadorMap = new HashMap<>();
                trabajadorMap.put("id_trabajador", anuncio.getTrabajador().getId_trabajador());
                trabajadorMap.put("nombre", anuncio.getTrabajador().getNombre());
                trabajadorMap.put("apellidos", anuncio.getTrabajador().getApellidos());
                trabajadorMap.put("telefono", anuncio.getTrabajador().getTelefono());

                anuncioMap.put("trabajador", trabajadorMap);
            }

            return anuncioMap;
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("anuncioTrabajadores", anunciosConTrabajador);
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;
    }

    public AnuncioTrabajador save(AnuncioTrabajador anuncioTrabajador) {
        return this.anuncioTrabajadorRepository.save(anuncioTrabajador);
    }

    public AnuncioTrabajador one(Long id) {
        return this.anuncioTrabajadorRepository.findById(id)
                .orElseThrow(() -> new AnuncioTrabajadorNotFoundException(id));
    }

    public AnuncioTrabajador replace(Long id, AnuncioTrabajador anuncioTrabajador) {

        return this.anuncioTrabajadorRepository.findById(id).map( p -> (id
                        .equals(anuncioTrabajador.getIdAnuncioTrabajador())  ?
                        this.anuncioTrabajadorRepository.save(anuncioTrabajador) : null))
                .orElseThrow(() -> new AnuncioTrabajadorNotFoundException(id));
    }

    public void delete(Long id) {
        this.anuncioTrabajadorRepository.findById(id).map(p -> {this.anuncioTrabajadorRepository.delete(p);
                    return p;})
                .orElseThrow(() -> new AnuncioTrabajadorNotFoundException(id));
    }

    public List<AnuncioTrabajador> allByQueryFiltersStream(Optional<String> buscar, Optional<String>ordenar)
    {
        //Tenemos diferentes formas, en este caso vamos a seguir
        //Una forma con la cual no necesitamos Entity Manager
        //Ni usamos codigo JPQL, sino con la sintaxis del Repository

        List<AnuncioTrabajador> resultado = null;
        if (buscar.isPresent()) // ** Buscamos por la *DESCRIPCION* **
        {
            //queryBuilder.append(" ").append("WHERE C.nombre like :nombre");
            resultado = anuncioTrabajadorRepository.findByDescripcionContainingIgnoreCase(buscar.get());
        }
        if (ordenar.isPresent())
        {
            if (buscar.isPresent() && "asc".equalsIgnoreCase(ordenar.get()) ) {
                // queryBuilder.append(" ").append("ORDER BY C.nombre ASC");
                resultado = anuncioTrabajadorRepository
                        .findAllByDescripcionContainingIgnoreCaseOrderByDescripcionAsc(buscar.get());

            } else if(buscar.isPresent() && "desc".equalsIgnoreCase(ordenar.get())) {
                //queryBuilder.append(" ").append("ORDER BY C.nombre DESC");
                resultado = anuncioTrabajadorRepository
                        .findAllByDescripcionContainingIgnoreCaseOrderByDescripcionDesc(buscar.get());
            }
        }
        return resultado;
    }

}

