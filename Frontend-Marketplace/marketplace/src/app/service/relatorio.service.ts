import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpParams, HttpErrorResponse} from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError, map,tap } from 'rxjs/operators';
import { RelatorioPorDistanciasDTO, RelatorioPorZonasDTO } from '../model/models';

@Injectable({
  providedIn: 'root'
})
export class RelatorioService {

  constructor(private http: HttpClient) {}

  getRelatorioDistancias(): Observable<HttpResponse<any>> {
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const url = `https://grupo12.pt:8080/api/relatorio/distancia`;


    return this.http.get<RelatorioPorDistanciasDTO>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  getRelatorioZonas(): Observable<HttpResponse<any>> {
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const url = `https://grupo12.pt:8080/api/relatorio/zona`;


    return this.http.get<RelatorioPorZonasDTO>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }
}
