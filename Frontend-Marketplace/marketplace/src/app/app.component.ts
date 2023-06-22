import { HttpClient } from '@angular/common/http';
import { Component, OnInit, SimpleChanges } from '@angular/core';
import { ProdutosService } from './service/produtos.service';
import {DecodedToken} from './model/utilizador/decodedToken'
import jwt_decode from 'jwt-decode';
import { UtilizadorService } from './service/utilizador.service';
import { FullEncomendaDTO, FullSubEncomendaDTO, NotificacaoDTO, UtilizadorDTO } from './model/models';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { CestoService } from './service/cesto.service';
import { EncomendasService } from './service/encomendas.service';


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
  notifs?:NotificacaoDTO[];
  showEncomendaModal = false
  subencomendaDestaque?:FullSubEncomendaDTO;
  encomendaDestaque?:FullEncomendaDTO;
  numNotifs = 0;



  // products: this.getCategorias();

  constructor(private http: HttpClient,private location: Location,private router: Router, public cestoService:CestoService,
    private produtosService: ProdutosService, private utilizadorService : UtilizadorService, private encomendaService: EncomendasService){

  }

  ngOnInit(): void {
    
    this.getDecodedToken()
    this.getDetalhesUser()
    this.toggleNotifs()
    if(this.user){
      this.utilizadorService.getNotificacoesNum().subscribe(obj=>{
        const statusCode = obj.status
        if (statusCode === 200) {
          this.numNotifs = obj.body as number;
      }

      })
    }

    this.tokenExpirado()
  }

  goToLogin(){
    this.router.navigate(['/marketplace/login']);
  }
  goToRegister(){
    this.router.navigate(['/marketplace/register']);
  }


  goToPerfil(){
    let queryParams = {};
    queryParams = { role: this.role, user:JSON.stringify(this.user!)};
    this.router.navigate(['/marketplace/perfil-consumidor'], { queryParams });
  }

  goToCesto(){
    this.router.navigate(['/marketplace/cesto']);
  }

  showNotifs=false

  abreNotif(){
    this.showNotifs = true;
  }

  toggleNotifs() {
    if(!this.showNotifs){
      //fazer get das notificacoes

      this.utilizadorService.getNotificacoes().subscribe(obj=>{
        const statusCode = obj.status
        if (statusCode === 200) {
          this.notifs = obj.body as NotificacaoDTO[];
          this.showNotifs = !this.showNotifs
          this.numNotifs=0
      }

      })

    } else{
      this.showNotifs = !this.showNotifs
    }

  }

  entregarNotif(idNotificacao:number){
    this.utilizadorService.entregarNotificacao(idNotificacao).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.notifs = this.notifs!.filter(notif => notif.idNotificacao !== idNotificacao);

    }

    })
  }

  showEncomenda(id:number){

    if(this.role! === "ROLE_FORNECEDOR"){
      this.encomendaService.getSubEncomendaById(id).subscribe(obj=>{
        const statusCode = obj.status
        if (statusCode === 200) {
          this.subencomendaDestaque = obj.body as FullSubEncomendaDTO;
          this.showEncomendaModal = true
        }
      })
    } else{
      this.encomendaService.getEncomendaById(id).subscribe(obj=>{
        const statusCode = obj.status
        if (statusCode === 200) {
          this.encomendaDestaque = obj.body as FullEncomendaDTO;
          this.showEncomendaModal = true
        }

      })
    }
  }

  toggleEncomenda(){
    this.showEncomendaModal = false
  }


  tokenExpirado(){
    if(localStorage.getItem("jwt_token") && this.user === undefined){
      this.answer = "A sua sessão expirou!"
      this.success = false
    }
  }

  goToRelatorio(tipo:string){
    let queryParams = {tipo:tipo};
    this.router.navigate(['/marketplace/relatorios'], { queryParams });

  }

  getCategorias(){
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
    if(this.token && this.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser()?.subscribe(obj=>{
        if (obj && obj.status === 200) {
          console.log(obj.body)
          this.user = obj.body as UtilizadorDTO ?? undefined;

        }

      })
    }


  }



  logout() {
    localStorage.removeItem(this.tokenKey);
    this.location.go('/marketplace');
    window.location.reload();
    // this.router.navigateByUrl('/marketplace');
    //this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {  this.router.navigate(['/marketplace' ]);})
    // const currentUrl = this.router.url; // Get the current route's URL
    // this.router.navigate([currentUrl])
    //   .then(() => {
    //     this.location.replaceState(currentUrl); // Update the browser's URL without triggering a navigation
    //     window.location.reload(); // Reload the page
    //   });

  }

  toggleAnswer(){
    if(this.showAnswer)(
      this.logout()
    )
    this.showAnswer = !this.showAnswer;
  }

}





