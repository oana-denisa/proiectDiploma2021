import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConcluziiConsultatie } from '../concluzii-consultatie.model';
import { ConcluziiConsultatieService } from '../service/concluzii-consultatie.service';

@Component({
  templateUrl: './concluzii-consultatie-delete-dialog.component.html',
})
export class ConcluziiConsultatieDeleteDialogComponent {
  concluziiConsultatie?: IConcluziiConsultatie;

  constructor(protected concluziiConsultatieService: ConcluziiConsultatieService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.concluziiConsultatieService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
