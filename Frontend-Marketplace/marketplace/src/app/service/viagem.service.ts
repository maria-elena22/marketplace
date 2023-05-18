import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpParams, HttpErrorResponse} from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError, map,tap } from 'rxjs/operators';
import { NotificacaoDTO, SignUpDTO, SubItemDTO, TransporteDTO, TransporteInputDTO, UniProdDTO, UniProdInputDTO, ViagemDTO, ViagemInputDTO } from '../model/models';

@Injectable({
  providedIn: 'root'
})
export class ViagemService {

  constructor(private http: HttpClient) {}


  getViagens(idTransporte?:number, page?:number, size?:number): Observable<HttpResponse<any>> {
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    let params = new HttpParams();
    // params = idTransporte ? params.set('idTransporte', idTransporte) : params;
    params = page ? params.set('page', page) : params;
    params = size ? params.set('size', size) : params;

    let paramString = params.toString();
    if(paramString != ''){
      paramString = '?'+paramString

    }
    const url = `https://grupo12.pt:8080/api/viagem/transporte/${idTransporte}`;

    return this.http.get<Array<ViagemDTO>>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  iniciaViagem(subItemsIds:Array<number>){


    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    let params = new HttpParams();
    params = params.set('subItemsIds', subItemsIds.toString());
    let paramString = params.toString();


    const url = `https://grupo12.pt:8080/api/notificacao/saida?${paramString}`;

    return this.http.post<Array<NotificacaoDTO>>(url,{},{ headers,observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );

  }

  terminaViagem(subItemId:number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);


    let params = new HttpParams();
    params = params.set('subItemId', subItemId);
    let paramString = params.toString();

    const url = `https://grupo12.pt:8080/api/notificacao/chegada?${paramString}`;
    
    return this.http.post<Array<NotificacaoDTO>>(url,{},{ headers,observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  getSubItemsNaoEntregues(): Observable<HttpResponse<any>> {
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    let params = new HttpParams();
    // params = idTransporte ? params.set('idTransporte', idTransporte) : params;
    params.set('page', 0);
    params.set('size', 100000);

    let paramString = params.toString();
    if(paramString != ''){
      paramString = '?'+paramString

    }
    const url = `https://grupo12.pt:8080/api/encomenda/subItem${paramString}`;

    return this.http.get<Array<SubItemDTO>>(url ,{ headers,params, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }
  

  insertViagem(viagem: ViagemInputDTO): Observable<HttpResponse<any>> {

    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);


    const url = "https://grupo12.pt:8080/api/viagem";

    console.log(viagem)
    
    return this.http.post<ViagemDTO>(url, viagem,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

}
