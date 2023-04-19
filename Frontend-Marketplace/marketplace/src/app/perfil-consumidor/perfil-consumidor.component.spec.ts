import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerfilConsumidorComponent } from './perfil-consumidor.component';

describe('PerfilConsumidorComponent', () => {
  let component: PerfilConsumidorComponent;
  let fixture: ComponentFixture<PerfilConsumidorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PerfilConsumidorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PerfilConsumidorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
