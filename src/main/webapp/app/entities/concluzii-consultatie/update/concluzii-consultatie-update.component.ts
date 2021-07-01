import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IConcluziiConsultatie, ConcluziiConsultatie } from '../concluzii-consultatie.model';
import { ConcluziiConsultatieService } from '../service/concluzii-consultatie.service';
import { IConsultatie } from 'app/entities/consultatie/consultatie.model';
import { ConsultatieService } from 'app/entities/consultatie/service/consultatie.service';

@Component({
  selector: 'jhi-concluzii-consultatie-update',
  templateUrl: './concluzii-consultatie-update.component.html',
})
export class ConcluziiConsultatieUpdateComponent implements OnInit {
  isSaving = false;

  consultatiesCollection: IConsultatie[] = [];

  editForm = this.fb.group({
    id: [],
    diagnostic: [null, [Validators.required, Validators.maxLength(255)]],
    tratament: [null, [Validators.required, Validators.maxLength(255)]],
    observatii: [null, [Validators.required, Validators.maxLength(255)]],
    controlUrmator: [null, [Validators.required, Validators.maxLength(30)]],
    consultatie: [],
  });

  constructor(
    protected concluziiConsultatieService: ConcluziiConsultatieService,
    protected consultatieService: ConsultatieService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ concluziiConsultatie }) => {
      this.updateForm(concluziiConsultatie);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const concluziiConsultatie = this.createFromForm();
    if (concluziiConsultatie.id !== undefined) {
      this.subscribeToSaveResponse(this.concluziiConsultatieService.update(concluziiConsultatie));
    } else {
      this.subscribeToSaveResponse(this.concluziiConsultatieService.create(concluziiConsultatie));
    }
  }

  trackConsultatieById(index: number, item: IConsultatie): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConcluziiConsultatie>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(concluziiConsultatie: IConcluziiConsultatie): void {
    this.editForm.patchValue({
      id: concluziiConsultatie.id,
      diagnostic: concluziiConsultatie.diagnostic,
      tratament: concluziiConsultatie.tratament,
      observatii: concluziiConsultatie.observatii,
      controlUrmator: concluziiConsultatie.controlUrmator,
      consultatie: concluziiConsultatie.consultatie,
    });

    this.consultatiesCollection = this.consultatieService.addConsultatieToCollectionIfMissing(
      this.consultatiesCollection,
      concluziiConsultatie.consultatie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.consultatieService
      .query({ filter: 'concluziiconsultatie-is-null' })
      .pipe(map((res: HttpResponse<IConsultatie[]>) => res.body ?? []))
      .pipe(
        map((consultaties: IConsultatie[]) =>
          this.consultatieService.addConsultatieToCollectionIfMissing(consultaties, this.editForm.get('consultatie')!.value)
        )
      )
      .subscribe((consultaties: IConsultatie[]) => (this.consultatiesCollection = consultaties));
  }

  protected createFromForm(): IConcluziiConsultatie {
    return {
      ...new ConcluziiConsultatie(),
      id: this.editForm.get(['id'])!.value,
      diagnostic: this.editForm.get(['diagnostic'])!.value,
      tratament: this.editForm.get(['tratament'])!.value,
      observatii: this.editForm.get(['observatii'])!.value,
      controlUrmator: this.editForm.get(['controlUrmator'])!.value,
      consultatie: this.editForm.get(['consultatie'])!.value,
    };
  }
}
