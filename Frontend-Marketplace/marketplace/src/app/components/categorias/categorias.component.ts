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

  showCategoria(categoria : FullCategoriaDTO){
    console.log(categoria.nomeCategoria)
  }

  resetSelection(){
    this.categoriaEscolhida = undefined;
    this.subcategorias = undefined
  }

  goToProdutos(categoriaP?: FullCategoriaDTO){
    let queryParams = {};
    if(categoriaP){
      queryParams = { categoria: categoriaP.idCategoria, subCategoria:-1 };
    } else{
      queryParams = { categoria: -1, subCategoria:-1 };

    }
    this.router.navigate(['/produtos'], { queryParams });

  }


}
