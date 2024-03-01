package org.iesvdm.apitest.service;


import org.iesvdm.apitest.domain.Categoria;
import org.iesvdm.apitest.exception.CategoriaNotFoundException;
import org.iesvdm.apitest.repository.CategoriaRepository;
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
public class CategoriaService {

   /* @Autowired
    private EntityManager em;*/

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> all() {
        return this.categoriaRepository.findAll();
    }

    public Map<String, Object> all(int pagina, int tamanio) {
        //  org.springframework.data.domain public interface Pageable  Maven: org.springframework.data

        Pageable paginado = PageRequest.of(pagina, tamanio, Sort.by("id" ).ascending());
        //Interfaces
        Page<Categoria> pageAll = this.categoriaRepository.findAll(paginado);

        Map<String,Object> response = new HashMap<>();

        response.put("categorias", pageAll.getContent());
        response.put("currentPage", pageAll.getNumber());
        response.put("totalItems", pageAll.getTotalElements());
        response.put("totalPages", pageAll.getTotalPages());

        return response;
    }




    public Categoria save(Categoria categoria) {
        return this.categoriaRepository.save(categoria);
    }

    public Categoria one(Long id) {
        return this.categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException(id));
    }

    public Categoria replace(Long id, Categoria categoria) {

        return this.categoriaRepository.findById(id).map( p -> (id.equals(categoria.getId())  ?
                        this.categoriaRepository.save(categoria) : null))
                .orElseThrow(() -> new CategoriaNotFoundException(id));

    }

    public void delete(Long id) {
        this.categoriaRepository.findById(id).map(p -> {this.categoriaRepository.delete(p);
                    return p;})
                .orElseThrow(() -> new CategoriaNotFoundException(id));
    }



    /*
    * Este metodo se puede conformar por las 2 formas, la forma Que usamos ahora mediante
    * JPARepository de forma simple mediante métodos con formas predefinidas O con JPQL para lo cual nos ha falta StringBuilder para construir las queries
    * Query y un EntityManager.
    *
    * */
    public List<Categoria> allByQueryFiltersStream(Optional<String> buscarOpc, Optional<String> ordenarOpt) {

        List<Categoria> resultado = null;
        //StringBuilder queryBuilder = new StringBuilder("SELECT C from Categoria C");
        if (buscarOpc.isPresent())
        {
            //queryBuilder.append(" ").append("WHERE C.nombre like :nombre");
            resultado = categoriaRepository.findByNombreContainingIgnoreCase(buscarOpc.get());
        }
        if (ordenarOpt.isPresent())
        {
            if (buscarOpc.isPresent() && "asc".equalsIgnoreCase(ordenarOpt.get()) ) {
               // queryBuilder.append(" ").append("ORDER BY C.nombre ASC");
                resultado = categoriaRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(buscarOpc.get());

            } else if(buscarOpc.isPresent() && "desc".equalsIgnoreCase(ordenarOpt.get())) {
                //queryBuilder.append(" ").append("ORDER BY C.nombre DESC");
                resultado = categoriaRepository.findByNombreContainingIgnoreCaseOrderByNombreDesc(buscarOpc.get());
            }
        }
        //Consulta JPQL, sintaxis SQL pero con una entidad JPA
       /* Query query = em.createQuery(queryBuilder.toString());
        if (buscarOpc.isPresent()){
            query.setParameter("nombre", "%"+buscarOpc.get()+"%");
        }*/
        //return query.getResultList();

        return resultado;
    }
}
