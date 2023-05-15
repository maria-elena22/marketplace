import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminConsumidorComponent } from './admin-consumidor.component';

describe('AdminConsumidorComponent', () => {
  let component: AdminConsumidorComponent;
  let fixture: ComponentFixture<AdminConsumidorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminConsumidorComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminConsumidorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
