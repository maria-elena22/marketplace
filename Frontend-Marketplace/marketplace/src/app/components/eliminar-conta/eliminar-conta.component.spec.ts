import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EliminarContaComponent } from './eliminar-conta.component';

describe('EliminarContaComponent', () => {
  let component: EliminarContaComponent;
  let fixture: ComponentFixture<EliminarContaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EliminarContaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EliminarContaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
