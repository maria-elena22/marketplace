import { Injectable } from '@angular/core';
import { FullCategoriaDTO } from '../model/models';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  constructor(private http: HttpClient) { }

  getCategorias(): Observable<HttpResponse<any>> {
    const headers = new HttpHeaders();

    return this.http.get<Array<FullCategoriaDTO>>('https://grupo12.pt:8080/api/categoria', { headers, observe: 'response' });
  }

}