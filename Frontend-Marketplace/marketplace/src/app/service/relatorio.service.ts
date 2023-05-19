import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpParams, HttpErrorResponse} from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError, map,tap } from 'rxjs/operators';
import { RelatorioPorDistanciasDTO, RelatorioPorZonasDTO } from '../model/models';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RelatorioService {


  backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) {}

  getRelatorioDistancias(): Observable<HttpResponse<any>> {
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const url = `${this.backendUrl}/relatorio/distancia`;


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

    const url = `${this.backendUrl}/relatorio/zona`;


    return this.http.get<RelatorioPorZonasDTO>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }
}
