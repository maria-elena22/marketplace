import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpParams, HttpErrorResponse} from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError, map,tap } from 'rxjs/operators';
import { SignUpDTO, TransporteDTO, TransporteInputDTO, UniProdDTO, UniProdInputDTO } from '../model/models';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UniProdsService {

  backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) {}

  getUniProds(nomeUniProd?:string ): Observable<HttpResponse<any>> {
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    let params = new HttpParams();
    params = nomeUniProd ? params.set('nomeUniProd', nomeUniProd) : params;

    let paramString = params.toString();
    if(paramString != ''){
      paramString = '?'+paramString

    }
    const url = `${this.backendUrl}/uniProd${paramString}`;

    return this.http.get<Array<UniProdDTO>>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  insertUniProd(uniProd: UniProdInputDTO): Observable<HttpResponse<any>> {

    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = `${this.backendUrl}/uniProd`;

    return this.http.post<any>(url, uniProd,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  getTransportes(unidadeProducaoId?:number,estadoTransporte?:string, page?:number, size?:number): Observable<HttpResponse<any>> {
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    let params = new HttpParams();
    params = unidadeProducaoId ? params.set('unidadeProducaoId', unidadeProducaoId) : params;
    params = estadoTransporte ? params.set('estadoTransporte', estadoTransporte) : params;
    params = page ? params.set('page', page) : params;
    params = size ? params.set('size', size) : params;

    let paramString = params.toString();
    if(paramString != ''){
      paramString = '?'+paramString

    }
    const url = `${this.backendUrl}/transporte${paramString}`;

    return this.http.get<Array<UniProdDTO>>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  insertTransportes(transporte: TransporteInputDTO, uniProdId:number): Observable<HttpResponse<any>> {

    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);


    const url = `${this.backendUrl}/transporte/${uniProdId}`;

    return this.http.post<any>(url, transporte,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }



  updateUniProd(uniprod :UniProdInputDTO,idUniProd :number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);


    const url = `${this.backendUrl}/uniProd/${idUniProd}`;
  
    return this.http.put<any>(url, uniprod,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  updateTransporte(transporte :TransporteInputDTO,idTransporte :number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);


    const url = `${this.backendUrl}/transporte/${idTransporte}`;
  
    return this.http.put<any>(url, transporte,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }


  updateUniProdStock(idUniProd :number,stock:number, produtoId:number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    let params = new HttpParams();
    params = params.set('idUniProd', idUniProd);
    params = params.set('stock', stock);

    const url = `${this.backendUrl}/produto/unidade/stock/${produtoId}`;
  
    return this.http.put<any>(url, {},{ headers, params,observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  removeUniProd(idUniProd :number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);


    const url = `${this.backendUrl}/uniProd/deactivate/${idUniProd}`;

    return this.http.put<any>(url,{},{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  removeTransporte(idTransporte :number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);


    const url = `${this.backendUrl}/transporte/deactivate/${idTransporte}`;

    return this.http.put<any>(url,{},{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

}
