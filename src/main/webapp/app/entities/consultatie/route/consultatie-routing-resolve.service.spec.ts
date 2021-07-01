jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IConsultatie, Consultatie } from '../consultatie.model';
import { ConsultatieService } from '../service/consultatie.service';

import { ConsultatieRoutingResolveService } from './consultatie-routing-resolve.service';

describe('Service Tests', () => {
  describe('Consultatie routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ConsultatieRoutingResolveService;
    let service: ConsultatieService;
    let resultConsultatie: IConsultatie | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ConsultatieRoutingResolveService);
      service = TestBed.inject(ConsultatieService);
      resultConsultatie = undefined;
    });

    describe('resolve', () => {
      it('should return IConsultatie returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConsultatie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConsultatie).toEqual({ id: 123 });
      });

      it('should return new IConsultatie if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConsultatie = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultConsultatie).toEqual(new Consultatie());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConsultatie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConsultatie).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
