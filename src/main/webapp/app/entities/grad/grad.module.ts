import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { GradComponent } from './list/grad.component';
import { GradDetailComponent } from './detail/grad-detail.component';
import { GradUpdateComponent } from './update/grad-update.component';
import { GradDeleteDialogComponent } from './delete/grad-delete-dialog.component';
import { GradRoutingModule } from './route/grad-routing.module';

@NgModule({
  imports: [SharedModule, GradRoutingModule],
  declarations: [GradComponent, GradDetailComponent, GradUpdateComponent, GradDeleteDialogComponent],
  entryComponents: [GradDeleteDialogComponent],
})
export class GradModule {}
