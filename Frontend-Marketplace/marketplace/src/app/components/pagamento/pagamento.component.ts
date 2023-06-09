import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EncomendaDTO, EncomendaPaymentDTO, FullEncomendaDTO, FullProdutoDTO, FullSubEncomendaDTO } from 'src/app/model/models';
import { CestoService } from 'src/app/service/cesto.service';

@Component({
  selector: 'app-pagamento',
  templateUrl: './pagamento.component.html',
  styleUrls: ['./pagamento.component.css']
})
export class PagamentoComponent implements OnInit{
  encomendaPayment:EncomendaPaymentDTO;
  pagamentoForm: FormGroup;
  showAnswer = false
  answer:string
  success:boolean


  constructor(private router: Router, private formBuilder: FormBuilder,private route: ActivatedRoute, private cestoService : CestoService){}
  
  ngOnInit(): void {
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
      console.log(queryParams["encomenda"])
      // this.produtos = JSON.parse(queryParams["produtos"]);
      // console.log(JSON.parse(queryParams["produtos"])[0]["fornecedor"]["nome"])
      // for(let produto of JSON.parse(queryParams["produtos"])){
      //   console.log(produto);
      // }
      let payments:EncomendaPaymentDTO[] = JSON.parse(localStorage.getItem('encomendaPayments')!)
      for(let payment of payments){
        if(payment.encomendaDTO?.idEncomenda == queryParams["encomenda"]){
          this.encomendaPayment = payment
          console.log(this.encomendaPayment);
        }
      }


    });
  }
  
  goToProdutos(){
    this.router.navigate(['/produtos'])
  }

  onSubmit() {
    const pagamentoData = {
      nomeCartao: this.pagamentoForm.value.nomeCartao,
      numeroCartao: this.pagamentoForm.value.numeroCartao,
      validade: this.pagamentoForm.value.validade,
      cvc: this.pagamentoForm.value.cvc
    }

    console.log(pagamentoData)
    if(!this.pagamentoForm.valid){
      this.success = false
      this.answer = "Pagamento não foi feito. Verifique que preencheu todos os campo corretamente"
      this.toggleAnswer()
    }
    this.cestoService.confirmPayment(this.encomendaPayment.encomendaDTO?.idEncomenda!,{clientSecret: this.encomendaPayment.stripeClientSecret}).subscribe(
      (obj)=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        
        this.success=true;
        this.answer="Pagamento realizado com sucesso!"
        this.toggleAnswer()

      } 
    }, (error) => {
      // Handle error here
      console.log('An error occurred:', error);
      this.success = false
      this.answer = "Pagamento não foi feito. Verifique que preencheu todos os campo corretamente"
      this.toggleAnswer()
    }
    )

  } 

  toggleAnswer(){
    this.showAnswer = !this.showAnswer; 
  }

}
