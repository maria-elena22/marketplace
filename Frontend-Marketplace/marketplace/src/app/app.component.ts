import { HttpClient } from '@angular/common/http';
import { Component, OnInit, SimpleChanges } from '@angular/core';
import { ProdutosService } from './service/produtos.service';
import {DecodedToken} from './model/utilizador/decodedToken'
import jwt_decode from 'jwt-decode';
import { UtilizadorService } from './service/utilizador.service';
import { UtilizadorDTO } from './model/models';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { CestoService } from './service/cesto.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
  
})


export class AppComponent implements OnInit{
  title = 'marketplace';
  token:DecodedToken |null;
  role?:string;
  private tokenKey = 'jwt_token';
  user?: UtilizadorDTO;
  showAnswer = false
  answer:string
  success:boolean
  error? :Error;

  
  // products: this.getCategorias();

  constructor(private http: HttpClient,private location: Location,private router: Router, public cestoService:CestoService, 
    private produtosService: ProdutosService, private utilizadorService : UtilizadorService){

  }
 
  ngOnInit(): void {
    this.getDecodedToken()
    this.getDetalhesUser()
    console.log(this.user)
    this.tokenExpirado()
  }
  
  

  tokenExpirado(){
    if(localStorage.getItem("jwt_token") && this.user === undefined){
      this.answer = "A sua sessão expirou!"
      this.success = false
    }
  }

  goToRelatorio(tipo:string){
    let queryParams = {tipo:tipo};
    this.router.navigate(['/relatorios'], { queryParams });
    
  }

  getCategorias(){
    console.log("success");
    return this.produtosService.getCategorias();
  }

  getDecodedToken() {
    // Get the JWT token from the local storage
    const tokenLS = localStorage.getItem(this.tokenKey);

    if (tokenLS) {
      // Decode the JWT token and return the claims
      
      this.token = jwt_decode(tokenLS) as DecodedToken;
      this.role = this.token!.role;

    } else{
      this.token = null;

    }

  }

  getDetalhesUser(){
    if(this.token){
      this.utilizadorService.getDetalhesUser().subscribe(obj=>{
        const statusCode = obj.status
        if (statusCode === 200) {
          console.log("------------------------------")
          console.log(obj.body)
          this.user = obj.body as UtilizadorDTO;
          
        
      } else {
          this.error = obj.body as Error;
          //chamar pop up
  
      }
  
      })
    }
    

  }


  

  logout() {
    // Remove the JWT token from the local storage
    localStorage.removeItem(this.tokenKey);
    this.location.go('');
    window.location.reload();

  }

  toggleAnswer(){
    if(this.showAnswer)(
      this.logout()
    )
    this.showAnswer = !this.showAnswer;
  }

}





