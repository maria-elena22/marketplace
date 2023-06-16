import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpParams, HttpErrorResponse} from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import { catchError, map,tap } from 'rxjs/operators';
import { NotificacaoDTO, SignUpDTO, UtilizadorDTO, UtilizadorInputDTO } from '../model/models';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';



@Injectable({
  providedIn: 'root'
})
export class UtilizadorService {

  backendUrl = environment.backendUrl;
  constructor(private http: HttpClient, private router: Router) {}

  getConsumidorById(id:number){
    console.log("entrei")
    const headers = new HttpHeaders();
    

    const url = `${this.backendUrl}/utilizador/consumidor/${id}`;
    console.log(url)

    return this.http.get<any>(url ,{ headers, observe: 'response' })
    .pipe(
        catchError((error) => {
        console.log('An error occurred:', error);
        console.log(error.error.message)
        console.log(error.error.exception)
        let errorMessage = '';
        if(error.error.message){
          errorMessage = error.error.message

        }
        return throwError(errorMessage);
      })
    );
  }

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

  getConsumidor(id:number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    
    const url = `${this.backendUrl}/utilizador/consumidor/${id}`;

    return this.http.get<UtilizadorDTO>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  getConsumidores(){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    
    const url = `${this.backendUrl}/utilizador/consumidor`;

    return this.http.get<Array<UtilizadorDTO>>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }
  getFornecedores(){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    
    const url = `${this.backendUrl}/utilizador/fornecedor`;

    return this.http.get<Array<UtilizadorDTO>>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  getFornecedor(id:number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    
    const url = `${this.backendUrl}/utilizador/fornecedor/${id}`;

    return this.http.get<UtilizadorDTO>(url ,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
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

  entregarNotificacao(idNotificacao:number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    
    const url = `${this.backendUrl}/notificacao/${idNotificacao}`;

    return this.http.put<any>(url, {} ,{ headers, observe: 'response' })
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
    return this.http.post<any>(url, consumidor, { headers, observe: 'response' })
    .pipe(
      catchError((error) => {
      console.log('An error occurred:', error);
      let errorMessage = '';
      if(error.error.exception && error.error.exception === "SignUpException"){
        errorMessage = "Email já existe no sistema. Faça login"
      }
      if(error.error.exception && error.error.exception === "DataIntegrityViolationException"){
        errorMessage = "Id Fiscal introduzido já existe no sistema"
      }
      return throwError(errorMessage);
    }));

  }

  insertFornecedor(fornecedor: SignUpDTO){
    const headers = new HttpHeaders();
    const url = `${this.backendUrl}/utilizador/register/fornecedor`;
    console.log(url)
    return this.http.post<any>(url, fornecedor, { headers, observe: 'response' })
    .pipe(
      catchError((error) => {
      console.log('An error occurred:', error);
      let errorMessage = '';
      if(error.error.exception && error.error.exception === "SignUpException"){
        errorMessage = "Email já existe no sistema. Faça login"
      }
      if(error.error.exception && error.error.exception === "DataIntegrityViolationException"){
          errorMessage = "Id Fiscal introduzido já existe no sistema"
      }
      return throwError(errorMessage);
    }));

  }

  updateFornecedor(fornecedor: UtilizadorInputDTO){

    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const url = `${this.backendUrl}/utilizador/fornecedor`;

    //const url = `${this.backendUrl}/utilizador/consumidor`;
  
    return this.http.put<any>(url, fornecedor,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );


  }

  updateConsumidor(consumidor: UtilizadorInputDTO){

    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const url = `${this.backendUrl}/utilizador/consumidor`;
  
    return this.http.put<any>(url, consumidor,{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );


  }

  removeConsumidor(idConsumidor :number){
    console.log("olaa")
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);


    const url = `${this.backendUrl}/utilizador/consumidor/deactivate/${idConsumidor}`;

    return this.http.put<any>(url,{},{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  removeFornecedor(idFornecedor :number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);


    const url = `${this.backendUrl}/utilizador/fornecedor/deactivate/${idFornecedor}`;

    return this.http.put<any>(url, {},{ headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }


  ativarConsumidor(idConsumidor :number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const url = `${this.backendUrl}/utilizador/consumidor/activate/${idConsumidor}`;

    return this.http.put<any>(url, {}, { headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  ativarFornecedor(idFornecedor :number){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const url = `${this.backendUrl}/utilizador/fornecedor/activate/${idFornecedor}`;

    return this.http.put<any>(url, {}, { headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          return throwError('Something went wrong');
        })
      );
  }

  getDetalhesUser(): Observable<HttpResponse<any>> {

    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = `${this.backendUrl}/utilizador/detalhes`;

    return this.http.get<any>(url, { headers, observe: 'response' })
      .pipe(
          catchError((error) => {
          console.log('An error occurred:', error);
          localStorage.removeItem('jwt_token');
          const currentUrl = this.router.url;
          console.log(currentUrl)
          if(currentUrl==='/marketplace'){
            window.location.reload();

          } else{
            this.router.navigateByUrl('');
            this.router.navigateByUrl('/marketplace');
          }
         
          return throwError('Something went wrong');
        })
      );
  }



}
