import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConcluziiConsultatieComponent } from '../list/concluzii-consultatie.component';
import { ConcluziiConsultatieDetailComponent } from '../detail/concluzii-consultatie-detail.component';
import { ConcluziiConsultatieUpdateComponent } from '../update/concluzii-consultatie-update.component';
import { ConcluziiConsultatieRoutingResolveService } from './concluzii-consultatie-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const concluziiConsultatieRoute: Routes = [
  {
    path: '',
    component: ConcluziiConsultatieComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConcluziiConsultatieDetailComponent,
    resolve: {
      concluziiConsultatie: ConcluziiConsultatieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConcluziiConsultatieUpdateComponent,
    resolve: {
      concluziiConsultatie: ConcluziiConsultatieRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConcluziiConsultatieUpdateComponent,
    resolve: {
      concluziiConsultatie: ConcluziiConsultatieRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(concluziiConsultatieRoute)],
  exports: [RouterModule],
})
export class ConcluziiConsultatieRoutingModule {}
