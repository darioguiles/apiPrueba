import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnuncioService {
  private apiUrl = 'http://localhost:8080/v1/data-api/anuncio';

  constructor(private http: HttpClient) {}

  // Obtener anuncios solo de trabajadores POR DEFECTO
  getAnunciosTrabajadores(pageSize: number, currentPage: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}trabajadores`, {
      params: new HttpParams()
        .set('pagina', currentPage.toString())
        .set('tamanio', pageSize.toString())
    });
  }

  // Obtener anuncios solo de empresas
  getAnunciosEmpresas(pageSize: number, currentPage: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}empresa`, {
      params: new HttpParams()
        .set('pagina', currentPage.toString())
        .set('tamanio', pageSize.toString())
    });
  }

  // Crear un anuncio
  crearAnuncio(anuncio: any): Observable<any> {
    let endpoint = '';

    if (anuncio.trabajador) {
      endpoint = `${this.apiUrl}trabajadores`;
    } else if (anuncio.empresa) {
      endpoint = `${this.apiUrl}empresa`;
    } else {
      throw new Error('Debe especificarse un trabajador o empresa para el anuncio.');
    }

    return this.http.post<any>(endpoint, anuncio, {
      headers: { 'Content-Type': 'application/json' }
    });
  }

  // Editar un anuncio existente
  editarAnuncio(id: number, anuncio: any): Observable<any> {
    let endpoint = '';

    if (anuncio.trabajador) {
      endpoint = `${this.apiUrl}/anunciostrabajador/${id}`;
    } else if (anuncio.empresa) {
      endpoint = `${this.apiUrl}/anunciosempresa/${id}`;
    } else {
      throw new Error('Debe especificarse un trabajador o empresa para editar el anuncio.');
    }

    return this.http.put<any>(endpoint, anuncio, {
      headers: { 'Content-Type': 'application/json' }
    });
  }

  borrarAnuncio(id: number, anuncio: any):Observable<any> {
    let endpoint = '';

    if (anuncio.trabajador) {
      endpoint = `${this.apiUrl}/anunciostrabajador/${id}`;

    } else if (anuncio.empresa) {
      endpoint = `${this.apiUrl}/anunciosempresa/${id}`;
    } else {
      throw new Error('Debe especificarse un trabajador o empresa para Borrar el anuncio.');
    }

    return this.http.delete<any>(endpoint, {
      headers: { 'Content-Type': 'application/json' }
    });

  }
}
