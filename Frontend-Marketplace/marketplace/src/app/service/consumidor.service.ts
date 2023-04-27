//TESTE
import { HttpClient, HttpHeaders } from "@angular/common/http";

import { Injectable } from "@angular/core";

@Injectable({providedIn: "root"})
export class ConsumidorService{
    constructor(private http: HttpClient){

    }
    //cria um consumidor na base de dados
    createConsumidor(consumidor: {idFiscal:String ,nome:String}){
        // console.log(consumidor);
        const headers = new HttpHeaders();
        this.http.post<{name:string}>('http://localhost:8080/api/utilizador/consumidor', consumidor, {headers: headers})
        .subscribe((res) => {
            console.log(res);
        });


        //funciona
        // this.http.post<Article>('http://localhost:8080/api/utilizador/fornecedor', { title: 'Angular POST Request Example' }).subscribe();
            
        //     interface Article {
        //         id: number;
        //         title: string;
        // }
    }

    //apaga um consumidor da base de dados
    deleteConsumidor(){

    }

    teste(){          
        this.http.delete('http://localhost:8080/api/utilizador/fornecedor/16').subscribe();
    }
        
}