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
  oitoCaracteres = false;
  maiusculas = false;
  minusculas = false;
  numeros = false;
  startPassword = false;
  passwordEmpty = true

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
    this.router.navigate(['/marketplace/login'])
  }


  onPasswordInput(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const passwordValue = inputElement.value;
    this.passwordEmpty = passwordValue.length > 0;
    // 8 caracteres
    this.oitoCaracteres = passwordValue.length >= 8;

    // Maiusculas
    const uppercaseRegex = /[A-Z]/;
    this.maiusculas = uppercaseRegex.test(passwordValue);

    // minusculas
    const lowercaseRegex = /[a-z]/;
    this.minusculas = lowercaseRegex.test(passwordValue);

    // numbers
    const numericRegex = /\d/;
    this.numeros = numericRegex.test(passwordValue);


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
      this.utilizadorService.insertFornecedor(signUpData).subscribe(obj=>{
        const statusCode = obj.status
        console.log("-------------------")
  
        if (statusCode === 200) {
          const token = jwt_decode(obj.body['token']) as DecodedToken;
          localStorage.setItem('jwt_token', obj.body['token']);
          this.appComponent.token = token;
          
          this.location.go('/marketplace');
          window.location.reload(); 
  
      } 
    }
    )

    } 
    if (role === "consumidor"){
      this.utilizadorService.insertConsumidor(signUpData).subscribe(obj=>{
        const statusCode = obj.status
        console.log("-------------------")
  
        if (statusCode === 200) {
          const token = jwt_decode(obj.body['token']) as DecodedToken;
          localStorage.setItem('jwt_token', obj.body['token']);
          this.appComponent.token = token;
          
          this.location.go('/marketplace');
          window.location.reload(); 
  
      } 
    }
    )
    }

  }



  

  
}
