import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedic, Medic } from '../medic.model';
import { MedicService } from '../service/medic.service';

@Injectable({ providedIn: 'root' })
export class MedicRoutingResolveService implements Resolve<IMedic> {
  constructor(protected service: MedicService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMedic> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((medic: HttpResponse<Medic>) => {
          if (medic.body) {
            return of(medic.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Medic());
  }
}
