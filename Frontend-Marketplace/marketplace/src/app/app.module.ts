import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { TermosCondicoesComponent } from './termos-condicoes/termos-condicoes.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RegisterComponent } from './register/register.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { CestoComponent } from './cesto/cesto.component';
import { AdminConsumidorComponent } from './admin-consumidor/admin-consumidor.component';
import { ConsumidorHistoricoComponent } from './consumidor-historico/consumidor-historico.component';
import { DetalhesEncomendaConsumidorComponent } from './detalhes-encomenda-consumidor/detalhes-encomenda-consumidor.component';
import { PerfilConsumidorComponent } from './perfil-consumidor/perfil-consumidor.component';
import { AdminFornecedorComponent } from './admin-fornecedor/admin-fornecedor.component';
import { TransportesComponent } from './transportes/transportes.component';
import { PagamentoComponent } from './pagamento/pagamento.component';
import { WelcomeComponent } from './welcome/welcome.component';


const appRoutes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: HomeComponent },
  { path: 'termos-condicoes', component: TermosCondicoesComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'cesto', component: CestoComponent },
  { path: 'admin-consumidor', component: AdminConsumidorComponent},
  { path: 'admin-fornecedor', component: AdminFornecedorComponent },
  { path: 'consumidor-historico', component: ConsumidorHistoricoComponent},
  { path: 'detalhes-encomenda-consumidor', component: DetalhesEncomendaConsumidorComponent},
  { path: 'perfil-consumidor', component: PerfilConsumidorComponent},
  { path: 'transportes', component: TransportesComponent },  
  { path: 'pagamento', component: PagamentoComponent },
  { path: 'welcome', component: WelcomeComponent }
];

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true } // <-- debugging purposes only
    ),
    NgbModule
  ],
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    TermosCondicoesComponent,
    RegisterComponent,
    ForgotPasswordComponent,
    CestoComponent,
    AdminConsumidorComponent,
    ConsumidorHistoricoComponent,
    DetalhesEncomendaConsumidorComponent,
    PerfilConsumidorComponent,
    AdminFornecedorComponent,
    TransportesComponent,
    PagamentoComponent,
    WelcomeComponent
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }

