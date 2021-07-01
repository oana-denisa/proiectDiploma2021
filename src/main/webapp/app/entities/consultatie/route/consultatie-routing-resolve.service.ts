import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConsultatie, Consultatie } from '../consultatie.model';
import { ConsultatieService } from '../service/consultatie.service';

@Injectable({ providedIn: 'root' })
export class ConsultatieRoutingResolveService implements Resolve<IConsultatie> {
  constructor(protected service: ConsultatieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConsultatie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((consultatie: HttpResponse<Consultatie>) => {
          if (consultatie.body) {
            return of(consultatie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Consultatie());
  }
}
