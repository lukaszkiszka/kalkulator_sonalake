import { TestBed, inject } from '@angular/core/testing';

import { CalculateNetProfitService } from './calculate-net-profit.service';

describe('CalculateNetProfitService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CalculateNetProfitService]
    });
  });

  it('should be created', inject([CalculateNetProfitService], (service: CalculateNetProfitService) => {
    expect(service).toBeTruthy();
  }));
});
