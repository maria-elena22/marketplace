import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute} from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { FullProdutoDTO, ProdutoFornecedorDTO, SimpleItemDTO, UniProdDTO } from 'src/app/model/models';
import { UtilizadorCoordsDTO } from 'src/app/model/utilizador/utilizadorCoordsDTO';
import { CestoService } from 'src/app/service/cesto.service';
import { ProdutosService } from 'src/app/service/produtos.service';

@Component({
  selector: 'app-produto-detalhes',
  templateUrl: './produto-detalhes.component.html',
  styleUrls: ['./produto-detalhes.component.css']
})
export class ProdutoDetalhesComponent implements OnInit{
  error?:Error;
  idProduto?: number;
  produto: FullProdutoDTO;
  listaProdutos: FullProdutoDTO[];
  addCarrinhoForm: FormGroup;
  success = false;
  answer = ""
  showAnswer = false;
  showModal = false;
  role?:string;
  
  
  produtoAfornecer?:FullProdutoDTO
  produtoAremover?:FullProdutoDTO
  showAddProduto: boolean = false;
  showRemoverProduto: boolean = false;
  uniProdsA:UniProdDTO[]=[]

  constructor(private route: ActivatedRoute, private produtosService: ProdutosService, private cestoService: CestoService, private appComponent: AppComponent){}
  
  ngOnInit(): void {
    this.getProduto();
    this.role = this.appComponent.role
    this.addCarrinhoForm = new FormGroup({
      quantidade: new FormControl(0, [Validators.required,Validators.pattern(/^[1-9][0-9]*$/)]),
      fornecedor:new FormControl("", Validators.required)
    });
  }

  getProduto(){
    this.produtosService.getProdutos().subscribe(obj=>{
      const statusCode = obj.status;
      if (statusCode === 200) {
        this.listaProdutos = obj.body as FullProdutoDTO [];
        this.route.queryParams.subscribe((queryParams) => {
          for (let produto of this.listaProdutos){
            if(produto.idProduto == queryParams["produto"]){
              this.produto = produto;
              this.idProduto = produto.idProduto;
            }
          }
        })
        
      }else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    });
  } 

  getDistancia(f:UtilizadorCoordsDTO){
    const coordenadas = f.coordenadas;
    const lat1 = this.appComponent.user?.coordenadas?.latitude!
    const lon1 = this.appComponent.user?.coordenadas?.longitude!
    const lat2 = coordenadas?.latitude!
    const lon2 = coordenadas?.longitude!
    const earthRadius = 6371; // Radius of the Earth in kilometers

    // Convert latitude and longitude to radians
    const lat1Rad = (lat1 * Math.PI) / 180;
    const lon1Rad = (lon1 * Math.PI) / 180;
    const lat2Rad = (lat2 * Math.PI) / 180;
    const lon2Rad = (lon2 * Math.PI) / 180;

    // Calculate the differences between coordinates
    const dLat = lat2Rad - lat1Rad;
    const dLon = lon2Rad - lon1Rad;

    // Apply the Haversine formula
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    const distance = earthRadius * c;

    return distance;
  }
  
  onSubmitProduto(){
    const itemData: SimpleItemDTO = {
      produtoId : this.produto.idProduto,
      fornecedorId: this.addCarrinhoForm.value.fornecedor,
      quantidade: this.addCarrinhoForm.value.quantidade
    }

    console.log(itemData)
    this.cestoService.addToCart(itemData)
    this.success = true
    this.answer = "Produto adicionado ao cesto!"
    this.toggleAnswer();
    this.addCarrinhoForm.reset();
  }

  toggleModal(){
    this.showModal = !this.showModal;
    //Criar Uni Prod
  }
  toggleAnswer(){
    if(!this.showAnswer){
      if(this.success){
        this.toggleModal()
        this.showAnswer = true
      }else{
        this.showAnswer = true

      }
      
    }else{
      this.showAnswer = false
    }
  }

  forneco(produto:FullProdutoDTO):boolean{
    const myId = this.appComponent.user?.idUtilizador;
    for (let pf of produto.precoFornecedores!){
      if(pf.fornecedor?.idUtilizador === myId){
        return true;
      }
    }
    return false;
  }

  toggleAddProduto(produto?:FullProdutoDTO){
    this.produtoAfornecer=produto;
    this.showAddProduto = !this.showAddProduto
    if(this.showAddProduto === false){
      this.uniProdsA = []
    }
  }

  toggleRemoverProduto(produto?:FullProdutoDTO){
    
    this.produtosService.getMeusProdutos(-1,-1).subscribe(obj=>{
      const statusCode = obj.status
  
      if (statusCode === 200) {
        let prods = obj.body as ProdutoFornecedorDTO[];

        for(let p of prods){
          if(p.idProduto === produto?.idProduto){
            this.produtoAremover=produto;
            this.produtoAremover!.uniProds = p.uniProds;
            console.log(this.produtoAremover?.uniProds)
            const state = { page: 'produtos' };
            const url = '/marketplace/produtos';

            window.history.pushState(state, url);
            console.log(this.produtoAremover?.uniProds)
            
            this.showRemoverProduto = !this.showRemoverProduto
          }
        }

      }
    }) 
  }

}
