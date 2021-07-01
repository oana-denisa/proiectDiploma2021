import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MedicComponent } from '../list/medic.component';
import { MedicDetailComponent } from '../detail/medic-detail.component';
import { MedicUpdateComponent } from '../update/medic-update.component';
import { MedicRoutingResolveService } from './medic-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const medicRoute: Routes = [
  {
    path: '',
    component: MedicComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MedicDetailComponent,
    resolve: {
      medic: MedicRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MedicUpdateComponent,
    resolve: {
      medic: MedicRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MedicUpdateComponent,
    resolve: {
      medic: MedicRoutingResolveService,
    },
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(medicRoute)],
  exports: [RouterModule],
})
export class MedicRoutingModule {}
