import { TestBed } from '@angular/core/testing';

import { UniProdsService } from './uni-prods.service';

describe('UniProdsService', () => {
  let service: UniProdsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UniProdsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
