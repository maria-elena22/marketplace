import { verifyHostBindings } from '@angular/compiler';
import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EncomendaDTO, EncomendaPaymentDTO, FullEncomendaDTO, FullProdutoDTO, FullSubEncomendaDTO, SimpleItemDTO, SimpleSubEncomendaDTO, SimpleUtilizadorDTO } from 'src/app/model/models';
import { CestoService } from 'src/app/service/cesto.service';
import { EncomendasService } from 'src/app/service/encomendas.service';
import { ProdutosService } from 'src/app/service/produtos.service';

@Component({
  selector: 'app-pagamento',
  templateUrl: './pagamento.component.html',
  styleUrls: ['./pagamento.component.css']
})
export class PagamentoComponent implements OnInit{
  error?:Error;
  todosProdutos: FullProdutoDTO[];
  encomenda: FullEncomendaDTO;
  subencomendas?: SimpleSubEncomendaDTO[];
  encomendaPayment:EncomendaPaymentDTO;
  pagamentoForm: FormGroup;
  showAnswer = false
  answer:string
  success:boolean


  constructor(private router: Router, private formBuilder: FormBuilder,private route: ActivatedRoute, private cestoService : CestoService, private encomendasService: EncomendasService, private produtosService: ProdutosService){}
  
  ngOnInit(): void {
      this.getProdutos();
      this.refresh();
      this.pagamentoForm = new FormGroup({
        nomeCartao: new FormControl('', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]),
        numeroCartao: new FormControl('', [Validators.required, Validators.pattern(/^[0-9]{16}$/)]),
        validade: new FormControl('', [Validators.required, Validators.pattern(/^\d{4}-\d{2}-\d{2}$/)]),
        cvc: new FormControl('', [Validators.required, Validators.pattern(/^[0-9]{3}$/)])
      });
  }

  refresh(){
    this.route.queryParams.subscribe((queryParams) => {
      
      let payments:EncomendaPaymentDTO[] = JSON.parse(localStorage.getItem('encomendaPayments')!)
      for(let payment of payments){
        console.log(payment)
        if(payment.encomendaDTO?.idEncomenda == queryParams["encomenda"]){
          this.encomendaPayment = payment
          console.log(this.encomendaPayment);
          
        }
      }
      this.getEncomenda(queryParams["encomenda"]);
    });
  }
  
  goToProdutos(){
    this.router.navigate(['/produtos'])
  }
  

  onSubmit() {
    localStorage.setItem("cartItems",JSON.stringify([]))
    const pagamentoData = {
      nomeCartao: this.pagamentoForm.value.nomeCartao,
      numeroCartao: this.pagamentoForm.value.numeroCartao,
      validade: this.pagamentoForm.value.validade,
      cvc: this.pagamentoForm.value.cvc
    }

    let validade = this.pagamentoForm.value.validade
    var nowDate = new Date(); 

    let verificarValidade = true;
    let arrayData = validade.split("-")
    if(arrayData.length>1){
      if(nowDate.getFullYear < arrayData[0]){
        verificarValidade = false;
      }else if(nowDate.getFullYear = arrayData[0] && nowDate.getMonth() +1 > parseInt(arrayData[1])){
        verificarValidade = false;
      } else if(nowDate.getFullYear = arrayData[0] && nowDate.getMonth() + 1 == parseInt(arrayData[1]) && nowDate.getDate() > parseInt(arrayData[2])){
        verificarValidade = false;
      }
    }else{
      verificarValidade = false;
    } 
    
    console.log(pagamentoData)
    if(this.pagamentoForm.valid && verificarValidade){

      this.cestoService.confirmPayment(this.encomendaPayment.encomendaDTO?.idEncomenda!,{clientSecret: this.encomendaPayment.stripeClientSecret}).subscribe(
        (obj)=>{
        const statusCode = obj.status
        if (statusCode === 200) {
          
          this.success=true;
          this.answer="Pagamento realizado com sucesso!"
          this.toggleAnswer()
          this.router.navigate(['/encomendas']);
        } 
      }, (error) => {
        // Handle error here
        console.log('An error occurred:', error);
        this.success = false
        this.answer = "Pagamento não foi feito. Verifique que preencheu todos os campo corretamente"
        this.toggleAnswer()
      }
      )
    }else if(!/^[0-9]{16}$/.test(this.pagamentoForm.value.numeroCartao)){
      this.success = false
      this.answer = "Número do cartão inválido!"
      this.toggleAnswer()
    }
    else if(!verificarValidade){
      this.success = false
      this.answer = "Data de validade do cartão inválida!"
      this.toggleAnswer()
    }
    else if(!/^[0-9]{3}$/.test(this.pagamentoForm.value.cvc)){
      this.success = false
      this.answer = "Número CVC inválido!"
      this.toggleAnswer()
    }
    else{
      this.success = false
      this.answer = "Pagamento não foi feito. Verifique que preencheu todos os campo corretamente"
      this.toggleAnswer()
    }

  } 

  toggleAnswer(){
    this.showAnswer = !this.showAnswer; 
  }

  getEncomenda(idEncomenda:number){
    this.encomendasService.getEncomendaById(idEncomenda).subscribe(obj=>{
      const statusCode = obj.status
          if (statusCode === 200) {
            this.encomenda = obj.body as FullEncomendaDTO;
            this.subencomendas = this.encomenda.subEncomendas || undefined;
        } else {
            this.error = obj.body as Error;
            //chamar pop up
        }
    })
   }

  getProdutos(){
    this.produtosService.getProdutos(-1,-1).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.todosProdutos = obj.body as FullProdutoDTO[];
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })
  }

  getProduto(produtoId:number){
    for(let produto of this.todosProdutos){
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

