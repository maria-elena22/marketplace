import { Component, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FullCategoriaDTO, RelatorioPorDistanciasDTO, RelatorioPorZonasDTO, ViagemInputDTO } from 'src/app/model/models';
import { RelatorioService } from 'src/app/service/relatorio.service';
import { CategoriasComponent } from '../categorias/categorias.component';
import { CategoriaService } from 'src/app/service/categoria.service';
import { GraficoComponent } from '../grafico/grafico.component';
import { AppComponent } from 'src/app/app.component';
import { UtilizadorService } from 'src/app/service/utilizador.service';

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
  categoriasIds:number[] = [];


  dados: Array<number>;
  labels: Array<string>;
  tiposZonas = ["Continentes", "Países", "Distritos", "Municípios", "Freguesias"]
  zonaVisivel = "Continentes";



  constructor(private route: ActivatedRoute, private router: Router, private relatorioService: RelatorioService, private categoriaService:CategoriaService,
    private appComponent: AppComponent, private utilizadorService:UtilizadorService){}

  @ViewChild(GraficoComponent, { static: false }) grafico: GraficoComponent;

  ngOnInit(): void {
    if(this.appComponent.token && this.appComponent.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser().subscribe()
    }

    this.getCategorias();
    
    this.route.queryParams.subscribe((queryParams) => {
      
      this.tipo = queryParams["tipo"]
      if(this.tipo === 'zonas'){
        this.getRelatorioZonas([]);
        
        

      } else{
        this.getRelatorioDistancias([]);

      }


    });
    this.filtrosForm = new FormGroup({
      startDate: new FormControl(null, Validators.required),
      endDate: new FormControl(null, Validators.required),
      categoriasIds: new FormControl([], Validators.required)

    });
  }


  abreTab(tabName: string): void {
    this.zonaVisivel = tabName;
    this.detalhesGraficoZonas()
  }
  
  // Set the first tab as active by default
  


  detalhesGraficoZonas(){
    switch (this.zonaVisivel) {
      case "Continentes":
        this.labels = this.getArrayZonas(this.relatorioZonas!.mapEncomendasContinente!);
        this.dados = Object.values(this.relatorioZonas!.mapEncomendasContinente!);

        break;

      case "Países":
        this.labels = this.getArrayZonas(this.relatorioZonas!.mapEncomendasPais!);
        this.dados = Object.values(this.relatorioZonas!.mapEncomendasPais!);

        break;
    
      case "Distritos":
        this.labels = this.getArrayZonas(this.relatorioZonas!.mapEncomendasDistrito!);
        this.dados = Object.values(this.relatorioZonas!.mapEncomendasDistrito!);

        break;
      
      case "Municípios":
        this.labels = this.getArrayZonas(this.relatorioZonas!.mapEncomendasMunicipio!);
        this.dados = Object.values(this.relatorioZonas!.mapEncomendasMunicipio!);

        break;
      
      case "Freguesias":
        this.labels = this.getArrayZonas(this.relatorioZonas!.mapEncomendasFreguesias!);
        this.dados = Object.values(this.relatorioZonas!.mapEncomendasFreguesias!);

        break;
        
      default:
        this.labels = this.getArrayZonas(this.relatorioZonas!.mapEncomendasContinente!);
        this.dados = Object.values(this.relatorioZonas!.mapEncomendasContinente!);

        break;
    }
    if(this.grafico){
      this.grafico.dadosHist=this.dados
      this.grafico.labelsHist=this.labels
      this.grafico.criarHistograma()
    }
 

    
  }

  detalhesGraficoDistancias(){
    const gamas = this.getArrayZonas(this.relatorioDistancias!.gamaDistanciasQuantidadeEncomendasMap!);
    
    this.labels = []
    for(const gama of gamas){
      switch (gama){
        case "<10":
          this.labels.push("10km")
          break;
        case "10-100":
          this.labels.push("100km")
          break;
        case "100-1000":
          this.labels.push("1000km")
          break;
        case ">1000":
          this.labels.push("+1000km")
          break;
      }
    }

    this.dados = Object.values(this.relatorioDistancias!.gamaDistanciasQuantidadeEncomendasMap!);

    this.grafico.dadosHist=this.dados
    this.grafico.labelsHist=this.labels
    this.grafico.criarHistograma()

  }

  // previousZona(){
  //   this.zonaVisivel = (this.zonaVisivel - 1 + this.tiposZonas.length) % this.tiposZonas.length;
  //   this.detalhesGraficoZonas()
  // }

  // nextZona(){
  //   this.zonaVisivel = (this.zonaVisivel + 1) % this.tiposZonas.length;
  //   this.detalhesGraficoZonas()
  // }

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

  onCategoriaAdd(target1:any, idCategoria: number): void {
    let target = target1 as HTMLInputElement
  
    if (target.checked) {
      this.categoriasIds.push(idCategoria);
    } else {
      const index = this.categoriasIds.indexOf(idCategoria);
      if (index !== -1) {
        this.categoriasIds.splice(index, 1);
      }
    }
    this.filtrosForm.get('categoriasIds')!.patchValue(this.categoriasIds);
    console.log(this.filtrosForm.value)

  }

  onSubmit(){
    console.log(this.filtrosForm.value)

    if(this.tipo === 'zonas'){
      this.getRelatorioZonas(this.filtrosForm.value.categoriasIds, this.filtrosForm.value.startDate, this.filtrosForm.value.endDate);
      
    } else{
      this.getRelatorioDistancias(this.filtrosForm.value.categoriasIds, this.filtrosForm.value.startDate, this.filtrosForm.value.endDate);
      
    }
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


  getRelatorioZonas(categoriasIds: Array<number>, dataMin?:string, dataMax?:string){

    this.relatorioService.getRelatorioZonas(categoriasIds, dataMin, dataMax).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.relatorioZonas = obj.body as RelatorioPorZonasDTO;
        this.relatorioDistancias = undefined
        console.log(this.relatorioZonas)
        // const state = { page: 'relatorios' };
        // const url = '/marketplace/relatorios';

        // window.history.pushState(state, url);
        this.detalhesGraficoZonas();
        
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })

  }

  getRelatorioDistancias(categoriasIds: Array<number>, dataMin?:string, dataMax?:string){

    this.relatorioService.getRelatorioDistancias(categoriasIds, dataMin, dataMax).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.relatorioDistancias = obj.body as RelatorioPorDistanciasDTO;
        this.relatorioZonas = undefined
        console.log(this.relatorioDistancias)
        this.sortRelatorio()
        this.detalhesGraficoDistancias();
        

    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })

  }

  sortRelatorio(){

    this.relatorioDistancias!.gamaDistanciasQuantidadeEncomendasMap = {"<10":this.relatorioDistancias?.gamaDistanciasQuantidadeEncomendasMap!["<10"]!,
    "10-100":this.relatorioDistancias?.gamaDistanciasQuantidadeEncomendasMap!["10-100"]!,
    "100-1000":this.relatorioDistancias?.gamaDistanciasQuantidadeEncomendasMap!["100-1000"]!,
    ">1000":this.relatorioDistancias?.gamaDistanciasQuantidadeEncomendasMap![">1000"]!
                                                                      }

  }
}
