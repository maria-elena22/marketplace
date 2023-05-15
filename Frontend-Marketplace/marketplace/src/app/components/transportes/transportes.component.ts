import { Component, OnInit } from '@angular/core';
import { ItemInfoDTO, ItemViagemDTO, SubItemViagemDTO, TransporteDTO, TransporteInputDTO, UniProdDTO, UniProdInputDTO, ViagemInputDTO } from 'src/app/model/models';
import{UniProdsService} from '../../service/uni-prods.service';
import { FormBuilder, FormGroup, FormControl, Validators, FormArray } from '@angular/forms';
import { AppComponent } from 'src/app/app.component';
import { UniProdsComponent } from '../uni-prods/uni-prods.component';
import { Router } from '@angular/router';
import { EncomendasService } from 'src/app/service/encomendas.service';
import { ViagemService } from 'src/app/service/viagem.service';

@Component({
  selector: 'app-transportes',
  templateUrl: './transportes.component.html',
  styleUrls: ['./transportes.component.css']
})
export class TransportesComponent implements OnInit{
  transportes : TransporteDTO[];
  transportesDisponiveis : TransporteDTO[];
  selectedTransporteId?: string;
  itensNaoEntregues?: ItemInfoDTO[];
  itensParaEntregar:SubItemViagemDTO[] = [];
  checkedItems: ItemViagemDTO[] = []

  page = 0
  previousButtonDisabled = true
  nextButtonDisabled = false
  error?:Error;
  showModal: boolean = false;
  showNovaViagem:boolean=false
  transporteForm: FormGroup;
  viagemForm:FormGroup;
  uniProds?:UniProdDTO[]
  estados = Object.keys(TransporteDTO.EstadoTransporteEnum).filter(key => isNaN(Number(key)));
  showAnswer = false
  answer:string
  success:boolean
  validado = false



  constructor(private uniProdService:UniProdsService,private formBuilder: FormBuilder, private appComponent:AppComponent,
    private router : Router, private encomendaService:EncomendasService, private viagemService:ViagemService){}

  ngOnInit(){
    this.getUniProds();
    this.getTransportes(this.page);
    
    console.log(this.uniProds)
    this.transporteForm = new FormGroup({
      matricula: new FormControl('', Validators.required),
      estadoTransporte: new FormControl('', Validators.required),
      uniProd: new FormControl('', Validators.required)

    });
//ViagemInputDTO
    this.viagemForm = new FormGroup({
      transporte: new FormControl('', Validators.required),
      subItems: new FormControl([], Validators.required)
    });
    
  }
  ;

  

  // Method to toggle the checkbox status
  toggleItemChecked(index: number): void {
    const idxChecked = this.isItemChecked(index);
    console.log(this.viagemForm.value)

    if(idxChecked !== null){
      this.checkedItems.splice(idxChecked,1)
      this.itensParaEntregar.splice(idxChecked,1)
      console.log(this.checkedItems)
    }else{
      this.checkedItems.push({idItem:index})
      this.itensParaEntregar.push({item:{idItem:index},quantidade: 0})
    }
    console.log(this.viagemForm.value)
      this.viagemForm.patchValue({subItems:this.itensParaEntregar})
      this.validado = false
  }

  // Method to check if the item at the given index is checked
  isItemChecked(index: number) {
    for(let i = 0; i<this.checkedItems.length;i++){
      if(this.checkedItems[i].idItem === index){
        return i;
      }
    }
    return null;
  }



  addSubItem(itemId:number, event: Event): void {
    console.log(this.viagemForm.value)
    const value = (event.target as HTMLInputElement).value;

    const quantidade: number = parseFloat(value);
    for(let itemE of this.itensParaEntregar){
      if(itemE.item?.idItem === itemId){
        const item = this.itensNaoEntregues?.filter(i => i.idItem===itemId)[0]
        if(quantidade<1 || quantidade>item?.quantidade!-item?.quantidadeDespachada! || quantidade>item?.quantidadeStock!){
          this.validado =false
        } else{
          itemE.quantidade = quantidade
          this.validado =true

        }
        itemE.quantidade = quantidade
      }
    }

    this.viagemForm.patchValue({subItems:this.itensParaEntregar})
  
    // Your function logic here, using the itemId and quantidade
    // For example:
    console.log(itemId, quantidade);
    
    console.log(this.viagemForm.value)
    console.log(this.itensParaEntregar)
  }

  onTransporteSelected(): void {
    // Perform actions when an option is selected
    // You can access the selected value using the selectedTransporteId property
    console.log('Option selected:', this.selectedTransporteId);


    this.encomendaService.getItensNaoEntregues(Number(this.selectedTransporteId),0,1000000).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.itensNaoEntregues = obj.body as ItemInfoDTO[];
        
          console.log(this.itensNaoEntregues[0])
        
    } 
    })

  }

  goToViagens(transporte:TransporteDTO){
    //this.router.navigate(['/viagens']);
    this.router.navigate(['/viagens', transporte.idTransporte], {
      state: { transporteId: JSON.stringify(transporte)}
    });

  }

  onSubmitViagem(){
    console.log(this.viagemForm.value)

    const viagemData: ViagemInputDTO = {
      transporte: {idTransporte:this.viagemForm.value.transporte},
      subItems: this.viagemForm.value.subItems
    }

    this.viagemService.insertViagem(viagemData).subscribe(obj=>{
      const statusCode = obj.status

      if (statusCode === 200) {
        this.toggleModal()
        this.handleAnswer("Viagem adicionada com sucesso!",true)   
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

    
  getUniProds(){
    this.uniProdService.getUniProds().subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.uniProds = obj.body as UniProdDTO[];
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })

  }

  nextPage(){
    this.page +=1
    this.getTransportes(this.page)
    console.log(this.page)
    // if(this.transportes.length===0){

    //   console.log(this.transportes)
      
    // } else{
      const state = { page: 'transportes' };
      const url = '/transportes';
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
      this.getTransportes(this.page)
      const state = { page: 'transportes' };
      const url = '/transportes';
      this.nextButtonDisabled = false
      window.history.pushState(state, url);

    }
    

    

  }

  getLocalidade():string{
    return `${this.appComponent.user?.continente}, ${this.appComponent.user?.pais}, 
          ${this.appComponent.user?.distrito}, ${this.appComponent.user?.municipio}, ${this.appComponent.user?.freguesia}`
  }
    


  getTransportes(page:number,unidadeProducaoId?:number,estadoTransporte?:TransporteDTO.EstadoTransporteEnum, size?:number){
    this.uniProdService.getTransportes(unidadeProducaoId,estadoTransporte,page,size).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        if(estadoTransporte){
          this.transportesDisponiveis = this.transportes = obj.body as TransporteDTO[];
        } else{
          this.transportes = obj.body as TransporteDTO[];
          if(this.transportes.length ===0 && this.page>0){
            this.page -=1
            this.nextButtonDisabled = true
            this.getTransportes(this.page)
          }
          console.log(this.transportes)
        }
        
    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }
    })
  }

  showModalNovaViagem(){
    this.getTransportes(0,undefined,TransporteDTO.EstadoTransporteEnum.Disponivel,1000000)
    this.toggleNovaViagem()
  }



  toggleModal(){
    this.showModal = !this.showModal;
  }

  toggleNovaViagem(){
    this.showNovaViagem = !this.showNovaViagem;

  }

  toggleAnswer(){
    this.showAnswer = !this.showAnswer;
  }
}
