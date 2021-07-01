import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SpecializareComponent } from './list/specializare.component';
import { SpecializareDetailComponent } from './detail/specializare-detail.component';
import { SpecializareUpdateComponent } from './update/specializare-update.component';
import { SpecializareDeleteDialogComponent } from './delete/specializare-delete-dialog.component';
import { SpecializareRoutingModule } from './route/specializare-routing.module';

@NgModule({
  imports: [SharedModule, SpecializareRoutingModule],
  declarations: [SpecializareComponent, SpecializareDetailComponent, SpecializareUpdateComponent, SpecializareDeleteDialogComponent],
  entryComponents: [SpecializareDeleteDialogComponent],
})
export class SpecializareModule {}
