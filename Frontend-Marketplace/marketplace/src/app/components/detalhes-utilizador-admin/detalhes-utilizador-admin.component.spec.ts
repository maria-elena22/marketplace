import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalhesUtilizadorAdminComponent } from './detalhes-utilizador-admin.component';

describe('DetalhesUtilizadorAdminComponent', () => {
  let component: DetalhesUtilizadorAdminComponent;
  let fixture: ComponentFixture<DetalhesUtilizadorAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetalhesUtilizadorAdminComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalhesUtilizadorAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
