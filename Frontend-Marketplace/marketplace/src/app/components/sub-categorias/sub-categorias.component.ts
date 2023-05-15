import { Component, Input } from '@angular/core';
import { SimpleSubCategoriaDTO } from '../../model/categoria/simpleSubCategoriaDTO';
import { Router } from '@angular/router';
import { ProdutosComponent } from '../produtos/produtos.component';


@Component({
  selector: 'app-sub-categorias',
  templateUrl: './sub-categorias.component.html',
  styleUrls: ['./sub-categorias.component.css']
})
export class SubCategoriasComponent {
  @Input() subCategoria!: SimpleSubCategoriaDTO;
  subcategoriaEscolhida? : SimpleSubCategoriaDTO;

  constructor(private router: Router,private produtosComponent: ProdutosComponent ){}


  selectSubCategoria(subCategoria : SimpleSubCategoriaDTO){
    this.subcategoriaEscolhida === subCategoria ?
    (this.subcategoriaEscolhida = undefined) :
    (this.subcategoriaEscolhida = subCategoria);
  }

  showSubCategoria(subCategoria : SimpleSubCategoriaDTO){
    this.subcategoriaEscolhida = subCategoria;
    console.log(subCategoria.nomeSubCategoria);
  }

  goToProdutos(subCategoriaP? : SimpleSubCategoriaDTO){

    let queryParams = {};
    if(subCategoriaP){
      queryParams = { subCategoria: subCategoriaP.idSubCategoria, categoria:-1 };
    } else{
      queryParams = { categoria: -1, subCategoria:-1 };

    }
    this.router.navigate(['/produtos'], { queryParams });


    //this.router.navigate(['/produtos'], { queryParams });

  }


}
