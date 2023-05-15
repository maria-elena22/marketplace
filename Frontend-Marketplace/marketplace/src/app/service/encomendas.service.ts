import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpParams, HttpErrorResponse} from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError, map,tap } from 'rxjs/operators';
import { EncomendaDTO, FullEncomendaDTO, ItemInfoDTO } from '../model/models';

@Injectable({
  providedIn: 'root'
})
export class EncomendasService {

  constructor(private http: HttpClient) {}

  


  getEncomendas( page?: number, estadoEncomenda?:EncomendaDTO.EstadoEncomendaEnum, precoMin?:number, precoMax?:number,dataMin?:string, dataMax?:string): Observable<HttpResponse<any>> {
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    let params = new HttpParams();
    params = precoMin ? params.set('precoMin', precoMin) : params;
    params = precoMax ? params.set('precoMax', precoMax) : params;
    params = dataMin ? params.set('dataMin', dataMin) : params;
    params = dataMax ? params.set('dataMax', dataMax) : params;
    params = estadoEncomenda ? params.set('estadoEncomenda', estadoEncomenda) : params;
    params = page ? params.set('page', page) : params;

    const url = `https://grupo12.pt:8080/api/encomenda`;

    return this.http.get<Array<FullEncomendaDTO>>(url ,{ headers, params,observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  getSubEncomendas( page?: number, estadoEncomenda?:EncomendaDTO.EstadoEncomendaEnum, precoMin?:number, precoMax?:number,dataMin?:string, dataMax?:string): Observable<HttpResponse<any>> {
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    let params = new HttpParams();
    params = precoMin ? params.set('precoMin', precoMin) : params;
    params = precoMax ? params.set('precoMax', precoMax) : params;
    params = dataMin ? params.set('dataMin', dataMin) : params;
    params = dataMax ? params.set('dataMax', dataMax) : params;
    params = estadoEncomenda ? params.set('estadoEncomenda', estadoEncomenda) : params;
    params = page ? params.set('page', page) : params;

    const url = `https://grupo12.pt:8080/api/encomenda/subEncomendas`;

    return this.http.get<Array<FullEncomendaDTO>>(url ,{ headers, params,observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }


  getItensNaoEntregues(idTransporte:number,page?:number,size?:number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    let params = new HttpParams()
      .set('idTransporte', idTransporte);
    params = page ? params.set('page', page) : params;
    params = size ? params.set('size', size) : params;


    const url = `https://grupo12.pt:8080/api/encomenda/item`;

    return this.http.get<Array<ItemInfoDTO>>(url ,{ headers, params,observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );

  }

}
