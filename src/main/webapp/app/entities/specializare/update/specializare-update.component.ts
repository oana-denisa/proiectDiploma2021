import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISpecializare, Specializare } from '../specializare.model';
import { SpecializareService } from '../service/specializare.service';

@Component({
  selector: 'jhi-specializare-update',
  templateUrl: './specializare-update.component.html',
})
export class SpecializareUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    denumire: [null, [Validators.required, Validators.maxLength(80)]],
  });

  constructor(protected specializareService: SpecializareService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specializare }) => {
      this.updateForm(specializare);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const specializare = this.createFromForm();
    if (specializare.id !== undefined) {
      this.subscribeToSaveResponse(this.specializareService.update(specializare));
    } else {
      this.subscribeToSaveResponse(this.specializareService.create(specializare));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpecializare>>): void {
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

  protected updateForm(specializare: ISpecializare): void {
    this.editForm.patchValue({
      id: specializare.id,
      denumire: specializare.denumire,
    });
  }

  protected createFromForm(): ISpecializare {
    return {
      ...new Specializare(),
      id: this.editForm.get(['id'])!.value,
      denumire: this.editForm.get(['denumire'])!.value,
    };
  }
}
