import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalhesEncomendaConsumidorComponent } from './detalhes-encomenda-consumidor.component';

describe('DetalhesEncomendaConsumidorComponent', () => {
  let component: DetalhesEncomendaConsumidorComponent;
  let fixture: ComponentFixture<DetalhesEncomendaConsumidorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetalhesEncomendaConsumidorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalhesEncomendaConsumidorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
