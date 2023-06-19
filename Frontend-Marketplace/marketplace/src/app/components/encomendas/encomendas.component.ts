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

  constructor(private encomendasService:EncomendasService, private appComponent:AppComponent, private utilizadorService:UtilizadorService){  }

  ngOnInit(): void {
    if(this.appComponent.token && this.appComponent.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser().subscribe()
    }
    if(this.appComponent.role === "ROLE_CONSUMIDOR"){
      this.getProximasEncomendas(this.page + 1);
      this.getEncomendas();
    } 
    if(this.appComponent.role === "ROLE_FORNECEDOR"){
      this.getProximasSubEncomendas(this.page + 1);
      this.getSubEncomendas();
    }
  }

  nextPage(){
    this.page +=1
    if(this.appComponent.role === "ROLE_CONSUMIDOR"){
      this.getProximasEncomendas(this.page + 2)
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
    if(this.appComponent.role === "ROLE_CONSUMIDOR"){
      this.getProximasEncomendas(this.page - 2)
      console.log(this.page-2)
      if(this.proximasEncomendas == 0 && this.page - 2 <= 0){
        this.previousButtonDisabled = true;
      }
      this.getEncomendas();
    } 
    if(this.appComponent.role === "ROLE_FORNECEDOR"){
      this.getProximasSubEncomendas(this.page - 2)
      if(this.proximasEncomendas == 0 && this.page - 2 <= 0){
        this.nextButtonDisabled = true;
      }
      this.getSubEncomendas();
    }
    const state = { page: 'encomendas' };
    const url = '/marketplace/encomendas';
    this.nextButtonDisabled = false
    window.history.pushState(state, url);
  }

  getEncomendas(){
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

  obterJSON(){
      this.encomendasService.getSubEncomendas(this.page).subscribe(obj => {
        const statusCode = obj.status;
        if (statusCode === 200) {
          this.generateFile(JSON.stringify(obj.body));
        
        }
      });

    
  }


generateFile(jsonContent: any) {
  const blob = new Blob([jsonContent], { type: 'application/json' });
  const url = URL.createObjectURL(blob);

  const link = document.createElement('a');
  link.href = url;
  link.download = `encomendas${this.appComponent.user?.nome}.json`;
  
  document.body.appendChild(link);
  link.click();
  
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}

  getProximasEncomendas(page: number){
    this.encomendasService.getEncomendas(page).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        let encomendas = obj.body as FullEncomendaDTO[];
        this.proximasEncomendas = encomendas.length;
        if(this.proximasEncomendas == 0){
          this.nextButtonDisabled = true;
        }
    } else {
      this.proximasEncomendas = 0;
      this.error = obj.body as Error;
    }
    })
  }

  getProximasSubEncomendas(page:number){
    this.encomendasService.getSubEncomendas(page).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        let subencomendas = obj.body as FullSubEncomendaDTO[];
        this.proximasEncomendas = subencomendas.length;
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
