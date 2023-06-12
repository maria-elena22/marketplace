import { Component, OnInit } from '@angular/core';
import { ActivatedRoute} from '@angular/router';
import { SignUpDTO, UtilizadorDTO } from 'src/app/model/models';
import { UtilizadorService } from 'src/app/service/utilizador.service';

@Component({
  selector: 'app-perfil-consumidor',
  templateUrl: './perfil-consumidor.component.html',
  styleUrls: ['./perfil-consumidor.component.css']
})
export class PerfilConsumidorComponent implements OnInit {
  error?:Error;
  utilizador: UtilizadorDTO;

  countries = Object.keys(SignUpDTO.PaisEnum).filter(key => isNaN(Number(key)));
  continents = Object.keys(SignUpDTO.ContinenteEnum).filter(key => isNaN(Number(key)));

  constructor(private route: ActivatedRoute, private utilizadorService: UtilizadorService){}
  
  ngOnInit(): void {
      this.refresh();
  }

  refresh(){
    this.route.queryParams.subscribe((queryParams) => {

      this.utilizador = queryParams["utilizador"];
      console.log(this.utilizador.nome)
      this.getConsumidorByID(queryParams["utilizador"])
    });
  }

  getConsumidorByID(id:number){
    this.utilizadorService.getConsumidorById(id).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
       console.log(obj.body as UtilizadorDTO)
      }
    })
  }
}
