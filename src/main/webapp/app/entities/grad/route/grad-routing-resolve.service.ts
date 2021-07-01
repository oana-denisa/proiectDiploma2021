import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGrad, Grad } from '../grad.model';
import { GradService } from '../service/grad.service';

@Injectable({ providedIn: 'root' })
export class GradRoutingResolveService implements Resolve<IGrad> {
  constructor(protected service: GradService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGrad> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((grad: HttpResponse<Grad>) => {
          if (grad.body) {
            return of(grad.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Grad());
  }
}
