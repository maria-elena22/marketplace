import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { MatFormFieldModule } from '@angular/material/form-field'; // <-- import this
import { ReactiveFormsModule } from '@angular/forms'; // import ReactiveFormsModule
import { SelectDropDownModule } from 'ngx-select-dropdown';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { TermosCondicoesComponent } from './components/termos-condicoes/termos-condicoes.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RegisterComponent } from './components/register/register.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { CestoComponent } from './components/cesto/cesto.component';
import { AdminConsumidorComponent } from './components/admin-consumidor/admin-consumidor.component';
import { ConsumidorHistoricoComponent } from './components/consumidor-historico/consumidor-historico.component';
import { DetalhesEncomendaConsumidorComponent } from './components/detalhes-encomenda-consumidor/detalhes-encomenda-consumidor.component';
import { PerfilConsumidorComponent } from './components/perfil-consumidor/perfil-consumidor.component';
import { AdminFornecedorComponent } from './components/admin-fornecedor/admin-fornecedor.component';
import { TransportesComponent } from './components/transportes/transportes.component';
import { PagamentoComponent } from './components/pagamento/pagamento.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { TerminarSessaoComponent } from './components/terminar-sessao/terminar-sessao.component';
import { EliminarContaComponent } from './components/eliminar-conta/eliminar-conta.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CategoriasComponent } from './components/categorias/categorias.component';
import { SubCategoriasComponent } from './components/sub-categorias/sub-categorias.component';
import { ProdutosComponent } from './components/produtos/produtos.component';
import { UniProdsComponent } from './components/uni-prods/uni-prods.component';
import { EncomendasComponent } from './components/encomendas/encomendas.component';
import { RelatoriosComponent } from './components/relatorios/relatorios.component';
import { ViagensComponent } from './components/viagens/viagens.component';
import { NotificacoesComponent } from './components/notificacoes/notificacoes.component';
import { DefinicoesComponent } from './components/definicoes/definicoes.component';
import { AdminUtilizadoresComponent } from './components/admin-utilizadores/admin-utilizadores.component';
import { DetalhesUtilizadorAdminComponent } from './components/detalhes-utilizador-admin/detalhes-utilizador-admin.component';
import { GraficoComponent } from './components/grafico/grafico.component';
import { ProdutoDetalhesComponent } from './components/produto-detalhes/produto-detalhes.component';
import { MatIconModule } from '@angular/material/icon';



const appRoutes: Routes = [
  { path: 'marketplace/login', component: LoginComponent },
  { path: 'marketplace', component: HomeComponent },
  { path: '', component: HomeComponent },
  { path: 'marketplace/termos-condicoes', component: TermosCondicoesComponent },
  { path: 'marketplace/produtos', component: ProdutosComponent },
  { path: 'marketplace/register', component: RegisterComponent },
  { path: 'marketplace/forgot-password', component: ForgotPasswordComponent },
  { path: 'marketplace/cesto', component: CestoComponent },
  { path: 'marketplace/admin-consumidor', component: AdminConsumidorComponent},
  { path: 'marketplace/admin-fornecedor', component: AdminFornecedorComponent },
  { path: 'marketplace/consumidor-historico', component: ConsumidorHistoricoComponent},
  { path: 'marketplace/perfil-consumidor', component: PerfilConsumidorComponent},
  { path: 'marketplace/transportes', component: TransportesComponent },
  { path: 'marketplace/pagamento', component: PagamentoComponent },
  { path: 'marketplace/welcome', component: WelcomeComponent },
  { path: 'marketplace/encomendas', component: EncomendasComponent },
  { path: 'marketplace/uniProds', component: UniProdsComponent },
  { path: 'marketplace/viagens/:id', component: ViagensComponent },
  { path: 'marketplace/relatorios', component: RelatoriosComponent },
  { path: 'marketplace/produto-detalhes', component: ProdutoDetalhesComponent },
  { path: 'marketplace/utilizadores', component: AdminUtilizadoresComponent },
  { path: 'marketplace/definicoes', component: DefinicoesComponent }

];

@NgModule({
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  imports: [
    MatIconModule,
    BrowserModule,
    FormsModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    HttpClientModule,
    SelectDropDownModule
    ,
    RouterModule.forRoot(
      appRoutes,
      { onSameUrlNavigation: 'reload' } //enableTracing: true, 
    ),
    NgbModule,
    BrowserAnimationsModule
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
    WelcomeComponent,
    SidebarComponent,
    TerminarSessaoComponent,
    EliminarContaComponent,
    CategoriasComponent,
    SubCategoriasComponent,
    ProdutosComponent,
    UniProdsComponent,
    EncomendasComponent,
    RelatoriosComponent,
    ViagensComponent,
    NotificacoesComponent,
    ProdutoDetalhesComponent,
    DefinicoesComponent,
    AdminUtilizadoresComponent,
    DetalhesUtilizadorAdminComponent,
    GraficoComponent  ],

  bootstrap: [ AppComponent ],
  exports: [ProdutosComponent],
  providers: [ProdutosComponent
  ]


})
export class AppModule { }

