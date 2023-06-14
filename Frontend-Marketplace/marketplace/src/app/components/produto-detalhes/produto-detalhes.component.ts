import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute} from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { FullCategoriaDTO, FullProdutoDTO, ProdutoFornecedorDTO, PropriedadeDTO, SimpleItemDTO, SimpleSubCategoriaDTO, SubCategoriaDTO, UniProdDTO } from 'src/app/model/models';
import { UtilizadorCoordsDTO } from 'src/app/model/utilizador/utilizadorCoordsDTO';
import { CategoriaService } from 'src/app/service/categoria.service';
import { CestoService } from 'src/app/service/cesto.service';
import { ProdutosService } from 'src/app/service/produtos.service';
import { UniProdsService } from 'src/app/service/uni-prods.service';

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
  selectAll: boolean = false;
  showAddProduto: boolean = false;
  showRemoverProduto: boolean = false;
  uniProdsA:UniProdDTO[]=[]
  uniProdsIds:number[] = [];
  uniProdsIdsA:number[]=[]
  uniProdsP:UniProdDTO[]=[]
  addUnis = false

  categorias:FullCategoriaDTO[] = []
  subcategorias:SubCategoriaDTO[] = []
  props:string[][] =[];
  
  addProdutoForm:FormGroup;
  removerProdutoForm:FormGroup;

  constructor(private route: ActivatedRoute, private produtosService: ProdutosService, private categoriaService: CategoriaService, private cestoService: CestoService, private appComponent: AppComponent, private uniProdService: UniProdsService){}
  
  ngOnInit(): void {
    this.getProduto();
    this.minhasUniProds()
    this.role = this.appComponent.role
    this.addCarrinhoForm = new FormGroup({
      quantidade: new FormControl(0, [Validators.required,Validators.pattern(/^[1-9][0-9]*$/)]),
      fornecedor:new FormControl("", Validators.required)
    });
    this.removerProdutoForm = new FormGroup({
      uniProdsIds: new FormControl([], Validators.required)
    })
    this.addProdutoForm = new FormGroup({
      uniProdsIds: new FormControl([], Validators.required),
      preco:new FormControl("", Validators.required),
      stock:new FormControl("", Validators.required)
    })
  }

  getProduto(){
    this.route.queryParams.subscribe((queryParams) => {
      this.produtosService.getProdutos(queryParams["idCategoria"],queryParams["idSubCategoria"],undefined,undefined,undefined,undefined,queryParams["page"]).subscribe(obj=>{
        const statusCode = obj.status;
        if (statusCode === 200) {
          this.listaProdutos = obj.body as FullProdutoDTO [];
          
          for (let produto of this.listaProdutos){
            if(produto.idProduto == queryParams["produto"]){
              this.produto = produto;
              console.log(this.produto)
              console.log(this.produto.subCategorias)
              this.idProduto = produto.idProduto;
              this.getCategorias();
              this.resolvePropriedades();
            }
          }
          
        }else {
          this.error = obj.body as Error;
          //chamar pop up
      }
      });
    })
  } 

  resolvePropriedades(){
    for(let prop of Object.keys(this.produto.propriedades!)){
      console.log(prop)
      const value = prop.split("nomePropriedade=")[1];
      const startSubstring = "nomePropriedade="; // Define the starting substring
      const startIndex = prop.indexOf(startSubstring); // Find the index where the substring starts
      const valueStartIndex = startIndex + startSubstring.length; // Calculate the index where the value starts
      const valueEndIndex = prop.indexOf(")", valueStartIndex); // Find the index where the value ends
      const extractedValue = prop.substring(valueStartIndex, valueEndIndex); // Extract the value

      // console.log(extractedValue); 
      // console.log(this.produto.propriedades![prop])

      this.props.push([extractedValue,this.produto.propriedades![prop]])


    }
    console.log(this.props)

  }
 

  getCategorias(){
    this.categoriaService.getCategorias().subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {

        const todasCategorias = obj.body as FullCategoriaDTO[];
        for (const categoria of todasCategorias){
          if (this.produto.subCategorias?.filter(subcategoria => subcategoria.categoria?.idCategoria === categoria.idCategoria).length !== 0){
            this.categorias.push(categoria)
            
          }
        }
    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }
    
    for(let categoria of this.categorias){
      
      for(let subcategoria of categoria.subCategoriaList!){
        let sbs = this.getSubCategorias([], [subcategoria]);
        let filter = 0;
        if(sbs.length>0){
          filter = this.produto.subCategorias?.filter(sb => sb.idSubCategoria === sbs[sbs.length-1].idSubCategoria).length!;
        }

        if(filter ===0){
          sbs = []
        }
        sbs.forEach(sb=>this.subcategorias.push(sb));
      }
      
    }
    console.log(this.categorias)
    console.log(this.produto.subCategorias)
    console.log(this.subcategorias)
    console.log(this.produto.propriedades)
    

    })

  }

  mostrar(sbNome:string){
    
    for(let sb of this.subcategorias){

      if(sb.nomeSubCategoria === sbNome){
        return true;
      }
    }

    return false;
  }

  getSubCategorias(subCategoriasToAdd:SubCategoriaDTO[], subCategorias:SimpleSubCategoriaDTO[]):SubCategoriaDTO[]{
    for(let subcategoria of subCategorias){
  
      let filter = this.produto.subCategorias?.filter(sb => sb.idSubCategoria === subcategoria.idSubCategoria)
      if(filter!.length>0){
        subCategoriasToAdd.push(subcategoria);
       
      } else if(filter!.length===0 && subcategoria.subCategoriasFilhos!.length===0){
        console.log("-")
      }

      else{
        subCategoriasToAdd.push(subcategoria);
        return this.getSubCategorias(subCategoriasToAdd,subcategoria.subCategoriasFilhos!);
      }
    }
    return subCategoriasToAdd;

  }

  minhasUniProds(){
    this.uniProdService.getUniProds().subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.uniProdsP = obj.body as UniProdDTO[];
    } else {
        this.error = obj.body as Error;
        //chamar pop up
    }
    })
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

  onSubmitFornecer(){
    const produtoId = this.produtoAfornecer!.idProduto!
    const uniProdsIds = this.addProdutoForm.value.uniProdsIds
    const preco = this.addProdutoForm.value.preco
    const stock = this.addProdutoForm.value.stock
    this.addUniProds(produtoId,uniProdsIds,preco,stock);
    this.uniProdsA = []
  }

  onSubmitRemover(){
    const produtoId = this.produtoAremover!.idProduto!
    const uniProdsIds = this.removerProdutoForm.value
     
    console.log(this.removerProdutoForm.value.uniProdsIds)
    this.removeProduto(produtoId,this.removerProdutoForm.value.uniProdsIds)
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

  closeRemoveForm(){
    this.showRemoverProduto = !this.showRemoverProduto
    this.produtoAremover = undefined
    this.selectAll = false
    this.removerProdutoForm.patchValue({uniProdsIds:[]})
  }

  removeProduto(produtoId:number,uniProdsIds:Array<number>){

    this.produtosService.removeProduto(produtoId,uniProdsIds).subscribe(obj=>{
      const statusCode = obj
      // if (statusCode === 200) {
        this.toggleRemoverProduto()
        console.log(obj)
        window.location.reload()
        this.removerProdutoForm.patchValue({uniProdsIds:[]})
    // } else {
        // this.error = obj.body as Error;
        //chamar pop up
    // }
    })
  }

  onSelectAllChange() {
    this.selectAll = !this.selectAll;
    if(this.selectAll){
      console.log(this.removerProdutoForm.value)
      this.removerProdutoForm.value.uniProdsIds = []
      let uniPId = this.removerProdutoForm.value.uniProdsIds
  
      for(let up of this.produtoAremover?.uniProds!){
        this.removerProdutoForm.value.uniProdsIds.push(up.idUnidade);
      }
      this.removerProdutoForm.patchValue({uniProdsIds:this.removerProdutoForm.value.uniProdsIds})
    }else{
      this.removerProdutoForm.patchValue({uniProdsIds:[]})
    }
    console.log(this.removerProdutoForm.value);    
  }

  addUniProds(produtoId:number,uniProdsIds:Array<number>,preco:number,stock:number){
    this.produtosService.addUniProds(produtoId,uniProdsIds,preco,stock).subscribe(obj=>{
      const statusCode = obj
        this.toggleRemoverProduto()
        console.log(obj)
        window.location.reload()
        this.addProdutoForm.patchValue({uniProdsIds:[],stock:"",preco:""})
    })
  }

  onUniProdAdd(target1:any, idUniProd: number): void {
    let target = target1 as HTMLInputElement
  
    if (target.checked) {
      this.uniProdsIdsA.push(idUniProd);
    } else {
      const index = this.uniProdsIdsA.indexOf(idUniProd);
      if (index !== -1) {
        this.uniProdsIdsA.splice(index, 1);
      }
    }
    this.addProdutoForm.get('uniProdsIds')!.patchValue(this.uniProdsIdsA);
    console.log(this.addProdutoForm.value)

  }

  onUniProdRemove(target1:any, idUniProd: number): void {
    let target = target1 as HTMLInputElement
  
    if (target.checked) {
      this.uniProdsIds.push(idUniProd);
    } else {
      const index = this.uniProdsIds.indexOf(idUniProd);
      if (index !== -1) {
        this.uniProdsIds.splice(index, 1);
      }
    }
    this.removerProdutoForm.get('uniProdsIds')!.patchValue(this.uniProdsIds);
  }
}
