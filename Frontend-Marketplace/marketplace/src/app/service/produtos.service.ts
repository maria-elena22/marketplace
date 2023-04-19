import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map } from "rxjs";

@Injectable({providedIn: "root"})
export class ProdutosService{

    constructor(private http: HttpClient){

    }
    
    getProdutos(){
        // return this.http.get('http://grupo12.pt:8080/api/categoria').pipe(map(res => {return res}));
        // this.http.get('http://grupo12.pt:8080/api/categoria').subscribe(data => console.log(data));
    }
        
}
