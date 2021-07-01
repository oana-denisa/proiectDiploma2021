import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpecializare } from '../specializare.model';
import { SpecializareService } from '../service/specializare.service';

@Component({
  templateUrl: './specializare-delete-dialog.component.html',
})
export class SpecializareDeleteDialogComponent {
  specializare?: ISpecializare;

  constructor(protected specializareService: SpecializareService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specializareService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
