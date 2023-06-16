import { Component, OnInit } from '@angular/core';
import { ItemInfoDTO, ItemViagemDTO, SubItemViagemDTO, TransporteDTO, TransporteInputDTO, UniProdDTO, UniProdInputDTO, UtilizadorDTO, ViagemInputDTO } from 'src/app/model/models';
import{UniProdsService} from '../../service/uni-prods.service';
import { FormBuilder, FormGroup, FormControl, Validators, FormArray } from '@angular/forms';
import { AppComponent } from 'src/app/app.component';
import { UniProdsComponent } from '../uni-prods/uni-prods.component';
import { Router } from '@angular/router';
import { EncomendasService } from 'src/app/service/encomendas.service';
import { ViagemService } from 'src/app/service/viagem.service';
import { UtilizadorService } from 'src/app/service/utilizador.service';

@Component({
  selector: 'app-admin-utilizadores',
  templateUrl: './admin-utilizadores.component.html',
  styleUrls: ['./admin-utilizadores.component.css']
})
export class AdminUtilizadoresComponent implements OnInit{

  roleVisivel ='';
  consumidores : UtilizadorDTO[];
  fornecedores : UtilizadorDTO[];
  selectedUtilizador?: UtilizadorDTO;


  // page = 0
  // previousButtonDisabled = true
  // nextButtonDisabled = false
  error?:Error;
  showModal: boolean = false;
  
  showAnswer = false
  answer:string
  success:boolean



  constructor(private uniProdService:UniProdsService,private formBuilder: FormBuilder, private appComponent:AppComponent,
    private router : Router, private utilizadorService:UtilizadorService, private viagemService:ViagemService){}

  ngOnInit(){
    if(this.appComponent.token && this.appComponent.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser().subscribe()
    }

    
  }
  
  openUser(user:UtilizadorDTO){

    this.selectedUtilizador = user;
    this.showModal = true
    
  }

  
  handleAnswer(answer:string,success:boolean){
    this.answer = answer
    this.success = success
    this.showAnswer = true 
  }

  showRole(role:string){
    
    if(role === 'consumidor'){
      this.getConsumidores()
    }
    if(role === 'fornecedor'){
      this.getFornecedores()
    }
  }

    
  getConsumidores(){
    this.utilizadorService.getConsumidores().subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.consumidores = obj.body as UtilizadorDTO[];
        this.fornecedores = []
        this.roleVisivel = 'consumidor'
    } else {
        //this.error = obj.body as Error;
        //chamar pop up
    }
    })

  }

  showEstado(estadoBool:boolean){
    return estadoBool ? "Ativo" : "Inativo"
  }

  getFornecedores(){
    
    this.utilizadorService.getFornecedores().subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.fornecedores = obj.body as UtilizadorDTO[];
        this.consumidores = []
        this.roleVisivel = 'fornecedor'
    } else {
        //this.error = obj.body as Error;
    }
    })

  }


  toggleModal(){
    this.showModal = !this.showModal;
  }


  toggleAnswer(){
    this.showAnswer = !this.showAnswer;
  }
}

