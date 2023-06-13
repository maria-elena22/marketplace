import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router} from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { Coordinate, SignUpDTO, UtilizadorDTO, UtilizadorInputDTO } from 'src/app/model/models';
import { UtilizadorService } from 'src/app/service/utilizador.service';

@Component({
  selector: 'app-perfil-consumidor',
  templateUrl: './perfil-consumidor.component.html',
  styleUrls: ['./perfil-consumidor.component.css']
})
export class PerfilConsumidorComponent implements OnInit {
  error?:Error;
  utilizador: UtilizadorDTO;

  role:string;
  showForm= false;
  noChanges = true;
  showAnswer = false
  answer:string
  success:boolean
  confimaRemover = false;
  updateForm: FormGroup;


  countries = Object.keys(SignUpDTO.PaisEnum).filter(key => isNaN(Number(key)));
  continents = Object.keys(SignUpDTO.ContinenteEnum).filter(key => isNaN(Number(key)));

  constructor(private route: ActivatedRoute, private utilizadorService: UtilizadorService, private appComponent:AppComponent,
     private router: Router){}
  
  ngOnInit(): void {
      this.refresh();

      this.updateForm = new FormGroup({
        idFiscal: new FormControl('', Validators.required),
        nome: new FormControl('', Validators.required),
        telemovel: new FormControl('', Validators.required),
        // latitude: new FormControl('', Validators.required),
        // longitude: new FormControl('', Validators.required),
        morada: new FormControl('', Validators.required),
        freguesia: new FormControl('', Validators.required),
        municipio: new FormControl('', Validators.required),
        distrito: new FormControl('', Validators.required),
        pais: new FormControl('', Validators.required),
        // continente: new FormControl('', Validators.required)
  
      });

      this.updateForm.patchValue({
        idFiscal: this.utilizador!.idFiscal,
        nome: this.utilizador!.nome,
        telemovel: this.utilizador!.telemovel,
        // latitude: this.utilizador!.coordenadas?.latitude,
        // longitude: this.utilizador!.coordenadas?.longitude,
        morada: this.utilizador!.morada,
        freguesia: this.utilizador!.freguesia,
        municipio: this.utilizador!.municipio,
        distrito: this.utilizador!.distrito,
        pais: this.utilizador!.pais,
        // continente: this.utilizador!.continente
  
      });
      this.showForm=true;
  
      this.updateForm.valueChanges.subscribe(() => {
        this.noChanges = false;
      });
  }

  onPageRefresh(event: BeforeUnloadEvent): void {
    this.refresh();
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
  

  refresh(){
    this.route.queryParams.subscribe((queryParams) => {

      console.log(JSON.parse(queryParams["user"]))
      this.role = queryParams["role"]
      this.utilizador = JSON.parse(queryParams["user"])


      // this.utilizador = queryParams["utilizador"];
      // console.log(this.utilizador.nome)
      // this.getConsumidorByID(queryParams["utilizador"])
    });
  }

  getConsumidorByID(id:number){
    this.utilizadorService.getConsumidor(id).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
       console.log(obj.body as UtilizadorDTO)
      }
    })
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
          console.log(this.utilizador)
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
    if(this.confimaRemover){
      this.router.navigate(['/marketplace']);
      return;
    }

    this.showAnswer=false;
    let queryParams = {};
    console.log(this.utilizador)
    queryParams = { role: this.role, user:JSON.stringify(this.utilizador!)};
    console.log(queryParams)
    this.router.navigate(['/marketplace/perfil-consumidor'], { queryParams });
  }

  

}
