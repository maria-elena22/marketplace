import { Component, OnInit, SimpleChanges } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RelatorioPorDistanciasDTO, RelatorioPorZonasDTO } from 'src/app/model/models';
import { RelatorioService } from 'src/app/service/relatorio.service';

@Component({
  selector: 'app-relatorios',
  templateUrl: './relatorios.component.html',
  styleUrls: ['./relatorios.component.css']
})
export class RelatoriosComponent implements OnInit{

  tipo:string;
  relatorioZonas?:RelatorioPorZonasDTO;
  relatorioDistancias?:RelatorioPorDistanciasDTO;
  error?:Error;
  showContinentes = false
  showPaises = false
  showDistritos=false
  showMunicipios=false
  showFreguesias=false
  

  constructor(private route: ActivatedRoute, private router: Router, private relatorioService: RelatorioService){}

  ngOnInit(): void {
    this.route.queryParams.subscribe((queryParams) => {
      
      this.tipo = queryParams["tipo"]
      if(this.tipo === 'zonas'){
        this.getRelatorioZonas();
        
        

      } else{
        this.getRelatorioDistancias();

      }


    });
  }

  getArrayZonas(mapEncomendas:{[key: string]: number}):string[]{
    let zonas:string[] = [];
    for (const [zona, quantidade] of Object.entries(mapEncomendas)) {
      // console.log(`Key: ${key}, Value: ${value}`);
      zonas.push(zona)
    } 
    return zonas;
  }


  getRelatorioZonas(){

    this.relatorioService.getRelatorioZonas().subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.relatorioZonas = obj.body as RelatorioPorZonasDTO;
        this.relatorioDistancias = undefined
        console.log(this.relatorioZonas)
        // const state = { page: 'relatorios' };
        // const url = '/relatorios';

        // window.history.pushState(state, url);
        
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })

  }

  getRelatorioDistancias(){

    this.relatorioService.getRelatorioDistancias().subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.relatorioDistancias = obj.body as RelatorioPorDistanciasDTO;
        this.relatorioZonas = undefined
        console.log(this.relatorioDistancias)

        

    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })

  }
}
