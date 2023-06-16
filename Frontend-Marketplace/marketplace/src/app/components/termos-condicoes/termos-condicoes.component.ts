import { Component, OnInit } from '@angular/core';
import { AppComponent } from 'src/app/app.component';
import { UtilizadorService } from 'src/app/service/utilizador.service';

@Component({
  selector: 'app-termos-condicoes',
  templateUrl: './termos-condicoes.component.html',
  styleUrls: ['./termos-condicoes.component.css']
})
export class TermosCondicoesComponent implements OnInit{

  constructor(private appComponent: AppComponent, private utilizadorService:UtilizadorService){}

  ngOnInit(): void {
    if(this.appComponent.token && this.appComponent.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser().subscribe()
    }

  }

}
