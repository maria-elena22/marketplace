import { Component, OnInit } from '@angular/core';
import { FullEncomendaDTO, FullSubEncomendaDTO } from 'src/app/model/models';
import { EncomendasService } from 'src/app/service/encomendas.service';
import { DetalhesEncomendaConsumidorComponent } from '../detalhes-encomenda-consumidor/detalhes-encomenda-consumidor.component';
import { AppComponent } from 'src/app/app.component';
import { UtilizadorService } from 'src/app/service/utilizador.service';

@Component({
  selector: 'app-encomendas',
  templateUrl: './encomendas.component.html',
  styleUrls: ['./encomendas.component.css']
})
export class EncomendasComponent implements OnInit {


  encomendas: FullEncomendaDTO[];
  subencomendas: FullSubEncomendaDTO[]
  error?:Error;
  page = 0
  showModal: boolean = false;
  previousButtonDisabled = true
  nextButtonDisabled = false
  encomendaDestaque?: FullEncomendaDTO
  subencomendaDestaque?:FullSubEncomendaDTO
  showAnswer = false
  answer:string
  success:boolean
  proximasEncomendas: number;

  constructor(private encomendasService:EncomendasService, private appComponent:AppComponent, private utilizadorService:UtilizadorService){}

  ngOnInit(): void {
    if(this.appComponent.token && this.appComponent.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser().subscribe()
    }
    console.log(this.appComponent.role)
    if(this.appComponent.role === "ROLE_CONSUMIDOR"){
      this.getProximasEncomendas(this.page + 1);
      if(this.proximasEncomendas == 0){
        this.nextButtonDisabled = true;
      }
      this.getEncomendas();
    } 
    if(this.appComponent.role === "ROLE_FORNECEDOR"){
      this.getProximasSubEncomendas(this.page + 1);
      if(this.proximasEncomendas == 0){
        this.nextButtonDisabled = true;
      }
      this.getSubEncomendas();
    }
  }

  nextPage(){
    this.page +=1
    if(this.appComponent.role === "ROLE_CONSUMIDOR"){
      this.getProximasEncomendas(this.page + 2)
      console.log(this.proximasEncomendas)
      if(this.proximasEncomendas == 0){
        this.nextButtonDisabled = true;
      }
      this.getEncomendas();
    } 
    if(this.appComponent.role === "ROLE_FORNECEDOR"){
      this.getProximasSubEncomendas(this.page + 2)
      console.log(this.proximasEncomendas)
      if(this.proximasEncomendas == 0){
        this.nextButtonDisabled = true;
      }
      this.getSubEncomendas();
    }
    const state = { page: 'encomendas' };
    const url = '/marketplace/encomendas';
    this.previousButtonDisabled = false
    window.history.pushState(state, url);
  }

  previousPage(){
    this.page -=1
    this.getProximasEncomendas(this.page - 1)
    if(this.proximasEncomendas == 0){  
      this.page += 1
      this.previousButtonDisabled = true

    } else{
      if(this.appComponent.role === "ROLE_CONSUMIDOR"){
        this.getEncomendas();
      } 
      if(this.appComponent.role === "ROLE_FORNECEDOR"){
        this.getSubEncomendas();
      }
      const state = { page: 'encomendas' };
      const url = '/marketplace/encomendas';
      this.nextButtonDisabled = false
      window.history.pushState(state, url);

    }
    
  }

  getEncomendas(){
    // console.log("+++++++++++")
    // console.log(this.page)
    // console.log("+++++++++++")

    this.encomendasService.getEncomendas(this.page).subscribe(obj=>{
      const statusCode = obj.status
  
      if (statusCode === 200) {
        this.encomendas = obj.body as FullEncomendaDTO[];

        if(this.encomendas.length ===0 && this.page>0){
          this.page -=1
          this.nextButtonDisabled = true
          this.getEncomendas()
        }
    } else {
        this.error = obj.body as Error;
    }
    })
  }

  getSubEncomendas(){
    // console.log("+++++++++++")
    // console.log(this.page)
    // console.log("+++++++++++")

    this.encomendasService.getSubEncomendas(this.page).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.subencomendas = obj.body as FullSubEncomendaDTO[];
        if(this.subencomendas.length ===0 && this.page>0){
          this.page -=1
          this.nextButtonDisabled = true
          this.getSubEncomendas()
        }
    } else {
        this.error = obj.body as Error;
    }
    })
  }

  getProximasEncomendas(page: number){
    this.encomendasService.getEncomendas(page).subscribe(obj=>{
      const statusCode = obj.status
  
      if (statusCode === 200) {
        let encomendas = obj.body as FullEncomendaDTO[];
        this.proximasEncomendas = encomendas.length;
        console.log(this.proximasEncomendas)
    } else {
        this.error = obj.body as Error;
    }
    })
  }

  getProximasSubEncomendas(page:number){
    // console.log("+++++++++++")
    // console.log(this.page)
    // console.log("+++++++++++")

    this.encomendasService.getSubEncomendas(page).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        let subencomendas = obj.body as FullSubEncomendaDTO[];
        this.proximasEncomendas = subencomendas.length;
        // if(this.subencomendas.length ===0 && this.page>0){
        //   this.page -=1
        //   this.nextButtonDisabled = true
        //   this.getSubEncomendas()
        // }
    } else {
        this.error = obj.body as Error;
    }
    })
  }
  
  showEncomenda(encomenda?:FullEncomendaDTO, subencomenda?:FullSubEncomendaDTO){
    this.encomendaDestaque = encomenda
    this.subencomendaDestaque = subencomenda
    this.toggleModal()

  }

  toggleModal(){
    this.showModal = !this.showModal;
  }

  toggleAnswer(){
    this.showAnswer = !this.showAnswer;
  }
}
