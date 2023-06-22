import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { CompraDTO, EncomendaPaymentDTO, FullProdutoDTO, SimpleItemDTO, SimpleUtilizadorDTO } from 'src/app/model/models';
import { CestoService } from 'src/app/service/cesto.service';
import { ProdutosService } from 'src/app/service/produtos.service';
import { UtilizadorService } from 'src/app/service/utilizador.service';

@Component({
  selector: 'app-cesto',
  templateUrl: './cesto.component.html',
  styleUrls: ['./cesto.component.css']
})
export class CestoComponent implements OnInit{

  carrinho:SimpleItemDTO[];
  items: { produto: FullProdutoDTO, fornecedor: SimpleUtilizadorDTO, quantidade: number , preco:string }[] = [];
  produtos:FullProdutoDTO[]

  // resposta
  showAnswer = false
  success? : boolean
  answer?:string
  error?: Error

  constructor(private appComponent:AppComponent,public cestoService:CestoService, private produtosService:ProdutosService,
    private router: Router,private route: ActivatedRoute, private utilizadorService:UtilizadorService){}

  ngOnInit(): void {
    if(this.appComponent.token && this.appComponent.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser().subscribe()
    }

    this.getProdutos(-1,-1)



  }

  goToProdutos(){
    this.router.navigate(['/marketplace/produtos'])
  }

  getMinhaMorada(){
    return this.appComponent.user?.morada;
  }

  handleItems(){
    let item;
    for(let simpleItem of this.carrinho){
      let produto = this.getProduto(simpleItem.produtoId!)
      if(produto ){
        let fornecedor = this.getFornecedor(produto,simpleItem.fornecedorId!)
        if(fornecedor){
          item = { produto: produto, fornecedor: fornecedor!, quantidade:simpleItem.quantidade!, preco:this.getPreco(produto,fornecedor!) }
          this.items.push(item)
        } else{
          this.cestoService.removeFromCart(simpleItem);
          this.items = []
          this.getProdutos(-1,-1)
        }

      }
    }
  }

  removeItem(item:any){
    let simpleItem = {produtoId: item.produto.idProduto, fornecedorId: item.fornecedor.idUtilizador, quantidade: item.quantidade}
    this.cestoService.removeFromCart(simpleItem);
    this.items = []
    this.getProdutos(-1,-1)
  }

  getPreco(produto:FullProdutoDTO, fornecedor:SimpleUtilizadorDTO){
    for(let pf of produto.precoFornecedores!){
      const preco = pf.preco;
      if(pf.fornecedor?.idUtilizador === fornecedor.idUtilizador){
        return preco!.toFixed(2);
      }
    }
    return '0';
  }
  totalProduto(item: { produto: FullProdutoDTO, fornecedor: SimpleUtilizadorDTO, quantidade: number, preco: string }): string {
    const quantidade = item.quantidade;
    const preco = parseFloat(item.preco);
    const produtoTotal = quantidade * preco;

    return produtoTotal.toFixed(2);
  }
  totalCarrinho():string{
    let total = 0;
    for(let item of this.items){
      total += (parseFloat(item.preco) * item.quantidade)
    }
    return total.toFixed(2);
  }

  getProduto(idProduto:number){
    for(let produto of this.produtos){
      if(produto.idProduto === idProduto){
        return produto
      }
    }
    return null;
  }

  getFornecedor(produto:FullProdutoDTO, idFornecedor:number){
    for(let pf of produto.precoFornecedores!){
      const fornecedor = pf.fornecedor
      if(fornecedor?.idUtilizador === idFornecedor){
        return fornecedor
      }
    }
    return null;
  }

  getProdutos(idCategoria:number,idSubCategoria:number){
    this.produtosService.getProdutos(idCategoria,idSubCategoria, undefined,undefined,undefined,undefined,999999999).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.produtos =  obj.body as FullProdutoDTO[];
        this.carrinho = this.cestoService.getItems();
        this.handleItems();
    }
    })
  }

  toggleAnswer(){
    if(!this.showAnswer){
      if(this.success){
        this.showAnswer = true
      }else{
        this.showAnswer = true
      }
    }else{
      this.showAnswer = false
    }
  }


  criaEncomenda(){
    if(this.appComponent.user){
      const compraData : CompraDTO = {
        items: this.carrinho,
        preco: parseFloat(this.totalCarrinho())
      }
      this.cestoService.addEncomenda(compraData).subscribe(obj=>{
        const statusCode = obj.status
        if (statusCode === 200) {
          let payments = localStorage.getItem('encomendaPayments') === null ? [] : JSON.parse(localStorage.getItem('encomendaPayments')!);
          payments.push(obj.body as EncomendaPaymentDTO);
          localStorage.setItem('encomendaPayments', JSON.stringify(payments));
          const body = obj.body as EncomendaPaymentDTO;
          let queryParams = { encomenda: body.encomendaDTO?.idEncomenda};
          localStorage.setItem("cartItems",JSON.stringify([]))

          this.router.navigate(['/marketplace/pagamento'], { queryParams });


          // const state = { page: 'cesto' };
          // const url = '/cesto';

          // window.history.pushState(state, url);

      }
      })
    } else{
      this.success = false;
      this.answer = "Deve aceder com a sua conta para fazer uma encomenda!"
      this.toggleAnswer()
    }
  }
}
