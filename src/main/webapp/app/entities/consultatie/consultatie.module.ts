import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ConsultatieComponent } from './list/consultatie.component';
import { ConsultatieDetailComponent } from './detail/consultatie-detail.component';
import { ConsultatieUpdateComponent } from './update/consultatie-update.component';
import { ConsultatieDeleteDialogComponent } from './delete/consultatie-delete-dialog.component';
import { ConsultatieRoutingModule } from './route/consultatie-routing.module';

@NgModule({
  imports: [SharedModule, ConsultatieRoutingModule],
  declarations: [ConsultatieComponent, ConsultatieDetailComponent, ConsultatieUpdateComponent, ConsultatieDeleteDialogComponent],
  entryComponents: [ConsultatieDeleteDialogComponent],
})
export class ConsultatieModule {}
