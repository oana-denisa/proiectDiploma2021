import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGrad } from '../grad.model';
import { GradService } from '../service/grad.service';

@Component({
  templateUrl: './grad-delete-dialog.component.html',
})
export class GradDeleteDialogComponent {
  grad?: IGrad;

  constructor(protected gradService: GradService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gradService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
