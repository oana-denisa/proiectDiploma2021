import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConcluziiConsultatie, ConcluziiConsultatie } from '../concluzii-consultatie.model';
import { ConcluziiConsultatieService } from '../service/concluzii-consultatie.service';

@Injectable({ providedIn: 'root' })
export class ConcluziiConsultatieRoutingResolveService implements Resolve<IConcluziiConsultatie> {
  constructor(protected service: ConcluziiConsultatieService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConcluziiConsultatie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((concluziiConsultatie: HttpResponse<ConcluziiConsultatie>) => {
          if (concluziiConsultatie.body) {
            return of(concluziiConsultatie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ConcluziiConsultatie());
  }
}
