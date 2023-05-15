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

  onSubmit() {
    console.log(this.loginForm.value);
    const values = this.loginForm.value;

    this.utilizadorService.getLogin(values["email"],values["password"]).subscribe(obj=>{
      const statusCode = obj.status
      console.log("-------------------")

      if (statusCode === 200) {
        this.token = jwt_decode(obj.body['token']) as DecodedToken;
        localStorage.setItem('jwt_token', obj.body['token']);
        console.log(this.token)
        this.appComponent.token = this.token;
        

        this.role = this.token.role;
        console.log(this.role); // 'admin'
        this.appComponent.role = this.role;
        this.location.go('');
        window.location.reload(); 

    } else {
        this.error = obj.body as Error;
        //chamar pop up

    }
    })
  }

  // email = new FormControl('', [Validators.required, Validators.email]);

  // getErrorMessage() {
  //   return this.email.hasError('required') ? 'You must enter a value' :
  //       this.email.hasError('email') ? 'Not a valid email' :
  //           '';
  // }
}
