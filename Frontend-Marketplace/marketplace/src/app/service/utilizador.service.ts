import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpParams, HttpErrorResponse} from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError, map,tap } from 'rxjs/operators';
import { NotificacaoDTO, SignUpDTO } from '../model/models';



@Injectable({
  providedIn: 'root'
})
export class UtilizadorService {


  constructor(private http: HttpClient) {}

  getLogin(email:string, password:string): Observable<HttpResponse<any>> {

    const headers = new HttpHeaders();
    
    // https://34.30.176.39:8080/api/utilizador/login?email=cont%40gmail.com&password=Cont%401234

    const url = `https://34.30.176.39:8080/api/utilizador/login?email=${email}&password=${password}`;
    console.log(url)
    return this.http.get<any>(url, { headers, observe: 'response'});


  }

  getNotificacoes(){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    
    const url = `https://34.30.176.39:8080/api/notificacao`;

    return this.http.get<Array<NotificacaoDTO>>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  getNotificacoesNum(){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    
    const url = `https://34.30.176.39:8080/api/notificacao/num`;

    return this.http.get<number>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  insertConsumidor(consumidor: SignUpDTO): Observable<HttpResponse<any>> {
    const headers = new HttpHeaders();
    const url = `https://34.30.176.39:8080/api/utilizador/register/consumidor`;
    console.log(url)
    return this.http.post<any>(url, consumidor, { headers, observe: 'response' });

  }

  insertFornecedor(fornecedor: SignUpDTO){
    const headers = new HttpHeaders();
    const url = `https://34.30.176.39:8080/api/utilizador/register/fornecedor`;
    console.log(url)
    return this.http.post<any>(url, fornecedor, { headers, observe: 'response' });

  }

  getDetalhesUser(): Observable<HttpResponse<any>> {

    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = 'https://34.30.176.39:8080/api/utilizador/detalhes';

    return this.http.get<any>(url, { headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }



}
