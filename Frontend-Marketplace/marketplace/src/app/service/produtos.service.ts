import { HttpClient, HttpHeaders, HttpResponse, HttpParams, HttpHandler} from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, throwError } from 'rxjs';
import { catchError, map,tap } from 'rxjs/operators';
import { EncomendaDTO, FullProdutoDTO, ProdutoFornecedorDTO, ProdutoPropriedadesDTO } from "../model/models";
import { environment } from "src/environments/environment";

@Injectable({providedIn: "root"})
export class ProdutosService{


    backendUrl = environment.backendUrl;

    constructor(private http: HttpClient){

    }

    getProdutos(categoriaId?:number, 
                subcategoriaId?:number,
                nomeProduto?:string,
                precoMin?:number,
                precoMax?:number,page?:number,size?:number): Observable<HttpResponse<any>> {
        const headers = new HttpHeaders();
        let params = new HttpParams();
        
        if(categoriaId == -1){
            categoriaId = undefined
        }
        if(subcategoriaId == -1){
            subcategoriaId = undefined
        }
        

        params = subcategoriaId ? params.set('subcategoriaId', subcategoriaId) : params;
        params = categoriaId ? params.set('categoriaId', categoriaId) : params;
        params = nomeProduto ? params.set('nomeProduto', nomeProduto) : params;
        params = precoMin ? params.set('precoMin', precoMin) : params;
        params = precoMax ? params.set('precoMax', precoMax) : params;
        params = page ? params.set('page', page) : params;
        params = size ? params.set('size', size) : params;


        let paramString = params.toString();
        if(paramString != ''){
            paramString = '?'+paramString

        }
        const url = `${this.backendUrl}/produto${paramString}`;
    
        return this.http.get<Array<FullProdutoDTO>>(url, { headers,observe: 'response' });
      }


      getMeusProdutos(categoriaId?:number, 
            subcategoriaId?:number,
            propriedadeId?:number,
            unidadeId?:number,
            nomeProduto?:string,
            precoMin?:number,
            precoMax?:number ,page?:number,size?:number): Observable<HttpResponse<any>> {
        const token = localStorage.getItem('jwt_token');
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        let params = new HttpParams();

        if(categoriaId == -1){
            categoriaId = undefined
        }
        if(subcategoriaId == -1){
            subcategoriaId = undefined
        }
        if(propriedadeId == -1){
            propriedadeId = undefined
        }
        params = subcategoriaId ? params.set('subcategoriaId', subcategoriaId) : params;
        params = categoriaId ? params.set('categoriaId', categoriaId) : params;
        params = propriedadeId ? params.set('propriedadeId', propriedadeId) : params;
        params = unidadeId ? params.set('unidadeId', unidadeId) : params;
        params = nomeProduto ? params.set('nomeProduto', nomeProduto) : params;
        params = precoMin ? params.set('precoMin', precoMin) : params;
        params = precoMax ? params.set('precoMax', precoMax) : params;
        params = page ? params.set('page', page) : params;
        params = size ? params.set('size', size) : params;

        let paramString = params.toString();
        if(paramString != ''){
            paramString = '?'+paramString

        }
        const url = `${this.backendUrl}/produto/fornecedor${paramString}`;

        return this.http.get<Array<ProdutoFornecedorDTO>>(url, { headers,observe: 'response' });
    }

    insertProduto(produto: ProdutoPropriedadesDTO, uniProdsIds:Array<number>, subCategoriasIds:Array<number>, preco : number): Observable<HttpResponse<any>> {
        const token = localStorage.getItem('jwt_token');
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        const url = `${this.backendUrl}/produto`;
        if(uniProdsIds.length === 0){
            return throwError("Deve associar pelo menos uma unidade de produção");
        }
        if(subCategoriasIds.length === 0){
            return throwError("Deve associar pelo menos uma subcategoria");
        }

        let params = new HttpParams()
        params = params.set('uniProdsIds',uniProdsIds.toString())
        params = params.set('subCategoriasIds', subCategoriasIds.toString())
        params = params.set('preco',preco)


        return this.http.post<any>(url, produto,{ headers, params ,observe: 'response' })
          .pipe(
              catchError((error) => {
              console.log('An error occurred:', error);
              return throwError(error);
            })
          );
      }

    cancelEncomenda(idEncomenda:number){
        const token = localStorage.getItem('jwt_token');
        let headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      
        const url = `${this.backendUrl}/encomenda/${idEncomenda}`;

        return this.http.put<EncomendaDTO>(url, {},{ headers,observe: 'response' })
        .pipe(
            catchError((error) => {
            console.log('An error occurred:', error);
            return throwError('Something went wrong');
            })
        );
    }
    

    addUniProds(produtoId:number,uniProdsIds:Array<number>,preco:number,stock:number){
        const token = localStorage.getItem('jwt_token');
        let headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        var params = new HttpParams()
        params = params.set('uniProdsIds',uniProdsIds.toString());
        params = params.set('preco',preco);
        params = params.set('stock',stock);
      
        const url = `${this.backendUrl}/produto/unidade/${produtoId}`;

        return this.http.put<FullProdutoDTO>(url, {},{ headers ,params,observe: 'response' })
        .pipe(
            catchError((error) => {
            console.log('An error occurred:', error);
            return throwError('Something went wrong');
            })
        );
    }

    removeProduto(produtoId:number,uniProdsIds:Array<number>){
        let ids = uniProdsIds.toString();
        const token = localStorage.getItem('jwt_token');
        let headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
        var params1 = new HttpParams().set('uniProdsIds',uniProdsIds.toString());
        let produto = produtoId.toString()
 
        let paramString = params1.toString();
        if(paramString != ''){
            paramString = '?'+paramString

        }
    
        const url = `${this.backendUrl}/produto/unidade/remove/${produto}${paramString}`;

        return this.http.put<FullProdutoDTO>(url, {},{ headers ,observe: 'response' })
        .pipe(
            catchError((error) => {
            console.log('An error occurred:', error);
            return throwError('Something went wrong');
            })
        );
    }


    getCategorias(){
        const headers = new HttpHeaders();
        this.http.get(`${this.backendUrl}/categoria`, {headers: headers})
        .subscribe((res) => {
            return res;
        });

    }
        
}
