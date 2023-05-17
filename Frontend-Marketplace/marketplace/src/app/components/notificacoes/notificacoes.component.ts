import { Component } from '@angular/core';

@Component({
  selector: 'app-notificacoes',
  templateUrl: './notificacoes.component.html',
  styleUrls: ['./notificacoes.component.css']
})
export class NotificacoesComponent {


  showNotifs=false

  toggleModal() {
    this.showNotifs = !this.showNotifs
  }
  
}
