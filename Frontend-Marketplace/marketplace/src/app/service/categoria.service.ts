import { Injectable } from '@angular/core';
import { FullCategoriaDTO } from '../model/models';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) { }

  getCategorias(): Observable<HttpResponse<any>> {
    const headers = new HttpHeaders();

    return this.http.get<Array<FullCategoriaDTO>>(`${this.backendUrl}/categoria`, { headers, observe: 'response' });
  }

}