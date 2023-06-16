import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { EncomendaDTO, FullEncomendaDTO, FullProdutoDTO, FullSubEncomendaDTO, SimpleUtilizadorDTO, UtilizadorDTO } from 'src/app/model/models';
import { ProdutosService } from 'src/app/service/produtos.service';
import { UtilizadorService } from 'src/app/service/utilizador.service';

@Component({
  selector: 'app-detalhes-utilizador-admin',
  templateUrl: './detalhes-utilizador-admin.component.html',
  styleUrls: ['./detalhes-utilizador-admin.component.css']
})
export class DetalhesUtilizadorAdminComponent implements OnInit{
  @Input() showDetalhes: boolean;
  @Input() user?: UtilizadorDTO;
  // error?:Error;
  @Input() role : string;
  showAnswer = false
  answer:string
  success:boolean
  confimaDesativar = false;
  confimaAtivar = false;


  constructor(private appComponent: AppComponent, private utilizadorService:UtilizadorService, private router: Router){}

  ngOnInit(): void {
    console.log(this.user)

  }

  openConfirmar(acao:string){
    if(acao === 'ativar'){
      this.confimaAtivar = true

    }
    if(acao === 'desativar'){
      this.confimaDesativar = true
    }
  }
  closeConfirmar(){
    this.confimaDesativar = false
    this.confimaAtivar = false
  }

  removerConta(){
    if(this.role === "fornecedor"){
      this.utilizadorService.removeFornecedor(this.user?.idUtilizador!).subscribe(obj=>{
        const statusCode = obj.status
          
        if (statusCode === 200) {
          this.answer = "Conta removida com sucesso"
          this.success = true;
          this.getUser();
  
      } 
    }
    )
  
    } 
    if (this.role === "consumidor"){
      this.utilizadorService.removeConsumidor(this.user?.idUtilizador!).subscribe(obj=>{
        const statusCode = obj.status
  
        if (statusCode === 200) {
          this.answer = "Conta removida com sucesso"
          this.success = true;
          this.getUser();
          //window.location.reload(); 
  
      } 
    }
    )
    }
  }

  ativarConta(){
    if(this.role === "fornecedor"){
      this.utilizadorService.ativarFornecedor(this.user?.idUtilizador!).subscribe(obj=>{
        const statusCode = obj.status
  
        if (statusCode === 200) {
          this.answer = "Conta reativada com sucesso"
          this.success = true;
  
          this.getUser();
  
      } 
    }
    )
  
    } 
    if (this.role === "consumidor"){
      this.utilizadorService.ativarConsumidor(this.user?.idUtilizador!).subscribe(obj=>{
        const statusCode = obj.status
  
        if (statusCode === 200) {
          this.answer = "Conta reativada com sucesso"
          this.success = true;

          this.getUser();
          //window.location.reload(); 
  
      } 
    }
    )
    }
  }

  getUser(){

    if(this.role === "fornecedor"){
      this.utilizadorService.getFornecedor(this.user?.idUtilizador!).subscribe(obj=>{
        if (obj.status  === 200) {
          this.user = obj.body as UtilizadorDTO;
          this.openAnswer();
  
      } 
    }
    )
  
    } 
    if (this.role === "consumidor"){
      this.utilizadorService.getConsumidor(this.user?.idUtilizador!).subscribe(obj=>{
        const statusCode = obj.status

        if (obj.status  === 200) {
          this.user = obj.body as UtilizadorDTO;
          this.openAnswer();
  
      } 
    }
    )
    }

  }



  openAnswer(){
    this.closeConfirmar()
    this.showAnswer = true;
  }


  closeAnswer(){
    
    this.showAnswer=false;
    window.location.reload()
    //this.router.navigate(['/marketplace/utilizadores']);
  }

  showEstado(estadoBool:boolean){
    return estadoBool ? "Ativo" : "Inativo"
  }




}

