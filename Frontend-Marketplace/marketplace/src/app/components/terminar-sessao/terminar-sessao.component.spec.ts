import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TerminarSessaoComponent } from './terminar-sessao.component';

describe('TerminarSessaoComponent', () => {
  let component: TerminarSessaoComponent;
  let fixture: ComponentFixture<TerminarSessaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TerminarSessaoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TerminarSessaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
