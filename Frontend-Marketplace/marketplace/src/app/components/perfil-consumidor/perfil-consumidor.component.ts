import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router} from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { Coordinate, SignUpDTO, UtilizadorDTO, UtilizadorInputDTO } from 'src/app/model/models';
import { UtilizadorService } from 'src/app/service/utilizador.service';

@Component({
  selector: 'app-perfil-consumidor',
  templateUrl: './perfil-consumidor.component.html',
  styleUrls: ['./perfil-consumidor.component.css']
})
export class PerfilConsumidorComponent implements OnInit {
  error?:Error;
  utilizador: UtilizadorDTO;

  role:string;
  showForm= false;
  noChanges = true;
  showAnswer = false
  answer:string
  success:boolean
  confimaRemover = false;
  updateForm: FormGroup;


  countries = Object.keys(SignUpDTO.PaisEnum).filter(key => isNaN(Number(key)));
  continents = Object.keys(SignUpDTO.ContinenteEnum).filter(key => isNaN(Number(key)));

  constructor(private route: ActivatedRoute, private utilizadorService: UtilizadorService, private appComponent:AppComponent,
     private router: Router){}
  
  ngOnInit(): void {
    if(this.appComponent.token && this.appComponent.role !== 'ROLE_ADMIN'){
      this.utilizadorService.getDetalhesUser().subscribe()
    }

      this.refresh();

      this.updateForm = new FormGroup({
        idFiscal: new FormControl('', Validators.required),
        nome: new FormControl('', Validators.required),
        telemovel: new FormControl('', Validators.required),
        // latitude: new FormControl('', Validators.required),
        // longitude: new FormControl('', Validators.required),
        morada: new FormControl('', Validators.required),
        freguesia: new FormControl('', Validators.required),
        municipio: new FormControl('', Validators.required),
        distrito: new FormControl('', Validators.required),
        pais: new FormControl('', Validators.required),
        // continente: new FormControl('', Validators.required)
  
      });

      this.updateForm.patchValue({
        idFiscal: this.utilizador!.idFiscal,
        nome: this.utilizador!.nome,
        telemovel: this.utilizador!.telemovel,
        // latitude: this.utilizador!.coordenadas?.latitude,
        // longitude: this.utilizador!.coordenadas?.longitude,
        morada: this.utilizador!.morada,
        freguesia: this.utilizador!.freguesia,
        municipio: this.utilizador!.municipio,
        distrito: this.utilizador!.distrito,
        pais: this.utilizador!.pais,
        // continente: this.utilizador!.continente
  
      });
      this.showForm=true;
  
      this.updateForm.valueChanges.subscribe(() => {
        this.noChanges = false;
      });
  }

  onPageRefresh(event: BeforeUnloadEvent): void {
    this.refresh();
  }

  openConfirmar(){
    this.confimaRemover = true
  }
  closeConfirmar(){
    this.confimaRemover = false
  }

  removerConta(){
    if(this.role === "ROLE_FORNECEDOR"){
      this.utilizadorService.removeFornecedor(this.utilizador?.idUtilizador!).subscribe(obj=>{
        const statusCode = obj.status
  
        if (statusCode === 200) {
          this.answer = "Conta removida com sucesso"
          this.success = true;
          this.utilizador = obj.body
          this.appComponent.user!.nome! =this.utilizador!.nome!
  
          this.openAnswer();
  
      } 
    }
    )
  
    } 
    if (this.role === "ROLE_CONSUMIDOR"){
      this.utilizadorService.removeConsumidor(this.utilizador?.idUtilizador!).subscribe(obj=>{
        const statusCode = obj.status
  
        if (statusCode === 200) {
          this.answer = "Conta removida com sucesso"
          this.success = true;
          this.utilizador = obj.body
          this.appComponent.user!.nome! =this.utilizador!.nome!
          this.openAnswer();
          //window.location.reload(); 
  
      } 
    }
    )
    }
  }
  

  refresh(){
    this.route.queryParams.subscribe((queryParams) => {
      this.role = queryParams["role"]
      this.utilizador = JSON.parse(queryParams["user"])
    });
  }

  onSubmit() {

    const coords : Coordinate = {latitude:this.updateForm.value.latitude,longitude:this.updateForm.value.longitude}
    const updateData: UtilizadorInputDTO = {
      idFiscal: this.updateForm.value.idFiscal,
      nome: this.updateForm.value.nome,
      telemovel: this.updateForm.value.telemovel,
      coordenadas: coords,
      morada: this.updateForm.value.morada,
      freguesia: this.updateForm.value.freguesia,
      municipio: this.updateForm.value.municipio,
      distrito: this.updateForm.value.distrito,
      pais: this.updateForm.value.pais,
      continente: this.getContinent(this.updateForm.value.pais),
    }

    if(this.role === "ROLE_FORNECEDOR"){
      this.utilizadorService.updateFornecedor(updateData).subscribe(obj=>{
        const statusCode = obj.status
  
        if (statusCode === 200) {
          this.answer = "Dados atualizados com sucesso!"
          this.success = true;
          this.utilizador = obj.body
          this.appComponent.user!.nome! =this.utilizador!.nome!

          this.openAnswer();
  
      } 
    }
    )

    } 
    if (this.role === "ROLE_CONSUMIDOR"){
      this.utilizadorService.updateConsumidor(updateData).subscribe(obj=>{
        const statusCode = obj.status
  
        if (statusCode === 200) {
          this.answer = "Dados atualizados com sucesso!"
          this.success = true;
          this.utilizador = obj.body
          this.appComponent.user!.nome! =this.utilizador!.nome!
          this.openAnswer();
      } 
    }
    )
    }

  }

  getContinent(pais: String){
    if(pais == "ALBANIA" || pais == "ANDORRA" || pais == "AUSTRIA" || pais == "BELGIUM" || pais == "BELARUS" || pais == "BOSNIA_HERZEGOVINA" || pais == "BULGARIA" || pais == "CROATIA" || pais == "CYPRUS" || pais == "CZECH_REPUBLIC" || pais == "DANZIG" || pais == "DENMARK" || pais == "ESTONIA" || pais == "FINLAND" || pais == "FRANCE" || pais == "GERMANY" || pais == "GREECE" || pais == "HOLY_ROMAN_EMPIRE" || pais == "HUNGARY" || pais == "ICELAND" || pais == "IRELAND" || pais == "ITALY" || pais == "KOSOVO" || pais == "LATVIA" || pais == "LIECHTENSTEIN" || pais == "LITHUANIA" || pais == "LUXEMBOURG" || pais == "MACEDONIA" || pais == "MALTA" || pais == "MOLDOVA" || pais == "MONACO" || pais == "MONTENEGRO" || pais == "MOUNT_ATHOS" || pais == "NETHERLANDS" || pais == "NORWAY" || pais == "POLAND" || pais == "PORTUGAL" || pais == "PRUSSIA" || pais == "ROMANIA" || pais == "SAN_MARINO" || pais == "SERBIA" || pais == "SLOVAKIA" || pais == "SLOVENIA" || pais == "SPAIN" || pais == "SWEDEN" || pais == "SWITZERLAND" || pais == "UNITED_KINGDOM" || pais == "UKRAINE" || pais == "VATICAN_CITY"){
      return SignUpDTO.ContinenteEnum.Europa;
    }else if(pais == "ALGERIA" || pais == "ANGOLA" || pais == "BENIN" || pais == "BOTSWANA" || pais == "BURKINA" || pais == "BURUNDI" || pais == "CAMEROON" || pais == "CAPE_VERDE" || pais == "CENTRAL_AFRICAN_REP" || pais == "CHAD" || pais == "COMOROS" || pais == "DEMOCRATIC_REPUBLIC_OF_THE_CONGO" || pais == "REPUBLIC_OF_THE_CONGO" || pais == "DJIBOUTI" || pais == "EGYPT" || pais == "EQUATORIAL_GUINEA" || pais == "ERITREA" || pais == "ETHIOPIA" || pais == "GABON" || pais == "THE_GAMBIA" || pais == "GHANA" || pais == "GUINEA" || pais == "GUINEA_BISSAU" || pais == "IVORY_COAST" || pais == "KENYA" || pais == "LESOTO" || pais == "LIBERIA" || pais == "LIBIA" || pais == "MADAGASCAR" || pais == "MALAWI" || pais == "MALI" || pais == "MAUTITANIA" || pais == "MAURITIUS" || pais == "MOROCCO" || pais == "MOZAMBIQUE" || pais == "NAMIBIA" || pais == "NIGER" || pais == "NIGERIA" || pais == "RWANDA" || pais == "SAO_TOME_PRINCIPE" || pais == "SENEGAL" || pais == "SEYCHELLES" || pais == "SIERRA_LEONE" || pais == "SOMALIA" || pais == "SOUTH_AFRICA" || pais == "SUDAN" || pais == "SWAZILAND" || pais == "TANZANIA" || pais == "TOGO" || pais == "TUNISIA" || pais == "UGANDA" || pais == "ZAMBIA" || pais == "ZIMBABUE"){
      return SignUpDTO.ContinenteEnum.Africa;
    }else if(pais == "AUSTRALIA" || pais == "MICRONESIA" || pais == "FIJI" || pais == "MARSHALL_ISLANDS" || pais == "SOLOMON_ISLANDS"  || pais == "KIRIBATI" || pais == "NAURU" || pais == "NEW_ZEALAND" || pais == "PALAU" || pais == "PAPUA_NEW_GUINEA" || pais == "SAMOA" || pais == "TONGA" || pais == "TUVALU" || pais == "VANUATU"){
      return SignUpDTO.ContinenteEnum.Oceania;
    }else if(pais == "ANTIGUA_DEPS" || pais == "ARGENTINA" || pais == "BAHAMAS" || pais == "BARBADOS" || pais == "BELIZE" || pais == "BOLIVIA" || pais == "BRAZIL" || pais == "CANADA" || pais == "CHILE" || pais == "COLOMBIA" || pais == "COSTA_RICA" || pais == "CUBA" || pais == "DOMINICA" || pais == "DOMINICAN_REPUBLIC" || pais == "EQUADOR" || pais == "EL_SALVADOR" || pais == "GEORGIA" || pais == "GRENADA" || pais == "GRENADINES" ||pais == "GUATEMALA" || pais == "GUYANA" || pais == "HAITI" || pais == "HONDURAS" || pais == "JAMAICA" || pais == "JONATHANLAN" || pais == "MEXICO" || pais == "NEWFOUNDLAND" || pais == "NICARAGUA" || pais == "PANAMA" || pais == "PARAGUAY" || pais == "PERU" || pais == "SURINAME" || pais == "TRINIDAD_TOBAGO" || pais == "URUGUAY" || pais == "USA" || pais == "VENEZUELA"){
        return SignUpDTO.ContinenteEnum.America;
    }else{
      return SignUpDTO.ContinenteEnum.Asia;
    }
  }

  openAnswer(){
    this.showAnswer = true;
  }


  closeAnswer(){
    if(this.confimaRemover){
      this.router.navigate(['/marketplace']);
      return;
    }

    this.showAnswer=false;
    let queryParams = {};
    queryParams = { role: this.role, user:JSON.stringify(this.utilizador!)};
    this.router.navigate(['/marketplace/perfil-consumidor'], { queryParams });
  }

  

}
