import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AnuncioService {
  private apiUrl = 'http://localhost:8080/v1/data-api/anuncios';

  constructor(private http: HttpClient) {}

  getAnuncios(soloTrabajadores: boolean, soloEmpresas: boolean, pageSize: number, currentPage: number): Observable<any> {
    let params = new HttpParams()
      .set('soloTrabajadores', soloTrabajadores.toString())
      .set('soloEmpresas', soloEmpresas.toString())
      .set('page', currentPage.toString())
      .set('size', pageSize.toString());

    return this.http.get<any>(this.apiUrl, { params });
  }
}
