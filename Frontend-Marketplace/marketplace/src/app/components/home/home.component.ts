import { Component, OnInit } from '@angular/core';
import { AppComponent } from 'src/app/app.component';
import { UtilizadorService } from 'src/app/service/utilizador.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{

  constructor(private appComponent: AppComponent, private utilizadorService:UtilizadorService){}

  ngOnInit(): void {
    if(this.appComponent.token && this.appComponent.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser().subscribe()
    }

  }

  bemVindo(){
    
    return this.appComponent.user!==undefined
  }

}
