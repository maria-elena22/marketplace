import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute} from '@angular/router';
import { FullProdutoDTO, SimpleItemDTO } from 'src/app/model/models';
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

  constructor(private route: ActivatedRoute, private produtosService: ProdutosService, private cestoService: CestoService){}
  
  ngOnInit(): void {
    this.getProduto();
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
}
