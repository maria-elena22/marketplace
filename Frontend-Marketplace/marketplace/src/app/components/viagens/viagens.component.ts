import { Component, Input, OnInit } from '@angular/core';
import { SubItemDTO, TransporteDTO, TransporteInputDTO, UniProdDTO, UniProdInputDTO, ViagemDTO } from 'src/app/model/models';
import{UniProdsService} from '../../service/uni-prods.service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { AppComponent } from 'src/app/app.component';
import { UniProdsComponent } from '../uni-prods/uni-prods.component';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { TransportesComponent } from '../transportes/transportes.component';
import { ViagemService } from 'src/app/service/viagem.service';
import { Inject } from '@angular/core';
import { UtilizadorService } from 'src/app/service/utilizador.service';

@Component({
  selector: 'app-viagens',
  templateUrl: './viagens.component.html',
  styleUrls: ['./viagens.component.css']
})
export class ViagensComponent implements OnInit{

  transporte: TransporteDTO;
  viagens:ViagemDTO[];
  showViagens = false
  viagemDestaque?:ViagemDTO;
  subItemsNaoEntregues?:SubItemDTO[]

  page = 0
  previousButtonDisabled = true
  nextButtonDisabled = false
  error?:Error;
  showModal: boolean = false;
  
  transporteForm: FormGroup;
  uniProds?:UniProdDTO[]
  estados = Object.keys(TransporteDTO.EstadoTransporteEnum).filter(key => isNaN(Number(key)));
  showAnswer = false
  answer:string
  success:boolean



  constructor(private uniProdService:UniProdsService,private formBuilder: FormBuilder, private appComponent:AppComponent,
              private route: ActivatedRoute, private viagemService:ViagemService, private router:Router, private utilizadorService:UtilizadorService){}

  ngOnInit(){
    if(this.appComponent.token && this.appComponent.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser().subscribe()
    }

    this.route.params.subscribe(params => {
      this.getTransporte(JSON.parse(params['id']))!
      this.getViagens(JSON.parse(params['id']))
      
    });

   

    // this.getUniProds();
    // this.getTransportes();
    
    // console.log(this.uniProds)
    // this.transporteForm = new FormGroup({
    //   matricula: new FormControl('', Validators.required),
    //   estadoTransporte: new FormControl('', Validators.required),
    //   uniProd: new FormControl('', Validators.required)

    // });
    
  }

 
  goToTransportes(){
    this.router.navigate(['/marketplace/transportes'])
  }


  getTransporte(id:number){
    this.uniProdService.getTransportes(undefined,undefined,this.page).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        const transportes = obj.body as TransporteDTO[];

          for(let t of transportes){
            console.log(t) 
            console.log(t.idTransporte)        

            if(t.idTransporte == id){
              this.transporte = t;
              break;
              // const state = { page: 'viagens' };
              // const url = '/marketplace/viagens';
              // window.history.pushState(state, url);
              
            }
          }
        
    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }

    })
  }


  onSubmit() {
    
    const transporteData: TransporteInputDTO = {
      matricula: this.transporteForm.value.matricula,
      estadoTransporte: this.transporteForm.value.estadoTransporte
    }

    this.uniProdService.insertTransportes(transporteData, this.transporteForm.value.uniProd).subscribe(obj=>{
      const statusCode = obj.status

      if (statusCode === 200) {
        this.toggleModal()
        this.handleAnswer("Transporte adicionado com sucesso!",true)   
        window.location.reload()    
      }  else {
        console.log(obj)
        this.handleAnswer(obj.statusText,false)   
        
      }
    },
    (error) => {
      console.log("An error occurred:", error);
      // Handle the error here, for example, you can display an error message to the user
      this.handleAnswer("Ocorreu um erro ao adicionar o produto.",false)   

    }
  )

  } 

  handleAnswer(answer:string,success:boolean){
    this.answer = answer
    this.success = success
    this.showAnswer = true 
  }

    
  getViagens(idTransporte:number){
    this.viagemService.getViagens(idTransporte).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.viagens = obj.body as ViagemDTO[];
        this.getSubItemsNaoEntregues()
        console.log(this.viagens)
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })

  }
  getSubItemsNaoEntregues(){
    this.viagemService.getSubItemsNaoEntregues().subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.subItemsNaoEntregues = obj.body as SubItemDTO[];
        console.log(this.subItemsNaoEntregues)
        
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })
  }

  subItemEstaEntregue(idSubItem:number){
    for(let subItemNE of this.subItemsNaoEntregues!){
      if(subItemNE.idSubItem === idSubItem){
        return "Por Entregar";
      }
    }
    return "Entregue";
  }

  viagemEstado(estado:string){
    return this.viagemDestaque?.estadoViagem === estado.toUpperCase()
  }

  iniciarViagem(){
    let subItemsIds:Array<number> = [] 

    for(let subItem of this.viagemDestaque?.subItems!){
      subItemsIds.push(subItem.idSubItem!);
    }

    this.viagemService.iniciaViagem(subItemsIds).subscribe(obj=>{
        console.log(obj)
        window.location.reload()
        
    })
  }

  entregarSubItem(idSubItem:number){
    this.viagemService.terminaViagem(idSubItem).subscribe(obj=>{
      console.log(obj)
      window.location.reload()
      
  })
  }

  getData(data:string){
    const date = new Date(data);
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const year = date.getFullYear().toString();

  return `${hours}:${minutes} - ${day}-${month}-${year}`;
  }

  terminarViagem(){


    for(let subItem of this.viagemDestaque?.subItems!){
      if(this.subItemEstaEntregue(subItem.idSubItem!) === "Por Entregar"){
        this.entregarSubItem(subItem.idSubItem!)
      }
    }

    

  }

  nextPage(){
    this.page +=1
    this.getTransportes()
    console.log(this.page)
    // if(this.transportes.length===0){

    //   console.log(this.transportes)
      
    // } else{
      const state = { page: 'transportes' };
      const url = '/marketplace/transportes';
      this.previousButtonDisabled = false
      window.history.pushState(state, url);
      

    //}
    
  }
  previousPage(){
    this.page -=1
    if(this.page<0){  
      this.page += 1
      this.previousButtonDisabled = true

    } else{
      this.getTransportes()
      const state = { page: 'transportes' };
      const url = '/marketplace/transportes';
      this.nextButtonDisabled = false
      window.history.pushState(state, url);

    }
    

    

  }

  getLocalidade():string{
    return `${this.appComponent.user?.continente}, ${this.appComponent.user?.pais}, 
          ${this.appComponent.user?.distrito}, ${this.appComponent.user?.municipio}, ${this.appComponent.user?.freguesia}`
  }
    


  getTransportes(){
    this.uniProdService.getTransportes(undefined,undefined,this.page).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.viagens = obj.body as ViagemDTO[];
        if(this.viagens.length ===0 && this.page>0){
          this.page -=1
          this.nextButtonDisabled = true
          this.getTransportes()
        }
        console.log(this.viagens)
    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }
    })
  }

  showDetalhesViagem(viagem:ViagemDTO){
    this.viagemDestaque = viagem
    this.toggleModal();
  }

  toggleModal(){
    this.showModal = !this.showModal;
  }

  toggleAnswer(){
    this.showAnswer = !this.showAnswer;
  }

}
