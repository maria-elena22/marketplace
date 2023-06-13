import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

import { UtilizadorService } from '../../service/utilizador.service';
import jwt_decode from 'jwt-decode';
import { Location } from '@angular/common';
import {DecodedToken} from '../../model/utilizador/decodedToken'
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SignUpDTO } from '../../model/utilizador/signUpDTO';
import { Pais } from 'src/app/model/utils/pais';
import { AppComponent } from 'src/app/app.component';
import { Coordinate, UtilizadorDTO, UtilizadorInputDTO } from 'src/app/model/models';

@Component({
  selector: 'app-definicoes',
  templateUrl: './definicoes.component.html',
  styleUrls: ['./definicoes.component.css']
})

export class DefinicoesComponent implements OnInit{
  utilizador?:UtilizadorDTO;

  role:string;
  showForm= false;
  noChanges = true;
  showAnswer = false
  answer:string
  success:boolean
  confimaRemover = false;

  constructor(private appComponent:AppComponent ,private http: HttpClient,
    private location: Location, private router: Router, private utilizadorService : UtilizadorService, private route: ActivatedRoute){
      
  }
  updateForm: FormGroup;
  countries = Object.keys(SignUpDTO.PaisEnum).filter(key => isNaN(Number(key)));
  continents = Object.keys(SignUpDTO.ContinenteEnum).filter(key => isNaN(Number(key)));


  ngOnInit() {

  
    this.refreshFunction()
  
    this.updateForm = new FormGroup({
      idFiscal: new FormControl('', Validators.required),
      nome: new FormControl('', Validators.required),
      telemovel: new FormControl('', Validators.required),
      latitude: new FormControl('', Validators.required),
      longitude: new FormControl('', Validators.required),
      morada: new FormControl('', Validators.required),
      freguesia: new FormControl('', Validators.required),
      municipio: new FormControl('', Validators.required),
      distrito: new FormControl('', Validators.required),
      pais: new FormControl('', Validators.required),
      continente: new FormControl('', Validators.required)

    });

    this.updateForm.patchValue({
      idFiscal: this.utilizador!.idFiscal,
      nome: this.utilizador!.nome,
      telemovel: this.utilizador!.telemovel,
      latitude: this.utilizador!.coordenadas?.latitude,
      longitude: this.utilizador!.coordenadas?.longitude,
      morada: this.utilizador!.morada,
      freguesia: this.utilizador!.freguesia,
      municipio: this.utilizador!.municipio,
      distrito: this.utilizador!.distrito,
      pais: this.utilizador!.pais,
      continente: this.utilizador!.continente

    });
    this.showForm=true;

    this.updateForm.valueChanges.subscribe(() => {
      this.noChanges = false;
    });

  }


openConfirmar(){
  this.confimaRemover = true
}
closeConfirmar(){
  this.confimaRemover = false
}

removerConta(){
  if(this.role === "ROLE_FORNECEDOR"){
    this.utilizadorService.removeFornecedor(this.utilizador?.idUtilizador!).subscribe(obj=>{
      const statusCode = obj.status
      console.log("-------------------")

      if (statusCode === 200) {
        this.answer = "Conta removida com sucesso"
        this.success = true;
        this.utilizador = obj.body
        this.appComponent.user!.nome! =this.utilizador!.nome!

        this.openAnswer();

    } 
  }
  )

  } 
  if (this.role === "ROLE_CONSUMIDOR"){
    this.utilizadorService.removeConsumidor(this.utilizador?.idUtilizador!).subscribe(obj=>{
      const statusCode = obj.status
      console.log("-------------------")
      console.log(statusCode)

      if (statusCode === 200) {
        this.answer = "Conta removida com sucesso"
        this.success = true;
        this.utilizador = obj.body
        this.appComponent.user!.nome! =this.utilizador!.nome!
        this.openAnswer();
        //window.location.reload(); 

    } 
  }
  )
  }
}


onPageRefresh(event: BeforeUnloadEvent): void {
  this.refreshFunction();
}

refreshFunction(): void {

  this.route.queryParams.subscribe((queryParams) => {
    console.log(JSON.parse(queryParams["user"]))

    this.role = queryParams["role"]
    this.utilizador = JSON.parse(queryParams["user"])
    console.log(this.utilizador)
    
  });

}


  onSubmit() {

    const coords : Coordinate = {latitude:this.updateForm.value.latitude,longitude:this.updateForm.value.longitude}
    const updateData: UtilizadorInputDTO = {
      idFiscal: this.updateForm.value.idFiscal,
      nome: this.updateForm.value.nome,
      telemovel: this.updateForm.value.telemovel,
      coordenadas: coords,
      morada: this.updateForm.value.morada,
      freguesia: this.updateForm.value.freguesia,
      municipio: this.updateForm.value.municipio,
      distrito: this.updateForm.value.distrito,
      pais: this.updateForm.value.pais,
      continente: this.updateForm.value.continente,
    }
    console.log(updateData);
    console.log(this.role);
    if(this.role === "ROLE_FORNECEDOR"){
      this.utilizadorService.updateFornecedor(updateData).subscribe(obj=>{
        const statusCode = obj.status
        console.log("-------------------")
  
        if (statusCode === 200) {
          this.answer = "Dados atualizados com sucesso!"
          this.success = true;
          this.utilizador = obj.body
          this.appComponent.user!.nome! =this.utilizador!.nome!

          this.openAnswer();
          
  
      } 
    }
    )

    } 
    if (this.role === "ROLE_CONSUMIDOR"){
      this.utilizadorService.updateConsumidor(updateData).subscribe(obj=>{
        const statusCode = obj.status
        console.log("-------------------")
        console.log(statusCode)
  
        if (statusCode === 200) {
          this.answer = "Dados atualizados com sucesso!"
          this.success = true;
          this.utilizador = obj.body
          this.appComponent.user!.nome! =this.utilizador!.nome!
          this.openAnswer();
  
      } 
    }
    )
    }

  }

  openAnswer(){
    this.showAnswer = true;
  }


  closeAnswer(){
    if(!this.confimaRemover){
      this.router.navigate(['/marketplace']);
      return;
    }

    this.showAnswer=false;
    let queryParams = {};

    queryParams = { role: this.role, user:JSON.stringify(this.utilizador!)};
    console.log(queryParams)
    this.router.navigate(['/marketplace/definicoes'], { queryParams });
  }

  

  
}
