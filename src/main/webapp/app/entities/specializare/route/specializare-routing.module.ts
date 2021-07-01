import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SpecializareComponent } from '../list/specializare.component';
import { SpecializareDetailComponent } from '../detail/specializare-detail.component';
import { SpecializareUpdateComponent } from '../update/specializare-update.component';
import { SpecializareRoutingResolveService } from './specializare-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const specializareRoute: Routes = [
  {
    path: '',
    component: SpecializareComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpecializareDetailComponent,
    resolve: {
      specializare: SpecializareRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpecializareUpdateComponent,
    resolve: {
      specializare: SpecializareRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpecializareUpdateComponent,
    resolve: {
      specializare: SpecializareRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(specializareRoute)],
  exports: [RouterModule],
})
export class SpecializareRoutingModule {}
