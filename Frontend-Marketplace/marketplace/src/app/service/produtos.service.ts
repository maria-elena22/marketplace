import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map } from "rxjs";

@Injectable({providedIn: "root"})
export class ProdutosService{

    constructor(private http: HttpClient){

    }
    
    getCategorias(){
        const headers = new HttpHeaders();
        this.http.get('http://localhost:8080/api/categoria', {headers: headers})
        .subscribe((res) => {
            return res;
        });
        // return this.http.get('http://grupo12.pt:8080/api/categoria').pipe(map(res => {return res}));
        // this.http.get('http://grupo12.pt:8080/api/categoria').subscribe(data => console.log(data));
    }
        
}
