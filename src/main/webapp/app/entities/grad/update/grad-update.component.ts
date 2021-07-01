import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IGrad, Grad } from '../grad.model';
import { GradService } from '../service/grad.service';

@Component({
  selector: 'jhi-grad-update',
  templateUrl: './grad-update.component.html',
})
export class GradUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    denumire: [null, [Validators.required, Validators.maxLength(30)]],
  });

  constructor(protected gradService: GradService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ grad }) => {
      this.updateForm(grad);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const grad = this.createFromForm();
    if (grad.id !== undefined) {
      this.subscribeToSaveResponse(this.gradService.update(grad));
    } else {
      this.subscribeToSaveResponse(this.gradService.create(grad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGrad>>): void {
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

  protected updateForm(grad: IGrad): void {
    this.editForm.patchValue({
      id: grad.id,
      denumire: grad.denumire,
    });
  }

  protected createFromForm(): IGrad {
    return {
      ...new Grad(),
      id: this.editForm.get(['id'])!.value,
      denumire: this.editForm.get(['denumire'])!.value,
    };
  }
}
