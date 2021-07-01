import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpecializare, Specializare } from '../specializare.model';
import { SpecializareService } from '../service/specializare.service';

@Injectable({ providedIn: 'root' })
export class SpecializareRoutingResolveService implements Resolve<ISpecializare> {
  constructor(protected service: SpecializareService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpecializare> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((specializare: HttpResponse<Specializare>) => {
          if (specializare.body) {
            return of(specializare.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Specializare());
  }
}
