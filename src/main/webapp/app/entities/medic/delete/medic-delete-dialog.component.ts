import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedic } from '../medic.model';
import { MedicService } from '../service/medic.service';

@Component({
  templateUrl: './medic-delete-dialog.component.html',
})
export class MedicDeleteDialogComponent {
  medic?: IMedic;

  constructor(protected medicService: MedicService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.medicService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
