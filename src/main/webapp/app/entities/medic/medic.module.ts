import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MedicComponent } from './list/medic.component';
import { MedicDetailComponent } from './detail/medic-detail.component';
import { MedicUpdateComponent } from './update/medic-update.component';
import { MedicDeleteDialogComponent } from './delete/medic-delete-dialog.component';
import { MedicRoutingModule } from './route/medic-routing.module';

@NgModule({
  imports: [SharedModule, MedicRoutingModule],
  declarations: [MedicComponent, MedicDetailComponent, MedicUpdateComponent, MedicDeleteDialogComponent],
  entryComponents: [MedicDeleteDialogComponent],
})
export class MedicModule {}
