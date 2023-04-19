import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFornecedorComponent } from './admin-fornecedor.component';

describe('AdminFornecedorComponent', () => {
  let component: AdminFornecedorComponent;
  let fixture: ComponentFixture<AdminFornecedorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminFornecedorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFornecedorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
