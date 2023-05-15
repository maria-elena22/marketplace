import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EncomendaDTO, EncomendaPaymentDTO } from 'src/app/model/models';
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
      console.log(queryParams)
      let payments:EncomendaPaymentDTO[] = JSON.parse(localStorage.getItem('encomendaPayments')!)
      for(let payment of payments){
        if(payment.encomendaDTO?.idEncomenda == queryParams["encomenda"]){
          this.encomendaPayment = payment
        }
      }


    });
  }
  
  onSubmit() {
    
    const pagamentoData = {
      nomeCartao: this.pagamentoForm.value.nomeCartao,
      numeroCartao: this.pagamentoForm.value.numeroCartao,
      validade: this.pagamentoForm.value.validade,
      cvc: this.pagamentoForm.value.cvc
    }

    console.log(pagamentoData)
    this.cestoService.confirmPayment(this.encomendaPayment.encomendaDTO?.idEncomenda!,{clientSecret: this.encomendaPayment.stripeClientSecret}).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        
        this.success=true;
        this.answer="Pagamento realizado com sucesso!"
        this.toggleAnswer()

      } 
    })

  } 

  toggleAnswer(){
    if(this.showAnswer){
      this.router.navigate(['/encomendas']);
      this.showAnswer = !this.showAnswer;

    } else {    
      this.showAnswer = !this.showAnswer;
    }  
  }

}
