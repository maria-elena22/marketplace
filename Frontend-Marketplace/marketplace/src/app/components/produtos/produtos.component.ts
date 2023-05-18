import { Component, OnInit } from '@angular/core';
import { FullCategoriaDTO, FullProdutoDTO ,ProdutoDTO,ProdutoFornecedorDTO, ProdutoPropriedadesDTO, ProdutoUniProdDTO, PropriedadeDTO, SimpleItemDTO, SimpleSubCategoriaDTO, SimpleUtilizadorDTO, SubCategoriaDTO, UniProdDTO} from 'src/app/model/models';
import { ActivatedRoute, Router} from '@angular/router';
import{ ProdutosService } from '../../service/produtos.service'
import { AppComponent } from 'src/app/app.component';
import { FormBuilder, FormGroup, FormControl, Validators, FormArray,ValidationErrors ,AbstractControl} from '@angular/forms';
import { UniProdsService } from 'src/app/service/uni-prods.service';
import { CategoriaService } from 'src/app/service/categoria.service';
import { CestoService } from 'src/app/service/cesto.service';



@Component({
  selector: 'app-produtos',
  templateUrl: './produtos.component.html',
  styleUrls: ['./produtos.component.css']
})
export class ProdutosComponent implements OnInit {

  produtos : FullProdutoDTO[]=[]; 
  produtosToShow:FullProdutoDTO[]=[];
  idCategoria:number;
  idSubCategoria:number;
  ivas = ['IVA_6' , 'IVA_13' , 'IVA_23'];
  uniProdsP:UniProdDTO[]=[]
  subCategorias?:SubCategoriaDTO[]=[] //??
  categorias:FullCategoriaDTO[] = [];
  uniProdsR:UniProdDTO[]=[]
  uniProdsA:UniProdDTO[]=[]

  prodComparar1?:FullProdutoDTO;
  prodComparar2?:FullProdutoDTO;


  // forms
  produtoForm:FormGroup;
  addCarrinhoForm:FormGroup;
  removerProdutoForm:FormGroup;
  addProdutoForm:FormGroup;
  searchProdutoForm: FormGroup;


  // novo produto
  subCategoriasIds:number[] = [];
  uniProdsIds:number[] = [];
  propriedadesVisible:boolean = false
  categoriasExpanded:FullCategoriaDTO[] = []
  propriedadesToComplete:PropriedadeDTO[]=[]
  categoriasEscolhidas:FullCategoriaDTO[] = []
  subcategoriasEscolhidas:SimpleSubCategoriaDTO[] = []
  propriedadesCompletas:{[key: string]: string}
  
  // user
  role?:string;
  meusProdutos : ProdutoFornecedorDTO[]=[];

  // resposta
  showAnswer = false
  success? : boolean
  answer?:string
  error?: Error  

  // modal
  showModal: boolean = false;
  showModalCarrinho : boolean = false;
  showAddProduto: boolean = false;
  showRemoverProduto: boolean = false;
  produtoAfornecer?:FullProdutoDTO
  produtoAremover?:FullProdutoDTO
  selectAll: boolean = false;
  addUnis = false
  showComparar = false
  showFilter = true

  uniProdsIdsA:number[]=[]

  page = 0
  previousButtonDisabled = true
  nextButtonDisabled = false

  // carrinho
  produtoAadicionar: FullProdutoDTO;


  constructor(private cestoService : CestoService, private uniProdService:UniProdsService,private router : Router,
    private route: ActivatedRoute,private formBuilder: FormBuilder, private produtosService : ProdutosService, 
    private appComponent : AppComponent, private categoriaService: CategoriaService) {           }

  

  ngOnInit(): void {
    this.refresh()
    this.role = this.appComponent.role;
    if(this.role ==="ROLE_FORNECEDOR"){
      this.minhasUniProds()
      this.todasCategorias()
      this.subCategoriasIds = []
      this.uniProdsIds = []
      this.produtoForm = new FormGroup({
          nome: new FormControl('', Validators.required),
          descricao: new FormControl('', Validators.required),
          iva: new FormControl('', Validators.required),
        stock: new FormControl('', [Validators.required,Validators.pattern(/^[1-9][0-9]*$/)]),
        propriedades: new FormGroup({
          myControl: new FormControl('', Validators.required)
        }),    
        uniProdsIds: new FormControl([], Validators.required),
        subCategoriasIds: new FormControl([], Validators.required),
        preco: new FormControl('', [Validators.required,Validators.pattern(/^[0-9]+(\.[0-9]{1,2})?$/)]),

      });

      this.removerProdutoForm = new FormGroup({
        uniProdsIds: new FormControl([], Validators.required)

      })
      this.addProdutoForm = new FormGroup({
        uniProdsIds: new FormControl([], Validators.required),
        preco:new FormControl("", Validators.required),
        stock:new FormControl("", Validators.required)

      })
      

    } else{
      this.addCarrinhoForm = new FormGroup({
        quantidade: new FormControl(0, [Validators.required,Validators.pattern(/^[1-9][0-9]*$/)]),
        fornecedor:new FormControl(0, Validators.required)
      });
      
    }
    this.searchProdutoForm = new FormGroup({
      nomeProduto: new FormControl("", Validators.required),
      precoMin:new FormControl("", Validators.required),
      precoMax:new FormControl("", Validators.required)

    })
    
  }

  closeRemoveForm(){
    this.showRemoverProduto = !this.showRemoverProduto
    this.produtoAremover = undefined
    this.selectAll = false
    this.removerProdutoForm.patchValue({uniProdsIds:[]})
  }

  removeComparar(prod:number){
    if(prod === 1){
      this.prodComparar1 = undefined
    }
    if(prod === 2){
      this.prodComparar2 = undefined
    }
  }

  comparar(){
    console.log(this.prodComparar1)
    console.log(this.prodComparar2)
    this.showComparar = true

  }

  addComparar(produto:FullProdutoDTO){
    console.log
    if(produto === this.prodComparar1 || produto === this.prodComparar2){
      return;
    }
    if(this.prodComparar1 && !this.prodComparar2){
      this.prodComparar2 = produto
    }
    if(!this.prodComparar1 && this.prodComparar2){
      this.prodComparar1 = produto
    }
    if(!this.prodComparar1 && !this.prodComparar2){
      this.prodComparar1 = produto
    }

    console.log(this.prodComparar1)
    console.log(this.prodComparar2)

  }

  // ---------------------- ADD CARRINHO ----------------------
  
  showAddCarrinho(produto:FullProdutoDTO){
    this.showModalCarrinho = true;
    this.produtoAadicionar = produto;

  }

  onSubmitProduto() {
    
    const itemData: SimpleItemDTO = {
      produtoId : this.produtoAadicionar.idProduto,
      fornecedorId: this.addCarrinhoForm.value.fornecedor,
      quantidade: this.addCarrinhoForm.value.quantidade
    }

    console.log(itemData)
    this.cestoService.addToCart(itemData)
    this.toggleModalCarrinho()
    this.success = true
    this.answer = "Produto adicionado ao cesto!"
    this.toggleAnswerC()
    this.addCarrinhoForm.reset();
  

  } 

  // ---------------------- ADD PRODUTO TO ME ----------------------


  forneco(produto:FullProdutoDTO):boolean{
    const myId = this.appComponent.user?.idUtilizador;
    for (let pf of produto.precoFornecedores!){
      if(pf.fornecedor?.idUtilizador === myId){
        return true;
      }
    }
    return false;

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

  

  toggleAddProduto(produto?:FullProdutoDTO){
    this.produtoAfornecer=produto;
    this.showAddProduto = !this.showAddProduto
    if(this.showAddProduto === false){
      this.uniProdsA = []
    }
  }

  toggleAddUniProds(produto?:ProdutoFornecedorDTO){
    this.produtoAfornecer=produto;
    this.addUnis = !this.addUnis
    if(produto?.uniProds){
      for(let up of this.uniProdsP){
        if(produto.uniProds.filter(uni => uni.idUnidade === up.idUnidade).length === 0){ 
          //se up nao estiver em produto uniprods add to upA
          this.uniProdsA.push(up)
        }
      }
    }
    this.showAddProduto = !this.showAddProduto
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
            const url = '/produtos';

            window.history.pushState(state, url);
            console.log(this.produtoAremover?.uniProds)
            
            this.showRemoverProduto = !this.showRemoverProduto
          }
        }

      }
    })


    
  }

  onSubmitRemover(){

    const produtoId = this.produtoAremover!.idProduto!
    const uniProdsIds = this.removerProdutoForm.value
     
    console.log(this.removerProdutoForm.value.uniProdsIds)
    this.removeProduto(produtoId,this.removerProdutoForm.value.uniProdsIds)

  }

  onSubmitFornecer(){

    const produtoId = this.produtoAfornecer!.idProduto!
    const uniProdsIds = this.addProdutoForm.value.uniProdsIds
    const preco = this.addProdutoForm.value.preco
    const stock = this.addProdutoForm.value.stock
     
    this.addUniProds(produtoId,uniProdsIds,preco,stock);
    this.uniProdsA = []

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

  addUniProds(produtoId:number,uniProdsIds:Array<number>,preco:number,stock:number){
    this.produtosService.addUniProds(produtoId,uniProdsIds,preco,stock).subscribe(obj=>{
      const statusCode = obj
        this.toggleRemoverProduto()
        console.log(obj)
        window.location.reload()
        this.addProdutoForm.patchValue({uniProdsIds:[],stock:"",preco:""})

    })

  }

  getFirstThird(array: any[]): any[] {
    const third = Math.ceil(array.length / 3);
    return array.slice(0, third);
  }

  getSecondThird(array: any[]): any[] {
    const third = Math.ceil(array.length / 3);
    const start = third;
    const end = third * 2;

    return array.slice(start, end);
  }

  getLastThird(array: any[]): any[] {
    const third = Math.ceil(array.length / 3);
    const start = third * 2;

    return array.slice(start);
  }

  // ---------------------- VER PRODUTOS ----------------------


  onSubmitSearch() {
    

    this.produtosService.getProdutos(this.idCategoria,this.idSubCategoria,undefined,
          this.searchProdutoForm.value.nomeProduto,this.searchProdutoForm.value.precoMin,this.searchProdutoForm.value.precoMax).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.produtos = obj.body as FullProdutoDTO[];
        console.log(obj.body)
        this.meusProdutos = []
        
    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }
    })
  

  } 

  verTodosProdutos(){
    this.showFilter = true
    this.getProdutos(-1,-1);
    
    this.router.navigate(['/produtos']);
    //window.location.reload()

  }

  nextPage(){
    this.page +=1
    if(this.produtos.length >0){
      this.getProdutos(this.idCategoria,this.idSubCategoria);
    }
    console.log(this.page)
    this.previousButtonDisabled = false

    // if(this.transportes.length===0){

    //   console.log(this.transportes)
      
    // } else{
      // const state = { page: 'transportes' };
      // const url = '/transportes';
      // this.previousButtonDisabled = false
      // window.history.pushState(state, url);
      

    //}
    
  }
  previousPage(){
    this.page -=1
    if(this.page<0){  
      this.page += 1
      this.previousButtonDisabled = true

    } else{
      this.nextButtonDisabled = false

      if(this.produtos.length >0){
        this.getProdutos(this.idCategoria,this.idSubCategoria);
      }

      // const state = { page: 'transportes' };
      // const url = '/transportes';
      // this.nextButtonDisabled = false
      // window.history.pushState(state, url);

    }
    

    

  }

  getProdutos(idCategoria:number, idSubCategoria:number){

    this.produtosService.getProdutos(idCategoria,idSubCategoria,undefined,undefined,undefined,undefined,this.page).subscribe(obj=>{
      const statusCode = obj.status
      if (statusCode === 200) {
        this.produtos = obj.body as FullProdutoDTO[];
        console.log(obj.body)
        this.meusProdutos = []
        if(this.produtos.length ===0 && this.page>0){
          this.page -=1
          this.nextButtonDisabled = true
          this.getProdutos(this.idCategoria,this.idSubCategoria)
        }
        
    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }
    })
  }

  verMeusProdutos(idCategoria:number, idSubCategoria:number){

    this.prodComparar1 = undefined
    this.prodComparar2 =undefined
    this.showFilter = false
    this.produtosService.getMeusProdutos(idCategoria,idSubCategoria).subscribe(obj=>{
      const statusCode = obj.status
  
      if (statusCode === 200) {
        this.meusProdutos = obj.body as ProdutoFornecedorDTO[];
        this.produtos = []
        console.log(this.meusProdutos)
        const state = { page: 'produtos' };
        const url = '/produtos';

        window.history.pushState(state, url);

    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }
    })

  }

  refresh(){
    this.route.queryParams.subscribe((queryParams) => {
      
      this.idCategoria = queryParams["categoria"] === undefined? -1:queryParams["categoria"]
      this.idSubCategoria = queryParams["subCategoria"]=== undefined? -1:queryParams["subCategoria"]
      this.getProdutos(this.idCategoria,this.idSubCategoria);

    });

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

  getMeuPreco(produto : ProdutoFornecedorDTO) : string{
    const pf = produto.precoFornecedor
    let preco = '';
    if(pf){
      pf[0].preco!.toFixed(2);
      preco += pf[0].preco!.toFixed(2);

    } else{
      preco += "Produto não disponível";
    }
    

    return preco;
  }

  getIntervaloPrecos(produto : FullProdutoDTO) : string{
    let precos = '';
    let pfs = produto.precoFornecedores;
    if(pfs && pfs.length !== 0){
      let precoMin = pfs[0].preco;
      let precoMax = pfs[0].preco;
      for(let pf of pfs){
        precoMin = pf.preco! < precoMin! ? pf.preco : precoMin;
        precoMax = pf.preco! > precoMax! ? pf.preco : precoMax;
      }
      if(precoMax == precoMin){
        precos += precoMin?.toFixed(2) + '€';
        // `${precoMin}€`
      } else{
        precos = `${precoMin?.toFixed(2)}€ - ${precoMax?.toFixed(2)}€`

      }

    } else{
      precos = "Produto não disponível";
    }


    return precos;
  }


  // ---------------------- NOVO PRODUTO  (rever remover propriedades) ----------------------

  onPropChange(prop:PropriedadeDTO,event:any){
    
    const inputValue = event.target.value;

    if(!this.propriedadesCompletas){
      this.propriedadesCompletas ={}
    }
    this.propriedadesCompletas[`${prop.idPropriedade}`] = inputValue
  }

  atualizarProps():boolean{

    for (const key in this.propriedadesCompletas) {
      const myString = this.propriedadesCompletas[key];
      const regex = /^\s+/;
      if (regex.test(myString)) {
        return false;
      }
      

    }
    return true;
  }

  onUniProdChange(target1:any, idUniProd: number): void {
    let target = target1 as HTMLInputElement
  
    if (target.checked) {
      this.uniProdsIds.push(idUniProd);
    } else {
      const index = this.uniProdsIds.indexOf(idUniProd);
      if (index !== -1) {
        this.uniProdsIds.splice(index, 1);
      }
    }
    this.produtoForm.get('uniProdsIds')!.patchValue(this.uniProdsIds);


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

  isSubCategoriaChecked(idSubCategoria?:number){
    if(idSubCategoria){
      return this.subCategoriasIds.some(id => id === idSubCategoria);
    } 
    return false;


  }

  onSubcategoriaChange(target1:any, subCategoria: SimpleSubCategoriaDTO, categoriaPai: FullCategoriaDTO): void {
    let target = target1 as HTMLInputElement
    const index = this.subCategoriasIds.indexOf(subCategoria.idSubCategoria!);
    const indexC = this.categoriasEscolhidas.indexOf(categoriaPai) 
    const indexS = this.subcategoriasEscolhidas.indexOf(subCategoria)

    if (target.checked) {
      if (index === -1) {
        this.subCategoriasIds.push(subCategoria.idSubCategoria!);
      }
      if(indexS === -1){
        this.subcategoriasEscolhidas.push(subCategoria)

      }
      if(indexC === -1){
        this.categoriasEscolhidas.push(categoriaPai)
        this.propriedadesVisible=true
        this.adicionarPropriedades(categoriaPai)

      }
    } else {
      if (index !== -1) {
        this.subCategoriasIds.splice(index, 1);

        
      }
      if(indexS !== -1){
        this.subcategoriasEscolhidas.splice(indexS, 1);

      }
      if(indexC !== -1){
        //só posso tirar se nao houver mais subcategorias daquela categoria
        let possoTirar = true
        for(let sc of this.subcategoriasEscolhidas){
          if(this.getCategoriaForSubcategoria(subCategoria,this.categorias) === categoriaPai){
            possoTirar = false
            break
          }
        }
        if(possoTirar){
          this.categoriasEscolhidas.splice(indexC, 1);
          if(this.categoriasEscolhidas.length ===0){
            this.propriedadesVisible =false
          }
          this.removerPropriedades(categoriaPai)


        }
        
      }
    }
    this.produtoForm.get('subCategoriasIds')!.patchValue(this.subCategoriasIds);

  }

  adicionarPropriedades(categoria:FullCategoriaDTO){
    const props = categoria.propriedadesList
    if(props){
      for(let propriedade of props){
        if(!(this.propriedadesToComplete.some(p => p.idPropriedade === propriedade.idPropriedade))){
          this.propriedadesToComplete.push(propriedade)
        }
      }
    }
    console.log(this.propriedadesToComplete)

    
  }

  removerPropriedades(categoria: FullCategoriaDTO) {
    const props = categoria.propriedadesList;
    if (props) {
      categoria.propriedadesList = props.filter(p => !this.removerPropriedadePartilhada(p));
      this.propriedadesToComplete.push(...props.filter(p => !this.propriedadesToComplete.includes(p)));
    }
    console.log(this.propriedadesToComplete)
  }

  removerPropriedadePartilhada(prop: PropriedadeDTO): boolean {
    return !this.categoriasEscolhidas.some(categoria => categoria.propriedadesList?.includes(prop));
  }

  getCategoriaForSubcategoria(subcategoria: any, categorias: any[]): any {
    // Find the categoria that contains the given subcategoria
    for (let categoria of categorias) {
      for (let subcategoriaOfCategoria of categoria.subCategoriaList) {
        if (subcategoriaOfCategoria.idSubCategoria === subcategoria.idSubCategoria) {
          return categoria;
        } else if (subcategoriaOfCategoria.subCategoriasFilhos && subcategoriaOfCategoria.subCategoriasFilhos.length > 0) {
          // Recursively search the subcategorias of this subcategoria
          let subcategoriaParentCategoria = this.getCategoriaForSubcategoria(subcategoria, [subcategoriaOfCategoria]);
          if (subcategoriaParentCategoria) {
            return subcategoriaParentCategoria;
          }
        }
      }
    }
    // The parent categoria was not found
    return null;
  }
  
  isCategoriaExpanded(categoria: FullCategoriaDTO): boolean {
    return this.categoriasExpanded.some(c => c.idCategoria === categoria.idCategoria);
  }

  showCategorias(categoria :FullCategoriaDTO){
    if(this.categoriasExpanded.some(c => c.idCategoria === categoria.idCategoria)){
      const index = this.categoriasExpanded.indexOf(categoria);
      if (index !== -1) {
        this.categoriasExpanded.splice(index, 1);
      }
    }else{
      this.categoriasExpanded.push(categoria)
    }

  }

  todasCategorias(){
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

  onSubmit() {
    const produto: ProdutoDTO = {
      nome: this.produtoForm.value.nome,
      descricao: this.produtoForm.value.descricao,
      iva: this.produtoForm.value.iva
    }
    const produtoData: ProdutoPropriedadesDTO = {
      produtoDTO : produto,
      propriedades: this.propriedadesCompletas,
      stock: this.produtoForm.value.stock
    }

  
    this.produtosService.insertProduto(produtoData, this.uniProdsIds, this.subCategoriasIds, this.produtoForm.value.preco)
  .subscribe(
    (obj) => {
      const statusCode = obj.status
      this.success = statusCode === 200
      if (this.success) {
        this.answer = "Produto adicionado com sucesso"
        // this.toggleModal()
        // this.verMeusProdutos(-1,-1)
        // window.location.reload();
        this.toggleAnswer()
      } else {
        this.answer = obj.statusText
        this.toggleAnswer()
      }
    },
    (error) => {
      console.log("An error occurred:", error);
      // Handle the error here, for example, you can display an error message to the user
      this.answer = "Ocorreu um erro ao adicionar o produto. Tente novamente mais tarde. "
      this.toggleAnswer()
    }
  );

  

  } 

  // ---------------------- MODALS ----------------------

  toggleModal(){
    this.showModal = !this.showModal;
    //Criar Uni Prod
  }

  toggleAnswerC(){
    if(!this.showAnswer){
      if(this.success){
        this.toggleModal()
        this.getProdutos(-1,-1)
        this.showAnswer = true
      }else{
        this.showAnswer = true

      }
      
    }else{
      this.showAnswer = false
    }
  }

  toggleAnswer(){
    if(!this.showAnswer){
      if(this.success){
        this.toggleModal()
        this.verMeusProdutos(-1,-1)
        this.showAnswer = true
      }else{
        this.showAnswer = true

      }
      
    }else{
      this.showAnswer = false
    }
  }

  toggleModalCarrinho(){
    this.showModalCarrinho = !this.showModalCarrinho;
  }

  
  

}
