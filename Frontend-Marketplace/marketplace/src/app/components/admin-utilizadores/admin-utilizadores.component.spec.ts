import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminUtilizadoresComponent } from './admin-utilizadores.component';

describe('AdminUtilizadoresComponent', () => {
  let component: AdminUtilizadoresComponent;
  let fixture: ComponentFixture<AdminUtilizadoresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminUtilizadoresComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminUtilizadoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
