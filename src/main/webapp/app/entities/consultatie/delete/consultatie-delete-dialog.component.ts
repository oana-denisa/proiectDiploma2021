import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConsultatie } from '../consultatie.model';
import { ConsultatieService } from '../service/consultatie.service';

@Component({
  templateUrl: './consultatie-delete-dialog.component.html',
})
export class ConsultatieDeleteDialogComponent {
  consultatie?: IConsultatie;

  constructor(protected consultatieService: ConsultatieService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.consultatieService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
