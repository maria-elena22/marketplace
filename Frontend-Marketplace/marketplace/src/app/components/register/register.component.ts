import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

import { UtilizadorService } from '../../service/utilizador.service';
import jwt_decode from 'jwt-decode';
import { Location } from '@angular/common';
import {DecodedToken} from '../../model/utilizador/decodedToken'
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SignUpDTO } from '../../model/utilizador/signUpDTO';
import { AppComponent } from 'src/app/app.component';
import { Coordinate } from 'src/app/model/models';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

export class RegisterComponent implements OnInit{
  constructor(private appComponent:AppComponent ,private http: HttpClient,
    private location: Location, private router: Router, private utilizadorService : UtilizadorService)
    {

    }
  signUpForm: FormGroup;
  countries = Object.keys(SignUpDTO.PaisEnum).filter(key => isNaN(Number(key)));
  continents = Object.keys(SignUpDTO.ContinenteEnum).filter(key => isNaN(Number(key)));
  //PASSWORD
  oitoCaracteresPassw = false;
  maiusculasPassw = false;
  minusculasPassw = false;
  numerosPassw = false;
  startPassword = false;
  passwordEmpty = true;
  passwordValid = false;
  //IDFISCAL
  startIdFiscal = false;
  oitoCaracteresId = false;
  numerosId = false;
  letrasId = false;  
  IdFiscalEmpty = true;
  idFiscalValid = false;
  //CONTACTO
  startContacto = false;
  oitoCaracteresContacto = false;
  numerosContacto = false;
  letrasContacto = false;
  ContactoEmpty = true;
  contactoValid = false;
  //EMAIL
  startEmail = false;
  simboloEmail = false;
  finalEmail = false;
  pontoEmail = false;
  EmailEmpty = true;
  emailValid = false;

  //anwser
  role?:string;
  error?:Error;
  showAnswer = false
  answer:string
  success:boolean
  
  formValid = false;
  
  ngOnInit() {
    this.signUpForm = new FormGroup({
      idFiscal: new FormControl('', Validators.required),
      nome: new FormControl('', Validators.required),
      telemovel: new FormControl('', Validators.required),
      morada: new FormControl('', Validators.required),
      codPostal: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required),
      freguesia: new FormControl('', Validators.required),
      municipio: new FormControl('', Validators.required),
      distrito: new FormControl('', Validators.required),
      pais: new FormControl('', Validators.required),
      role: new FormControl('', Validators.required)

    });

  }

  goToLogin(){
    this.router.navigate(['/marketplace/login'])
  }


  onPasswordInput(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const passwordValue = inputElement.value;
    this.passwordEmpty = passwordValue.length > 0;
    // 8 caracteres
    this.oitoCaracteresPassw = passwordValue.length >= 8;

    // Maiusculas
    const uppercaseRegex = /[A-Z]/;
    this.maiusculasPassw = uppercaseRegex.test(passwordValue);

    // minusculas
    const lowercaseRegex = /[a-z]/;
    this.minusculasPassw = lowercaseRegex.test(passwordValue);

    // numbers
    const numericRegex = /\d/;
    this.numerosPassw = numericRegex.test(passwordValue);

    if(this.maiusculasPassw && this.oitoCaracteresPassw && this.minusculasPassw && this.numerosPassw){
      this.passwordValid = true;
    }
  }

  onIdFiscalInput(event: Event){
    const inputElement = event.target as HTMLInputElement;
    const idFiscalValue = inputElement.value;
    this.IdFiscalEmpty = idFiscalValue.length > 0;
    // 8 caracteres
    this.oitoCaracteresId = idFiscalValue.length >= 9;

    // numbers
    const numericRegex = /\d/;
    this.numerosId = numericRegex.test(idFiscalValue);

    //letras
    const letras = /[a-zA-Z]/;    
    this.letrasId = letras.test(idFiscalValue);

    //special
    const specialChars = /[^a-zA-Z0-9]/;
    const hasSpecialChars = specialChars.test(idFiscalValue);

    if(this.oitoCaracteresId && this.numerosId && !this.letrasId){
      this.idFiscalValid = true;
    } else if(this.letrasId || hasSpecialChars){
      this.signUpForm.patchValue({idFiscal:inputElement.value.replace(/\D/g, "")})
    }
  }

  onContactoInput(event: Event){
    
    const inputElement = event.target as HTMLInputElement;
    const ContactoValue = inputElement.value;
    this.ContactoEmpty = ContactoValue.length > 0;
    //9 caracteres
    this.oitoCaracteresContacto = ContactoValue.length >= 9;
    
    // numbers
    const numericRegex = /\d/;
    this.numerosContacto = numericRegex.test(ContactoValue);

    //letras
    const letras = /[a-zA-Z]/;    
    this.letrasContacto = letras.test(ContactoValue);

    //special
    const specialChars = /[^a-zA-Z0-9]/;
    const hasSpecialChars = specialChars.test(ContactoValue);

    if(this.oitoCaracteresContacto && this.numerosContacto && !this.letrasContacto){
      this.contactoValid = true;
    } 
    else if(this.letrasContacto || hasSpecialChars){
      this.signUpForm.patchValue({telemovel:inputElement.value.replace(/\D/g, "")})
    }
  }

  onEmailInput(event: Event){
    const inputElement = event.target as HTMLInputElement;
    const EmailValue = inputElement.value;
    this.EmailEmpty = EmailValue.length > 0;
    //@
    const final = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/g;    
    this.finalEmail = final.test(EmailValue);

    //@
    const simbolo = /[@]/;    
    this.simboloEmail = simbolo.test(EmailValue);

     //@
     const ponto = /(\.)+[\w-]{2,4}$/g;    
     this.pontoEmail = ponto.test(EmailValue);

     if(this.finalEmail && this.simboloEmail && this.pontoEmail){
      this.emailValid = true;
    }
  }

  geocodeAddress(address: string): Observable<HttpResponse<any>> {
    const headers = new HttpHeaders();

    //const url = `${environment.mapsUrl}?address=${address}&key=${environment.mapsKey}`;
    // address = `https://maps.googleapis.com/maps/api/geocode/json?address=${address}&key=AIzaSyBboXYv2JYM8DJHRKXzG3ipHTlmjJpc_jM`
    
    const url = `${environment.mapsUrl}address?key=${environment.mapsKey}&location=${address}`
    return this.http.get<any>(url, { headers, observe: 'response' });
  }

  onSubmit() {
    let codPostal= this.signUpForm.value.codPostal.replace(" ","")
    let coords : Coordinate
    this.geocodeAddress(codPostal).subscribe(
      (obj)=>{

        let latitude = obj.body.results[0].locations[0].latLng.lat
        let longitude = obj.body.results[0].locations[0].latLng.lng

        coords = {latitude: latitude,longitude: longitude}
        const signUpData: SignUpDTO = {
            idFiscal: parseInt(this.signUpForm.value.idFiscal),
            nome: this.signUpForm.value.nome,
            telemovel: parseInt(this.signUpForm.value.telemovel),
            coordenadas: coords,
            morada: this.signUpForm.value.morada,
            email: this.signUpForm.value.email,
            password: this.signUpForm.value.password,
            freguesia: this.signUpForm.value.freguesia,
            municipio: this.signUpForm.value.municipio,
            distrito: this.signUpForm.value.distrito,
            pais: this.signUpForm.value.pais,
            continente: this.getContinent(this.signUpForm.value.pais),
        }

          if(this.emailValid && this.contactoValid && this.idFiscalValid && this.passwordValid){
            const role = this.signUpForm.value.role;
            if(role === "fornecedor"){

              this.insertFornecedor(signUpData);
            } 
            if (role === "consumidor"){
              this.insertConsumidor(signUpData);
          }
          } else{
            this.success = false
            this.answer = "Erro! Verifique que preencheu os campos corretamente"
            this.toggleAnswer()
          }
        
      }
      
    )


  }


  insertFornecedor(signUpData:SignUpDTO){
    this.utilizadorService.insertFornecedor(signUpData).subscribe(
      (obj)=>{
      const statusCode = obj.status

      if (statusCode === 200) {
        const token = jwt_decode(obj.body['token']) as DecodedToken;
        localStorage.setItem('jwt_token', obj.body['token']);
        this.appComponent.token = token;
    
        this.location.go('/marketplace');
        window.location.reload(); 
        
      }
      }, (error) => {
        // Handle error here
        console.log('An error occurred:', error);
        this.success = false
        this.answer = error
        this.toggleAnswer()
      }
    )
  }

  insertConsumidor(signUpData:SignUpDTO){
    this.utilizadorService.insertConsumidor(signUpData).subscribe(
      (obj)=>{
      const statusCode = obj.status

      if (statusCode === 200) {
        const token = jwt_decode(obj.body['token']) as DecodedToken;
        localStorage.setItem('jwt_token', obj.body['token']);
        this.appComponent.token = token;
        
        this.location.go('/marketplace');
        window.location.reload(); 
      
      }
      }, (error) => {
        console.log('An error occurred:', error);
        this.success = false
        this.answer = error
        this.toggleAnswer()
      }
    )
  }
    
  toggleAnswer(){
    this.showAnswer = !this.showAnswer;
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
}