import { TestBed } from '@angular/core/testing';

import { SsrStorage } from './ssr-storage';

describe('SsrStorage', () => {
  let service: SsrStorage;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SsrStorage);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
