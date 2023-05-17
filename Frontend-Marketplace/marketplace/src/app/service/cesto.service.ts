import { Injectable } from '@angular/core';
import { CompraDTO, FullProdutoDTO, PaymentConfirmationRequest, SimpleItemDTO } from '../model/models';
import { Observable, throwError } from 'rxjs';
import { catchError, map,tap } from 'rxjs/operators';
import { HttpClient, HttpHeaders, HttpResponse, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CestoService {

  items:SimpleItemDTO[];

  constructor(private http: HttpClient){}


  addToCart(item:SimpleItemDTO) {
    this.items = localStorage.getItem('cartItems') === null ? [] : JSON.parse(localStorage.getItem('cartItems')!);

    this.items.push(item);
    localStorage.setItem('cartItems', JSON.stringify(this.items));
  }

  removeFromCart(item:SimpleItemDTO) {
    this.items = localStorage.getItem('cartItems') === null ? [] : JSON.parse(localStorage.getItem('cartItems')!);
    let keepGoing = true
    let idx = -1;
    console.log(this.items)
    console.log(item)
    for (let i = 0; i < this.items.length && keepGoing; i++) {
      const itemLS = this.items[i];
      console.log(itemLS)
      if((item.fornecedorId == itemLS.fornecedorId) && (item.produtoId == itemLS.produtoId) && (item.quantidade == itemLS.quantidade)){
        keepGoing = false
        idx = i;
        console.log(idx)
      }
      
    }


    this.items.splice(idx,1);
    localStorage.setItem('cartItems', JSON.stringify(this.items));
  }

  getItems() {
    const carrinho = localStorage.getItem('cartItems')
    if(carrinho){
      return JSON.parse(carrinho);

    }
    return [];

  }

  clearCart() {
    this.items = [];
    localStorage.removeItem('cartItems');
    return this.items;
  }


  addEncomenda(compra: CompraDTO){
    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const url = `https://34.30.176.39:8080/api/encomenda`;

    return this.http.post<any>(url, compra,{ headers, observe: 'response' })
      .pipe(
        catchError((error) => {
        console.log('An error occurred:', error);
        return throwError(error);
      })
    );
  }

  confirmPayment(encomendaId : number, request:PaymentConfirmationRequest){


    const token = localStorage.getItem('jwt_token');
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const url = `https://34.30.176.39:8080/api/encomenda/confirm/${encomendaId}`;
    console.log(url)

    return this.http.post<any>(url, request,{ headers, observe: 'response' })
      .pipe(
        catchError((error) => {
        console.log('An error occurred:', error);
        return throwError(error);
      })
    );

  }

}
