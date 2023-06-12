import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FullProdutoDTO } from 'src/app/model/models';
import { ProdutosService } from 'src/app/service/produtos.service';

@Component({
  selector: 'app-produto-detalhes',
  templateUrl: './produto-detalhes.component.html',
  styleUrls: ['./produto-detalhes.component.css']
})
export class ProdutoDetalhesComponent implements OnInit{
  error?:Error;
  idProduto?: number;
  produto: FullProdutoDTO;
  listaProdutos: FullProdutoDTO[];

  constructor(private route: ActivatedRoute, private produtosService: ProdutosService){}
  
  ngOnInit(): void {
    this.getProduto();
    console.log(this.idProduto);
    console.log(this.produto);
    // this.refresh();
  }

  getProduto(){
    this.produtosService.getProdutos().subscribe(obj=>{
      const statusCode = obj.status;
      if (statusCode === 200) {
        this.listaProdutos = obj.body as FullProdutoDTO [];
        this.route.queryParams.subscribe((queryParams) => {
          for (let produto of this.listaProdutos){
            if(produto.idProduto == queryParams["produto"]){
              this.produto = produto;
              this.idProduto = produto.idProduto;
              console.log(this.produto)
              console.log(this.produto.precoFornecedores)
            }
          }
        })
        
      }else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    });
  }  
}
