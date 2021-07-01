import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConsultatieComponent } from '../list/consultatie.component';
import { ConsultatieDetailComponent } from '../detail/consultatie-detail.component';
import { ConsultatieUpdateComponent } from '../update/consultatie-update.component';
import { ConsultatieRoutingResolveService } from './consultatie-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const consultatieRoute: Routes = [
  {
    path: '',
    component: ConsultatieComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConsultatieDetailComponent,
    resolve: {
      consultatie: ConsultatieRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConsultatieUpdateComponent,
    resolve: {
      consultatie: ConsultatieRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConsultatieUpdateComponent,
    resolve: {
      consultatie: ConsultatieRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(consultatieRoute)],
  exports: [RouterModule],
})
export class ConsultatieRoutingModule {}
