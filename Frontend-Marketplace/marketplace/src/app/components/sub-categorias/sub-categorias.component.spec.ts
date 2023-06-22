import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubCategoriasComponent } from './sub-categorias.component';

describe('SubCategoriasComponent', () => {
  let component: SubCategoriasComponent;
  let fixture: ComponentFixture<SubCategoriasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubCategoriasComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubCategoriasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
