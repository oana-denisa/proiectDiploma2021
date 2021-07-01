import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ConcluziiConsultatieComponent } from './list/concluzii-consultatie.component';
import { ConcluziiConsultatieDetailComponent } from './detail/concluzii-consultatie-detail.component';
import { ConcluziiConsultatieUpdateComponent } from './update/concluzii-consultatie-update.component';
import { ConcluziiConsultatieDeleteDialogComponent } from './delete/concluzii-consultatie-delete-dialog.component';
import { ConcluziiConsultatieRoutingModule } from './route/concluzii-consultatie-routing.module';

@NgModule({
  imports: [SharedModule, ConcluziiConsultatieRoutingModule],
  declarations: [
    ConcluziiConsultatieComponent,
    ConcluziiConsultatieDetailComponent,
    ConcluziiConsultatieUpdateComponent,
    ConcluziiConsultatieDeleteDialogComponent,
  ],
  entryComponents: [ConcluziiConsultatieDeleteDialogComponent],
})
export class ConcluziiConsultatieModule {}
