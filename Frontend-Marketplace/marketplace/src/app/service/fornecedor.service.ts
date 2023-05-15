import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FornecedorService {

  constructor(private http: HttpClient){

  }
  //cria um consumidor na base de dados
  createFornecedor(fornecedor: {idFiscal:String ,nome:String}){
      // console.log(consumidor);
      const headers = new HttpHeaders();
      this.http.post<{name:string}>('https://localhost:8080/api/utilizador/fornecedor', fornecedor, {headers: headers})
      .subscribe((res) => {
          console.log(res);
      });
    }
}
