import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpParams, HttpErrorResponse} from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError, map,tap } from 'rxjs/operators';
import { NotificacaoDTO, SignUpDTO } from '../model/models';
import { environment } from 'src/environments/environment';



@Injectable({
  providedIn: 'root'
})
export class UtilizadorService {

  backendUrl = environment.backendUrl;

  constructor(private http: HttpClient) {}

  getLogin(email:string, password:string): Observable<HttpResponse<any>> {

    const headers = new HttpHeaders();
    
    // ${this.backendUrl}/utilizador/login?email=cont%40gmail.com&password=Cont%401234

    const url = `${this.backendUrl}/utilizador/login?email=${email}&password=${password}`;
    console.log(url)

    return this.http.get<any>(url ,{ headers, observe: 'response' })
    .pipe(
        catchError((error) => {
        console.log('An error occurred:', error);
        console.log(error.error.message)
        console.log(error.error.exception)
        let errorMessage = '';
        if(error.error.exception && error.error.exception === "EntityNotFoundException"){
          errorMessage = "Não está registado no sistema"
        }
        if(error.error.message){
          errorMessage = error.error.message

        }
        if(error.error.exception && error.error.exception === "BadCredentialsException"){
          errorMessage = "Credenciais inválidas"
        }
        return throwError(errorMessage);
      })
    );


  }

  getNotificacoes(){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    
    const url = `${this.backendUrl}/notificacao`;

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
    
    const url = `${this.backendUrl}/notificacao/num`;

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
    const url = `${this.backendUrl}/utilizador/register/consumidor`;
    console.log(url)
    return this.http.post<any>(url, consumidor, { headers, observe: 'response' });

  }

  insertFornecedor(fornecedor: SignUpDTO){
    const headers = new HttpHeaders();
    const url = `${this.backendUrl}/utilizador/register/fornecedor`;
    console.log(url)
    return this.http.post<any>(url, fornecedor, { headers, observe: 'response' });

  }

  getDetalhesUser(): Observable<HttpResponse<any>> {

    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = `${this.backendUrl}/utilizador/detalhes`;

    return this.http.get<any>(url, { headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }



}
