import { Component, OnInit } from '@angular/core';
import { ItemInfoDTO, ItemViagemDTO, SubItemViagemDTO, TransporteDTO, TransporteInputDTO, UniProdDTO, UniProdInputDTO, ViagemInputDTO } from 'src/app/model/models';
import{UniProdsService} from '../../service/uni-prods.service';
import { FormBuilder, FormGroup, FormControl, Validators, FormArray } from '@angular/forms';
import { AppComponent } from 'src/app/app.component';
import { UniProdsComponent } from '../uni-prods/uni-prods.component';
import { Router } from '@angular/router';
import { EncomendasService } from 'src/app/service/encomendas.service';
import { ViagemService } from 'src/app/service/viagem.service';
import { UtilizadorService } from 'src/app/service/utilizador.service';

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
  checkedItems: ItemViagemDTO[] = [];
  trans: TransporteDTO

  page = 0
  previousButtonDisabled = true
  nextButtonDisabled = false
  error?:Error;
  showModal: boolean = false;
  showNovaViagem:boolean=false
  showEditar: boolean = false;
  transporteForm: FormGroup;
  viagemForm:FormGroup;
  uniProds?:UniProdDTO[]
  estados = Object.keys(TransporteDTO.EstadoTransporteEnum).filter(key => isNaN(Number(key)));
  showAnswer = false
  answer:string
  success:boolean
  validado = false
  startMatricula = false;
  matriculaValida = false;
  editarForm: FormGroup;
  transporte: TransporteDTO;
  estadoTransporte: string;

  proximosTransportes: number;

  constructor(private uniProdService:UniProdsService,private formBuilder: FormBuilder, private appComponent:AppComponent,
    private router : Router, private encomendaService:EncomendasService, private viagemService:ViagemService, private utilizadorService:UtilizadorService){}

  ngOnInit(){
    if(this.appComponent.token && this.appComponent.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser()?.subscribe()    
    }

    this.getUniProds();
    this.getProximosTransportes(this.page + 1)
    this.getTransportes(this.page);


    this.transporteForm = new FormGroup({
      matricula: new FormControl('', Validators.required),
      estadoTransporte: new FormControl('', Validators.required),
      uniProd: new FormControl('', Validators.required)

    });
    this.viagemForm = new FormGroup({
      transporte: new FormControl('', Validators.required),
      subItems: new FormControl([], Validators.required)
    });
    this.editarForm = this.formBuilder.group({
      estadoTransporte: ['', Validators.required]
    });

  }
  ;

  onMatriculaInput(event: Event){
    const inputElement = event.target as HTMLInputElement;
    const matriculaValue = inputElement.value;

    const numericRegex = /[a-zA-Z]{2}-[0-9]{2}-[a-zA-Z]{2}/;
    this.matriculaValida = numericRegex.test(matriculaValue);
  }


  // Method to toggle the checkbox status
  toggleItemChecked(index: number): void {
    const idxChecked = this.isItemChecked(index);

    if(idxChecked !== null){
      this.checkedItems.splice(idxChecked,1)
      this.itensParaEntregar.splice(idxChecked,1)
      this.validado= this.itensParaEntregar.length === 0? false:true
      for(let itemE of this.itensParaEntregar){
        if (itemE.quantidade!<1){
          this.validado =false
        }
      }

    }else{
      this.checkedItems.push({idItem:index})
      this.itensParaEntregar.push({item:{idItem:index},quantidade: 0})
      this.validado = false

    }
      this.viagemForm.patchValue({subItems:this.itensParaEntregar})
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
    this.validado = true
    const value = (event.target as HTMLInputElement).value;

    const quantidade: number = parseFloat(value);
    for(let itemE of this.itensParaEntregar){
      if(itemE.item?.idItem === itemId){
        const item = this.itensNaoEntregues?.filter(i => i.idItem===itemE.item?.idItem)[0]
        if(quantidade<1 || quantidade>item?.quantidade!-item?.quantidadeDespachada! || quantidade>item?.quantidadeStock!){
          itemE.quantidade = quantidade
          this.validado = false
        } else{
          itemE.quantidade = quantidade

        }

      } else if (itemE.quantidade!<1){
        this.validado =false
      }
    }

    this.viagemForm.patchValue({subItems:this.itensParaEntregar})

  }

  onTransporteSelected(): void {

    this.encomendaService.getItensNaoEntregues(Number(this.selectedTransporteId),0,1000000).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.itensNaoEntregues = obj.body as ItemInfoDTO[];
    }
    })

  }

  goToViagens(transporte:TransporteDTO){
    //this.router.navigate(['/marketplace/viagens']);
    this.router.navigate(['/marketplace/viagens', transporte.idTransporte], {
      state: { transporteId: JSON.stringify(transporte)}
    });

  }

  onSubmitViagem(){

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
        this.handleAnswer(obj.statusText,false)

      }
    },
    (error) => {
      console.log("An error occurred:", error);
      // Handle the error here, for example, you can display an error message to the user
      this.handleAnswer("Ocorreu um erro ao adicionar a viagem.",false)

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
        // window.location.reload()
      }  else {
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
    this.getProximosTransportes(this.page + 2)
    this.page +=1
    this.getTransportes(this.page)
    const state = { page: 'transportes' };
    const url = '/marketplace/transportes';
    this.previousButtonDisabled = false
    window.history.pushState(state, url);
  }

  previousPage(){
    this.page -=1
    this.getProximosTransportes(this.page - 1)
    if(this.page<=0){
      this.page += 1
      this.getTransportes(this.page - 1)
      this.previousButtonDisabled = true
      this.nextButtonDisabled = false;

    } else{
      this.getTransportes(this.page)
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

  getProximosTransportes(page:number,unidadeProducaoId?:number,estadoTransporte?:TransporteDTO.EstadoTransporteEnum, size?:number){
    this.uniProdService.getTransportes(unidadeProducaoId,estadoTransporte,page,size).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        if(estadoTransporte){
          let transportesDisponiveis = this.transportes = obj.body as TransporteDTO[];
          this.proximosTransportes = transportesDisponiveis.length;
        } else{
          let transportes = obj.body as TransporteDTO[];
          this.proximosTransportes = transportes.length;
          if(this.proximosTransportes == 0){
            this.nextButtonDisabled = true;
          }
        }
    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }
    })
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

  toggleEditar() {
    this.showEditar = !this.showEditar;
  }
  editarTransporte(transporte: TransporteDTO){
    this.transporte = transporte;
    this.showEditar = !this.showEditar;
  }

  toggleModal(){
    this.showModal = !this.showModal;
  }

  toggleNovaViagem(){
    this.showNovaViagem = !this.showNovaViagem;

  }

  toggleAnswer(){
    this.showAnswer = !this.showAnswer;
    window.location.reload()
  }

  onSubmitEditar() {
    if (this.editarForm.valid) {
      const novoEstado = this.editarForm.value.estadoTransporte;

      const transporteToUpdate = this.transportes.find(t => t.idTransporte === this.transporte.idTransporte);
      if (transporteToUpdate) {
        transporteToUpdate.estadoTransporte = novoEstado;

      }


      this.toggleEditar();

    }
  }


  // onSubmitEditar(){

  //   this.uniProdService.updateUniProd(this.editarForm.value,this.uniProdEscolhida.idUnidade!).subscribe(obj=>{
  //     const statusCode = obj.status
  //     if (statusCode === 200) {
  //       this.toggleEditar()
  //       window.location.reload()
  //   } else {
  //       this.error = obj.body as Error;
  //       //chamar pop up
  //   }
  //   })
  // }
}

