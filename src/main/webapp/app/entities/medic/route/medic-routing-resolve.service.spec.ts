jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMedic, Medic } from '../medic.model';
import { MedicService } from '../service/medic.service';

import { MedicRoutingResolveService } from './medic-routing-resolve.service';

describe('Service Tests', () => {
  describe('Medic routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MedicRoutingResolveService;
    let service: MedicService;
    let resultMedic: IMedic | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MedicRoutingResolveService);
      service = TestBed.inject(MedicService);
      resultMedic = undefined;
    });

    describe('resolve', () => {
      it('should return IMedic returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedic = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedic).toEqual({ id: 123 });
      });

      it('should return new IMedic if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedic = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMedic).toEqual(new Medic());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedic = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedic).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
