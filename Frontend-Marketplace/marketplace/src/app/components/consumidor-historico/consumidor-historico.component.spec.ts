import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsumidorHistoricoComponent } from './consumidor-historico.component';

describe('ConsumidorHistoricoComponent', () => {
  let component: ConsumidorHistoricoComponent;
  let fixture: ComponentFixture<ConsumidorHistoricoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConsumidorHistoricoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConsumidorHistoricoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
