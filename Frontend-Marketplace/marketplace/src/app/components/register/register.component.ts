import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

import { UtilizadorService } from '../../service/utilizador.service';
import jwt_decode from 'jwt-decode';
import { Location } from '@angular/common';
import {DecodedToken} from '../../model/utilizador/decodedToken'
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { SignUpDTO } from '../../model/utilizador/signUpDTO';
import { Pais } from 'src/app/model/utils/pais';
import { AppComponent } from 'src/app/app.component';
import { Coordinate } from 'src/app/model/models';

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
  //IDFISCAL
  startIdFiscal = false;
  oitoCaracteresId = false;
  numerosId = false;
  letrasId = false;  
  IdFiscalEmpty = true;
  //CONTACTO
  startContacto = false;
  oitoCaracteresContacto = false;
  numerosContacto = false;
  letrasContacto = false;
  ContactoEmpty = true;
  //EMAIL
  startEmail = false;
  simboloEmail = false;
  finalEmail = false;
  pontoEmail = false;
  EmailEmpty = true;

  //anwser
  role?:string;
  error?:Error;
  showAnswer = false
  answer:string
  success:boolean
  
  ngOnInit() {
    this.signUpForm = new FormGroup({
      idFiscal: new FormControl('', Validators.required),
      nome: new FormControl('', Validators.required),
      telemovel: new FormControl('', Validators.required),
      latitude: new FormControl('', Validators.required),
      longitude: new FormControl('', Validators.required),
      morada: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required),
      freguesia: new FormControl('', Validators.required),
      municipio: new FormControl('', Validators.required),
      distrito: new FormControl('', Validators.required),
      pais: new FormControl('', Validators.required),
      continente: new FormControl('', Validators.required),
      role: new FormControl('', Validators.required)

    });

    console.log(this.countries)

  }

  goToLogin(){
    this.router.navigate(['/login'])
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
  }

  onIdFiscalInput(event: Event){
    const inputElement = event.target as HTMLInputElement;
    const idFiscalValue = inputElement.value;
    this.IdFiscalEmpty = idFiscalValue.length > 0;
    // 8 caracteres
    this.oitoCaracteresId = idFiscalValue.length >= 8;

    // numbers
    const numericRegex = /\d/;
    this.numerosId = numericRegex.test(idFiscalValue);

    //letras
    const letras = /[a-zA-Z]/;    
    this.letrasId = letras.test(idFiscalValue);
  }

  onContactoInput(event: Event){
    const inputElement = event.target as HTMLInputElement;
    const ContactoValue = inputElement.value;
    this.ContactoEmpty = ContactoValue.length > 0;
    // 8 caracteres
    this.oitoCaracteresContacto = ContactoValue.length >= 8;

    // numbers
    const numericRegex = /\d/;
    this.numerosContacto = numericRegex.test(ContactoValue);

    //letras
    const letras = /[a-zA-Z]/;    
    this.letrasContacto = letras.test(ContactoValue);
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
 
  }

  onSubmit() {
    const coords : Coordinate = {latitude:this.signUpForm.value.latitude,longitude:this.signUpForm.value.longitude}
    console.log(coords)
    const signUpData: SignUpDTO = {
      idFiscal: this.signUpForm.value.idFiscal,
      nome: this.signUpForm.value.nome,
      telemovel: this.signUpForm.value.telemovel,
      coordenadas: coords,
      morada: this.signUpForm.value.morada,
      email: this.signUpForm.value.email,
      password: this.signUpForm.value.password,
      freguesia: this.signUpForm.value.freguesia,
      municipio: this.signUpForm.value.municipio,
      distrito: this.signUpForm.value.distrito,
      pais: this.signUpForm.value.pais,
      continente: this.signUpForm.value.continente,
    }
    console.log(signUpData);
    console.log(this.signUpForm.value.role);
    const role = this.signUpForm.value.role;
    if(role === "fornecedor"){
      this.utilizadorService.insertFornecedor(signUpData).subscribe(
        (obj)=>{
        const statusCode = obj.status
        console.log("-------------------")
  
        if (statusCode === 200) {
          const token = jwt_decode(obj.body['token']) as DecodedToken;
          localStorage.setItem('jwt_token', obj.body['token']);
          this.appComponent.token = token;
          
          this.location.go('');
          window.location.reload(); 
  
        }
      }, (error) => {
        // Handle error here
        console.log('An error occurred:', error);
        this.success = false
        this.answer = "Registo não foi feito. Verifique que preencheu todos os campo corretamente"
        this.toggleAnswer()
      }
    )

    } 
    if (role === "consumidor"){
      this.utilizadorService.insertConsumidor(signUpData).subscribe(
        (obj)=>{
        const statusCode = obj.status
        console.log("-------------------")
  
        if (statusCode === 200) {
          const token = jwt_decode(obj.body['token']) as DecodedToken;
          localStorage.setItem('jwt_token', obj.body['token']);
          this.appComponent.token = token;
          
          this.location.go('');
          window.location.reload(); 
        
        }
      }, (error) => {
        // Handle error here
        console.log('An error occurred:', error);
        this.success = false
        this.answer = "Registo não foi feito. Verifique que preencheu todos os campo corretamente"
        this.toggleAnswer()
      }
    )
    }
  }
    toggleAnswer(){
      this.showAnswer = !this.showAnswer;
    }
}
