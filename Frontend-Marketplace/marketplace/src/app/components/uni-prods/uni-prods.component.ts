import { Component, OnInit } from '@angular/core';
import { FullProdutoDTO, ProdutoFornecedorDTO, TransporteDTO, UniProdDTO, UniProdInputDTO } from 'src/app/model/models';
import{UniProdsService} from '../../service/uni-prods.service';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { ProdutosService } from 'src/app/service/produtos.service';

@Component({
  selector: 'app-uni-prods',
  templateUrl: './uni-prods.component.html',
  styleUrls: ['./uni-prods.component.css']
})
export class UniProdsComponent implements OnInit {

  uniProds : UniProdDTO[];
  error?:Error;
  showModal: boolean = false;
  showProdutos: boolean = false;
  showEditar: boolean = false;
  showRemover: boolean = false;
  showConfirmar: boolean = false;
  uniProdForm: FormGroup;
  editarForm: FormGroup;
  estados = Object.keys(TransporteDTO.EstadoTransporteEnum).filter(key => isNaN(Number(key)));
  produtosUP :ProdutoFornecedorDTO[];
  uniProdEscolhida : UniProdDTO;
  showAlteraStock = false
  produtoStock :ProdutoFornecedorDTO;
  stockForm: FormGroup;


  constructor(private uniProdService:UniProdsService,private formBuilder: FormBuilder, private produtoService:ProdutosService){
    this.getUniProds()
  }

  ngOnInit(){
    //this.getUniProds();

    this.uniProdForm = new FormGroup({
      nomeUniProd: new FormControl('', Validators.required)

    });

    this.editarForm = new FormGroup({
      nomeUniProd: new FormControl('', Validators.required)

    });

    this.stockForm = new FormGroup({
      stock: new FormControl('', Validators.required)

    });
    
    
  }

  
  alterarStock(produto:ProdutoFornecedorDTO){
    console.log(produto)
    this.showAlteraStock = true
    this.produtoStock = produto
    this.toggleProdutos()

  }

  onSubmitStock(){
    this.uniProdService.updateUniProdStock(this.uniProdEscolhida.idUnidade!,
            this.stockForm.value.stock,this.produtoStock.idProduto!).subscribe(obj=>{
      const statusCode = obj.status

      if (statusCode === 200) {
        
        
        // const state = { page: 'uniProds' };
        // const url = '/uniProds';

        // window.history.pushState(state, url);
        this.toggleAlteraStock()
        window.location.reload()
        //trocar na tabela

      } 
    }
  )
  }

  onSubmit() {
    
    const uniProdData: UniProdInputDTO = {
      nomeUniProd: this.uniProdForm.value.nomeUniProd
    }
  
    this.uniProdService.insertUniProd(uniProdData).subscribe(obj=>{
      const statusCode = obj.status

      if (statusCode === 200) {
        this.toggleModal()
        window.location.reload(); 


      } 
    }
  )

  } 
  toggleProdutos(){
    this.showProdutos = !this.showProdutos
  }
  toggleEditar(){
    this.showEditar = !this.showEditar
  }
  toggleRemover(){
    this.showRemover = !this.showRemover
  }
  toggleConfirmar(){
    this.showConfirmar = !this.showConfirmar
  }

  toggleAlteraStock(){
    this.showAlteraStock = !this.showAlteraStock
  }

    
  editarUniProd(uniProd:UniProdDTO){
    this.uniProdEscolhida = uniProd
    this.toggleEditar()

  }

  onSubmitEditar(){
    
    this.uniProdService.updateUniProd(this.editarForm.value,this.uniProdEscolhida.idUnidade!).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.toggleEditar()
        console.log(obj.body)
        window.location.reload()
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })
  }

  removerUniProd(uniProd : UniProdDTO){
    this.uniProdService.removeUniProd(uniProd.idUnidade!).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.toggleRemover()
        window.location.reload()
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })
  }

  confirmarRemoverUniProd(uniProd:UniProdDTO){
    this.uniProdEscolhida =uniProd
    this.toggleConfirmar()
  }

  verProdutosUniProd(uniProd : UniProdDTO){

    this.produtoService.getMeusProdutos(-1,-1,-1,uniProd.idUnidade).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.produtosUP = obj.body as ProdutoFornecedorDTO[];
        console.log(this.produtosUP)
        this.uniProdEscolhida = uniProd;
        this.toggleProdutos()
        console.log(this.produtosUP)
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })
    
  }

  stockProduto(produto:ProdutoFornecedorDTO) : number{
    
    const uniProdsP = produto.uniProds
    if(uniProdsP){
      for(let uniProd of uniProdsP){
        if(uniProd.idUnidade === this.uniProdEscolhida.idUnidade){
          return uniProd.stock!
        }
      }
    }
    return 0
    
  }
    


  getUniProds(){
    this.uniProdService.getUniProds().subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.uniProds = obj.body as UniProdDTO[];
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })

  }

  toggleModal(){
    this.showModal = !this.showModal;
    //Criar Uni Prod
  }
}
