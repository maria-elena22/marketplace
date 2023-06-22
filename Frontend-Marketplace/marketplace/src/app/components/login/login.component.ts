import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UtilizadorService } from '../../service/utilizador.service';
import jwt_decode from 'jwt-decode';
import {DecodedToken} from '../../model/utilizador/decodedToken'
import {AppComponent} from '../../app.component'
import { Router } from '@angular/router';
import { Location } from '@angular/common';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent  implements OnInit {

  email: string;
  password: string;
  loginForm: FormGroup;
  token?:DecodedToken;
  role?:string;
  error?:Error;
  showAnswer = false
  answer:string
  success:boolean
  

  constructor(private formBuilder: FormBuilder, 
    private location: Location,
    private utilizadorService :UtilizadorService,
    private router: Router,
    private appComponent:AppComponent
    ) {}


  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  goToRegister(){
    this.router.navigate(['/marketplace/register'])
  }

  onSubmit() {
    const values = this.loginForm.value;

    this.utilizadorService.getLogin(values["email"],values["password"]).subscribe(
      (obj) => { 
        this.token = jwt_decode(obj.body['token']) as DecodedToken;
        localStorage.setItem('jwt_token', obj.body['token']);
        this.appComponent.token = this.token;
        

        this.role = this.token.role;
        this.appComponent.role = this.role;
        this.location.go('/marketplace');
        window.location.reload(); 
      },
      (error) => {
        // Handle error here
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

}
