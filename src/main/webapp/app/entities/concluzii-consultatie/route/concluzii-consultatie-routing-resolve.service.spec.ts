jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IConcluziiConsultatie, ConcluziiConsultatie } from '../concluzii-consultatie.model';
import { ConcluziiConsultatieService } from '../service/concluzii-consultatie.service';

import { ConcluziiConsultatieRoutingResolveService } from './concluzii-consultatie-routing-resolve.service';

describe('Service Tests', () => {
  describe('ConcluziiConsultatie routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ConcluziiConsultatieRoutingResolveService;
    let service: ConcluziiConsultatieService;
    let resultConcluziiConsultatie: IConcluziiConsultatie | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ConcluziiConsultatieRoutingResolveService);
      service = TestBed.inject(ConcluziiConsultatieService);
      resultConcluziiConsultatie = undefined;
    });

    describe('resolve', () => {
      it('should return IConcluziiConsultatie returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConcluziiConsultatie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConcluziiConsultatie).toEqual({ id: 123 });
      });

      it('should return new IConcluziiConsultatie if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConcluziiConsultatie = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultConcluziiConsultatie).toEqual(new ConcluziiConsultatie());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConcluziiConsultatie = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConcluziiConsultatie).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
