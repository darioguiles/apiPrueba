package org.iesvdm.apitest.service;

import org.iesvdm.apitest.domain.AnuncioEmpresa;
import org.iesvdm.apitest.domain.AnuncioTrabajador;
import org.iesvdm.apitest.repository.AnuncioEmpresaRepository;
import org.iesvdm.apitest.repository.AnuncioTrabajadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnunciosService {

    @Autowired
    private AnuncioTrabajadorRepository anuncioTrabajadorRepository;
    @Autowired
    private AnuncioEmpresaRepository anuncioEmpresaRepository;

    // Obtener anuncios de Trabajador y Empresa con paginación
    public Map<String, Object> getAnuncios(int pagina, int tamanio, boolean soloTrabajadores, boolean soloEmpresas) {

        Pageable paginado = PageRequest.of(pagina, tamanio, Sort.by("idAnuncioTrabajador").ascending()); // Aquí también puedes cambiar el sorting según el campo

        Map<String, Object> response = new HashMap<>();

        // Si solo quieres anuncios de Trabajadores
        if (soloTrabajadores) {
            Page<AnuncioTrabajador> anunciosTrabajadores = anuncioTrabajadorRepository.findAll(paginado);
            response.put("anuncios", anunciosTrabajadores.getContent());
            response.put("currentPage", anunciosTrabajadores.getNumber());
            response.put("totalItems", anunciosTrabajadores.getTotalElements());
            response.put("totalPages", anunciosTrabajadores.getTotalPages());
        }
        // Si solo quieres anuncios de Empresas
        else if (soloEmpresas) {
            Page<AnuncioEmpresa> anunciosEmpresas = anuncioEmpresaRepository.findAll(paginado);
            response.put("anuncios", anunciosEmpresas.getContent());
            response.put("currentPage", anunciosEmpresas.getNumber());
            response.put("totalItems", anunciosEmpresas.getTotalElements());
            response.put("totalPages", anunciosEmpresas.getTotalPages());
        }
        // Si quieres ambos tipos de anuncios
        else {
            Page<AnuncioTrabajador> anunciosTrabajadores = anuncioTrabajadorRepository.findAll(paginado);
            Page<AnuncioEmpresa> anunciosEmpresas = anuncioEmpresaRepository.findAll(paginado);

            List<Object> todosAnuncios = new ArrayList<>();
            todosAnuncios.addAll(anunciosTrabajadores.getContent());
            todosAnuncios.addAll(anunciosEmpresas.getContent());

            response.put("anuncios", todosAnuncios);
            response.put("currentPage", anunciosTrabajadores.getNumber());
            response.put("totalItems", anunciosTrabajadores.getTotalElements() + anunciosEmpresas.getTotalElements());
            response.put("totalPages", anunciosTrabajadores.getTotalPages());
        }

        return response;
    }
}