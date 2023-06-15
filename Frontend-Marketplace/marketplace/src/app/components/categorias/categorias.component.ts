import { Component, OnInit } from '@angular/core';
import { FullCategoriaDTO } from '../../model/categoria/fullCategoriaDTO';
import{ SimpleSubCategoriaDTO } from '../../model/categoria/simpleSubCategoriaDTO'
import{ CategoriaService } from '../../service/categoria.service'
import { Router } from '@angular/router';
import { ProdutosComponent } from '../produtos/produtos.component';


@Component({
  selector: 'app-categorias',
  templateUrl: './categorias.component.html',
  styleUrls: ['./categorias.component.css']
})
export class CategoriasComponent implements OnInit{

  categorias? :FullCategoriaDTO[];
  categoriaEscolhida? : FullCategoriaDTO
  subcategorias?: SimpleSubCategoriaDTO[];
  error? : Error;

  subCategoria!: SimpleSubCategoriaDTO;
  subcategoriaEscolhida? : SimpleSubCategoriaDTO;



  constructor(private categoriaService : CategoriaService,private router: Router,private produtosComponent: ProdutosComponent){}

  ngOnInit(): void {
    this.getCategorias();

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

  selectCategoria(categoria : FullCategoriaDTO){
    this.categoriaEscolhida = categoria;
    this.subcategorias = categoria.subCategoriaList;

  }

  selectSubCategoria(subCategoriaNome? : string, subCategoria1Nome?:string, subCategoria2Nome?:string){
    console.log(subCategoriaNome)
    console.log(subCategoria1Nome)
    if(subCategoriaNome && subCategoria1Nome === undefined && subCategoria2Nome === undefined){
      this.subcategoriaEscolhida = this.subcategorias?.filter(subC => subC.nomeSubCategoria === subCategoriaNome)[0];

    }
    else if(subCategoriaNome && subCategoria1Nome && subCategoria2Nome === undefined){
      let sub1 = this.subcategorias?.filter(subC => subC.nomeSubCategoria === subCategoriaNome)[0];
      let sub2 = sub1?.subCategoriasFilhos?.filter(subC => subC.nomeSubCategoria === subCategoria1Nome)[0];
      this.subcategoriaEscolhida = sub2
    }
    else if(subCategoriaNome && subCategoria1Nome && subCategoria2Nome){
      let sub1 = this.subcategorias?.filter(subC => subC.nomeSubCategoria === subCategoriaNome)[0];
      let sub2 = sub1?.subCategoriasFilhos?.filter(subC => subC.nomeSubCategoria === subCategoria1Nome)[0];
      let sub3 = sub2?.subCategoriasFilhos?.filter(subC => subC.nomeSubCategoria === subCategoria2Nome)[0];
      this.subcategoriaEscolhida = sub3
    }

    console.log(this.subcategoriaEscolhida)
  }



  showCategoria(categoria : FullCategoriaDTO){
    console.log(categoria.nomeCategoria)
  }

  showSubCategoria(subCategoria : SimpleSubCategoriaDTO){
    this.subcategoriaEscolhida = subCategoria;
    console.log(subCategoria.nomeSubCategoria);
  }

  resetSelection(){
    this.categoriaEscolhida = undefined;
    this.subcategorias = undefined
  }

  goToProdutos(categoriaNome?:string, subC1Nome?:string, subC2Nome?:string, subC3Nome?:string ){
    let cat = this.categorias?.filter(categoria => categoria.nomeCategoria === categoriaNome)[0];
    let titulo = cat?.nomeCategoria;
    let idC = cat?.idCategoria ? cat?.idCategoria : -1;
    let idS = -1;

    //se só tiver subC1
    if(subC1Nome && subC2Nome===undefined && subC3Nome===undefined){
      let sub1 = this.subcategorias?.filter(subC => subC.nomeSubCategoria === subC1Nome)[0];
      idS = sub1?.idSubCategoria!;
      titulo=sub1?.nomeSubCategoria
      

    } else if (subC1Nome && subC2Nome && subC3Nome===undefined){
      let sub1 = this.subcategorias?.filter(subC => subC.nomeSubCategoria === subC1Nome)[0];
      let sub2 = sub1?.subCategoriasFilhos!.filter(subC => subC.nomeSubCategoria === subC2Nome)[0];
      idS = sub2?.idSubCategoria!;
      if(sub1?.nomeSubCategoria?.includes(sub2?.nomeSubCategoria!)){
        titulo=sub2?.nomeSubCategoria
      } else{
        titulo= sub1?.nomeSubCategoria + ", "+sub2?.nomeSubCategoria

      }

    } else if (subC1Nome && subC2Nome && subC3Nome){
      let sub1 = this.subcategorias?.filter(subC => subC.nomeSubCategoria === subC1Nome)[0];
      let sub2 = sub1?.subCategoriasFilhos!.filter(subC => subC.nomeSubCategoria === subC2Nome)[0];
      let sub3 = sub2?.subCategoriasFilhos!.filter(subC => subC.nomeSubCategoria === subC3Nome)[0];
      idS = sub3?.idSubCategoria!;
      titulo= sub3?.nomeSubCategoria

    }

    let queryParams = { categoria: idC, subCategoria: idS, titulo: titulo};

   
    this.router.navigate(['/marketplace/produtos'], { queryParams });

  }

  mostrar(a:string){

  }

}
