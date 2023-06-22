import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UniProdsComponent } from './uni-prods.component';

describe('UniProdsComponent', () => {
  let component: UniProdsComponent;
  let fixture: ComponentFixture<UniProdsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UniProdsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UniProdsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
