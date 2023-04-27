import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ProdutosService } from './service/produtos.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
  
})


export class AppComponent implements OnInit{
  title = 'marketplace';
  
  // products: this.getCategorias();

  constructor(private http: HttpClient, private produtosService: ProdutosService){

  }

  ngOnInit(): void {
  }

  getCategorias(){
    console.log("success");
    return this.produtosService.getCategorias();
  }

}





