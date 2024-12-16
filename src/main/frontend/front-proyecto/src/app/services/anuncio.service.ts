import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnuncioService {
  private apiUrl = 'http://localhost:8080/v1/data-api/anuncios';

  constructor(private http: HttpClient) {}

  // Obtener anuncios solo de trabajadores POR DEFECTO
  getAnunciosTrabajadores(pageSize: number, currentPage: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/trabajadores`, {
      params: new HttpParams()
        .set('pagina', currentPage.toString())
        .set('tamanio', pageSize.toString())
    });
  }

  // Obtener anuncios solo de empresas
  getAnunciosEmpresas(pageSize: number, currentPage: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/empresas`, {
      params: new HttpParams()
        .set('pagina', currentPage.toString())
        .set('tamanio', pageSize.toString())
    });
  }
}
