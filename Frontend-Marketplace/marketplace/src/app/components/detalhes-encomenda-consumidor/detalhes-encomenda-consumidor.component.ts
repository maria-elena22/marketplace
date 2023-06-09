import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { EncomendaDTO, FullEncomendaDTO, FullProdutoDTO, FullSubEncomendaDTO, SimpleUtilizadorDTO } from 'src/app/model/models';
import { ProdutosService } from 'src/app/service/produtos.service';

@Component({
  selector: 'app-detalhes-encomenda-consumidor',
  templateUrl: './detalhes-encomenda-consumidor.component.html',
  styleUrls: ['./detalhes-encomenda-consumidor.component.css']
})
export class DetalhesEncomendaConsumidorComponent implements OnInit{
  @Input() showDetalhes: boolean;
  @Input() encomenda?: FullEncomendaDTO;
  @Input() subencomenda?:FullSubEncomendaDTO;
  produtos: FullProdutoDTO[];
  error?:Error;
  role : string

  constructor(private appComponent: AppComponent, private produtosService:ProdutosService, private router: Router){}

  ngOnInit(): void {
    this.getProdutos()
    console.log(this.encomenda)
    this.role = this.appComponent.role!
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['encomenda'] && changes['encomenda'].currentValue) {
      console.log(this.encomenda);
      console.log(this.subencomenda)
    }
    if (changes['subencomenda'] && changes['subencomenda'].currentValue) {
      console.log(this.subencomenda)
    }
  }

  ngAfterViewInit(): void {
    console.log(this.encomenda);
  }

  getMoradaUser(){
    return this.appComponent.user?.morada
  }
  
  porPagar(){
    return this.encomenda!.estadoEncomenda === FullEncomendaDTO.EstadoEncomendaEnum.PorPagar;
  }

  pagarEncomenda(){
    let queryParams = { encomenda: this.encomenda!.idEncomenda, produtos: JSON.stringify(this.encomenda?.subEncomendas)};
    localStorage.setItem("cartItems",JSON.stringify([]))
    this.router.navigate(['/pagamento'], { queryParams });
  }

  cancelarEncomenda(){
    this.produtosService.cancelEncomenda(this.encomenda?.idEncomenda!).subscribe(obj=>{
      const statusCode = obj.status
      window.location.reload()
    })

  }

  getProdutos(){
    this.produtosService.getProdutos(-1,-1).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.produtos = obj.body as FullProdutoDTO[];
    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }
    })
  }


  getProduto(produtoId:number){
    for(let produto of this.produtos){
      if(produto.idProduto === produtoId){
        return produto
      }
    }
    return null;
  }

  getPrecoProduto(produtoId:number,fornecedor:SimpleUtilizadorDTO){
    const produto = this.getProduto(produtoId)
    for(let pf of produto?.precoFornecedores!){
      if(pf.fornecedor?.idUtilizador === fornecedor.idUtilizador){
        return pf.preco!
      }
    }
    return 0
  }
}
