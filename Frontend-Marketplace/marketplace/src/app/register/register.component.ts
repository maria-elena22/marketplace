import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ConsumidorService } from '../service/consumidor.service';
import { FornecedorService } from '../service/fornecedor.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

export class RegisterComponent {
  constructor(private http: HttpClient, private consumidoresService: ConsumidorService, private fornecedoresService: FornecedorService, private router: Router){

  }

  ngOnInit(): void {
  }

  createUser(){
    console.log("success");
    const input = document.getElementById('filtraSelect') as HTMLInputElement;
    // console.log(input.value);
    
    const nome = document.getElementById('nome') as HTMLInputElement;
    // console.log(nome.value)
    const idFiscal = document.getElementById('idFiscal') as HTMLInputElement;
    // console.log(idFiscal.value)

    if(input.value === "consumidor"){
      this.createConsumidor(idFiscal.value, nome.value);
    }else{
      this.createFornecedor(idFiscal.value, nome.value);
    }
  }

  createConsumidor(idFiscal:String, nome:String){
    console.log("Vamos criar um consumidor");
    this.consumidoresService.createConsumidor({idFiscal, nome});
    this.router.navigate(['/admin-consumidor']);
  }

  createFornecedor(idFiscal:String, nome:String){
    console.log("Vamos criar um fornecedor");
    this.fornecedoresService.createFornecedor({idFiscal, nome});
    this.router.navigate(['/admin-fornecedor']);
  }
}
