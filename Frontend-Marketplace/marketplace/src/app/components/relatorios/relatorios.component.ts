import { Component, OnInit, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FullCategoriaDTO, RelatorioPorDistanciasDTO, RelatorioPorZonasDTO, ViagemInputDTO } from 'src/app/model/models';
import { RelatorioService } from 'src/app/service/relatorio.service';
import { CategoriasComponent } from '../categorias/categorias.component';
import { CategoriaService } from 'src/app/service/categoria.service';

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
  filtrosForm:FormGroup;
  categorias? :FullCategoriaDTO[];



  constructor(private route: ActivatedRoute, private router: Router, private relatorioService: RelatorioService, private categoriaService:CategoriaService){}

  ngOnInit(): void {
    this.getCategorias();
    this.route.queryParams.subscribe((queryParams) => {
      
      this.tipo = queryParams["tipo"]
      if(this.tipo === 'zonas'){
        this.getRelatorioZonas();
        
        

      } else{
        this.getRelatorioDistancias();

      }


    });
    this.filtrosForm = new FormGroup({
      startDate: new FormControl('', Validators.required),
      endDate: new FormControl('', Validators.required),
      categoriasIds: new FormControl([], Validators.required)

    });
  }


  getCategorias(){
    this.categoriaService.getCategorias().subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.categorias = obj.body as FullCategoriaDTO[];
    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }
    })
  }


  onSubmit(){
    console.log(this.filtrosForm.value)
    return;
  //   const viagemData: ViagemInputDTO = {
  //     transporte: {idTransporte:this.filtrosForm.value.transporte},
  //     subItems: this.filtrosForm.value.subItems
  //   }

  //   this.viagemService.insertViagem(viagemData).subscribe(obj=>{
  //     const statusCode = obj.status

  //     if (statusCode === 200) {
  //       this.toggleModal()
  //       this.handleAnswer("Viagem adicionada com sucesso!",true)   
  //       window.location.reload()    
  //     }  else {
  //       console.log(obj)
  //       this.handleAnswer(obj.statusText,false)   
        
  //     }
  //   },
  //   (error) => {
  //     console.log("An error occurred:", error);
  //     // Handle the error here, for example, you can display an error message to the user
  //     this.handleAnswer("Ocorreu um erro ao adicionar a viagem.",false)   

  //   }
  // )

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
        // const url = '/marketplace/relatorios';

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
