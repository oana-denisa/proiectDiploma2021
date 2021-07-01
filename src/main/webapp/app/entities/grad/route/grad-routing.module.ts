import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GradComponent } from '../list/grad.component';
import { GradDetailComponent } from '../detail/grad-detail.component';
import { GradUpdateComponent } from '../update/grad-update.component';
import { GradRoutingResolveService } from './grad-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const gradRoute: Routes = [
  {
    path: '',
    component: GradComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GradDetailComponent,
    resolve: {
      grad: GradRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GradUpdateComponent,
    resolve: {
      grad: GradRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GradUpdateComponent,
    resolve: {
      grad: GradRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(gradRoute)],
  exports: [RouterModule],
})
export class GradRoutingModule {}
