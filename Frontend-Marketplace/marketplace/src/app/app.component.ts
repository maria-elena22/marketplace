import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ProdutosService } from './service/produtos.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
  
})


export class AppComponent implements OnInit{
  title = 'marketplace';

  products = this.getProdutos();

  constructor(private http: HttpClient, private produtosService: ProdutosService){

  }

  ngOnInit(): void {
  }

  getProdutos(){
    console.log("success");
    this.produtosService.getProdutos();
  }

}





