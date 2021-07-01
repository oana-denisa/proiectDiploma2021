jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGrad, Grad } from '../grad.model';
import { GradService } from '../service/grad.service';

import { GradRoutingResolveService } from './grad-routing-resolve.service';

describe('Service Tests', () => {
  describe('Grad routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: GradRoutingResolveService;
    let service: GradService;
    let resultGrad: IGrad | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(GradRoutingResolveService);
      service = TestBed.inject(GradService);
      resultGrad = undefined;
    });

    describe('resolve', () => {
      it('should return IGrad returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGrad = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGrad).toEqual({ id: 123 });
      });

      it('should return new IGrad if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGrad = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultGrad).toEqual(new Grad());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGrad = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGrad).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
