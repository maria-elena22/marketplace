import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { EncomendaDTO, FullEncomendaDTO, FullProdutoDTO, FullSubEncomendaDTO, SimpleUtilizadorDTO } from 'src/app/model/models';
import { EncomendasService } from 'src/app/service/encomendas.service';
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

  showAnswer = false
  answer:string
  success:boolean

  constructor(private encomendasService:EncomendasService, private appComponent: AppComponent, private produtosService:ProdutosService, private router: Router){}

  ngOnInit(): void {
    this.getProdutos()
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
    let queryParams = { encomenda: this.encomenda!.idEncomenda};
    localStorage.setItem("cartItems",JSON.stringify([]))
    let data = this.encomenda!.dataEncomenda!.split('-');
    let date1 = new Date();
    let date2 = new Date(data[1] + "/" + data[2] + "/" + data[0]);
    let difference = date1. getTime() - date2. getTime();
    let TotalDays = Math. ceil(difference / (1000 * 3600 * 24)) - 1; 
    if(TotalDays > 15){
      this.success = false
      this.answer = "Prazo para pagamento foi ultrapassado! Encomenda Cancelada"
      this.produtosService.cancelEncomenda(this.encomenda?.idEncomenda!).subscribe(obj=>{
        const statusCode = obj.status
      })
      this.toggleAnswer()
    }else{
      this.router.navigate(['/marketplace/pagamento'], { queryParams });
    }
  }

  toggleAnswer(){
    if(!this.showAnswer){
      this.showAnswer = !this.showAnswer;
    }
    else{
      this.showAnswer = !this.showAnswer;
      window.location.reload()
    }
  }

  cancelarEncomenda(){
    this.produtosService.cancelEncomenda(this.encomenda?.idEncomenda!).subscribe(obj=>{
      const statusCode = obj.status
      window.location.reload()
    })

  }

  getProdutos(){
    this.produtosService.getProdutos(-1,-1,undefined,undefined,undefined,undefined,999999999).subscribe(obj=>{
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

  obterJSON(idEncomenda:number){
    
      this.encomendasService.getEncomendaById(idEncomenda).subscribe(obj => {
        const statusCode = obj.status;
        if (statusCode === 200) {
          this.generateFile(JSON.stringify(obj.body), idEncomenda);
        
        }
      });
  }


  generateFile(jsonContent: any, idEncomenda:number) {
    const blob = new Blob([jsonContent], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
  
    const link = document.createElement('a');
    link.href = url;
    link.download = `encomenda${idEncomenda}-${this.appComponent.user?.nome}.json`;
    
    document.body.appendChild(link);
    link.click();
    
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  }

}
