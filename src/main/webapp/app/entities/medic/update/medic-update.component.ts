import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMedic, Medic } from '../medic.model';
import { MedicService } from '../service/medic.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IGrad } from 'app/entities/grad/grad.model';
import { GradService } from 'app/entities/grad/service/grad.service';
import { ISpecializare } from 'app/entities/specializare/specializare.model';
import { SpecializareService } from 'app/entities/specializare/service/specializare.service';

@Component({
  selector: 'jhi-medic-update',
  templateUrl: './medic-update.component.html',
})
export class MedicUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  gradsSharedCollection: IGrad[] = [];
  specializaresSharedCollection: ISpecializare[] = [];

  editForm = this.fb.group({
    id: [],
    numeSiPrenume: [null, [Validators.required, Validators.maxLength(100)]],
    user: [],
    grad: [],
    specializare: [],
  });

  constructor(
    protected medicService: MedicService,
    protected userService: UserService,
    protected gradService: GradService,
    protected specializareService: SpecializareService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medic }) => {
      this.updateForm(medic);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medic = this.createFromForm();
    if (medic.id !== undefined) {
      this.subscribeToSaveResponse(this.medicService.update(medic));
    } else {
      this.subscribeToSaveResponse(this.medicService.create(medic));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackGradById(index: number, item: IGrad): number {
    return item.id!;
  }

  trackSpecializareById(index: number, item: ISpecializare): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedic>>): void {
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

  protected updateForm(medic: IMedic): void {
    this.editForm.patchValue({
      id: medic.id,
      numeSiPrenume: medic.numeSiPrenume,
      user: medic.user,
      grad: medic.grad,
      specializare: medic.specializare,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, medic.user);
    this.gradsSharedCollection = this.gradService.addGradToCollectionIfMissing(this.gradsSharedCollection, medic.grad);
    this.specializaresSharedCollection = this.specializareService.addSpecializareToCollectionIfMissing(
      this.specializaresSharedCollection,
      medic.specializare
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.gradService
      .query()
      .pipe(map((res: HttpResponse<IGrad[]>) => res.body ?? []))
      .pipe(map((grads: IGrad[]) => this.gradService.addGradToCollectionIfMissing(grads, this.editForm.get('grad')!.value)))
      .subscribe((grads: IGrad[]) => (this.gradsSharedCollection = grads));

    this.specializareService
      .query()
      .pipe(map((res: HttpResponse<ISpecializare[]>) => res.body ?? []))
      .pipe(
        map((specializares: ISpecializare[]) =>
          this.specializareService.addSpecializareToCollectionIfMissing(specializares, this.editForm.get('specializare')!.value)
        )
      )
      .subscribe((specializares: ISpecializare[]) => (this.specializaresSharedCollection = specializares));
  }

  protected createFromForm(): IMedic {
    return {
      ...new Medic(),
      id: this.editForm.get(['id'])!.value,
      numeSiPrenume: this.editForm.get(['numeSiPrenume'])!.value,
      user: this.editForm.get(['user'])!.value,
      grad: this.editForm.get(['grad'])!.value,
      specializare: this.editForm.get(['specializare'])!.value,
    };
  }
}
