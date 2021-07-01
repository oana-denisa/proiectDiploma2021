import { Component, OnInit, DoCheck } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IConsultatie, Consultatie } from '../consultatie.model';
import { ConsultatieService } from '../service/consultatie.service';
import { IMedic } from 'app/entities/medic/medic.model';
import { MedicService } from 'app/entities/medic/service/medic.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-consultatie-update',
  templateUrl: './consultatie-update.component.html',
})
export class ConsultatieUpdateComponent implements OnInit, DoCheck {
  isSaving = false;
  //
  medicSelectat: IMedic = {};
  listaConsultatii: IConsultatie[] = [];
  consultatiiMedicSelectat: IConsultatie[] = [];
  clientSelectat: IClient = {};

  medicsSharedCollection: IMedic[] = [];
  clientsSharedCollection: IClient[] = [];
  startDate: dayjs.Dayjs | undefined;
  startDateString: any;
  stopDate: dayjs.Dayjs | undefined;
  stopDateString: any;

  intervalOrarValidMedic = true;
  intervalOrarValidClient = true;

  //consultatieCreata: IConsultatie = {};

  editForm = this.fb.group({
    id: [],
    dataOra: [null, [Validators.required]],
    descriere: [null, [Validators.required, Validators.maxLength(30)]],
    medic: [],
    client: [],
  });

  constructor(
    protected consultatieService: ConsultatieService,
    protected medicService: MedicService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consultatie }) => {
      if (consultatie.id === undefined) {
        const today = dayjs().startOf('day');
        consultatie.dataOra = today;
        //
        this.consultatieService.getListaConsultatii().subscribe(listaConsultatii => (this.listaConsultatii = listaConsultatii));
        this.startDate = dayjs().startOf('minute');
        this.startDateString = this.startDate.format(DATE_TIME_FORMAT);
        this.stopDate = dayjs().startOf('minute').add(30, 'days');
        this.stopDateString = this.stopDate.format(DATE_TIME_FORMAT);
      }

      this.updateForm(consultatie);

      this.loadRelationshipsOptions();
    });
  }

  //
  ngDoCheck(): void {
    // actualizare consultatie:
    const consultatieCreata = this.createFromForm();

    if (consultatieCreata.medic) {
      this.medicSelectat = consultatieCreata.medic;
      for (const consultatie of this.listaConsultatii) {
        if (consultatie.medic!.id === this.medicSelectat.id) {
          const dataOraFinal = dayjs(consultatie.dataOra).startOf('minutes').add(30, 'minutes');
          const dataOraInainte = dayjs(consultatie.dataOra).startOf('minutes').add(-30, 'minutes');

          if (consultatieCreata.dataOra!.isBefore(dataOraFinal) && consultatieCreata.dataOra!.isAfter(dataOraInainte)) {
            this.intervalOrarValidMedic = false;
            break;
          } else {
            this.intervalOrarValidMedic = true;
          }
        } else {
          this.intervalOrarValidMedic = true;
        }
      }
    }

    if (consultatieCreata.client) {
      this.clientSelectat = consultatieCreata.client;
      for (const consultatie of this.listaConsultatii) {
        if (consultatie.client!.id === this.clientSelectat.id) {
          const dataOraFinal = dayjs(consultatie.dataOra).startOf('minutes').add(30, 'minutes');
          const dataOraInainte = dayjs(consultatie.dataOra).add(-30, 'minutes');

          if (consultatieCreata.dataOra!.isBefore(dataOraFinal) && consultatieCreata.dataOra!.isAfter(dataOraInainte)) {
            this.intervalOrarValidClient = false;
            break;
          } else {
            this.intervalOrarValidClient = true;
          }
        } else {
          this.intervalOrarValidClient = true;
        }
      }
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consultatie = this.createFromForm();
    if (consultatie.id !== undefined) {
      this.subscribeToSaveResponse(this.consultatieService.update(consultatie));
    } else {
      this.subscribeToSaveResponse(this.consultatieService.create(consultatie));
    }
  }

  trackMedicById(index: number, item: IMedic): number {
    return item.id!;
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsultatie>>): void {
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

  protected updateForm(consultatie: IConsultatie): void {
    this.editForm.patchValue({
      id: consultatie.id,
      dataOra: consultatie.dataOra ? consultatie.dataOra.format(DATE_TIME_FORMAT) : null,
      descriere: consultatie.descriere,
      medic: consultatie.medic,
      client: consultatie.client,
    });

    this.medicsSharedCollection = this.medicService.addMedicToCollectionIfMissing(this.medicsSharedCollection, consultatie.medic);
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, consultatie.client);
  }

  protected loadRelationshipsOptions(): void {
    this.medicService
      .query()
      .pipe(map((res: HttpResponse<IMedic[]>) => res.body ?? []))
      .pipe(map((medics: IMedic[]) => this.medicService.addMedicToCollectionIfMissing(medics, this.editForm.get('medic')!.value)))
      .subscribe((medics: IMedic[]) => (this.medicsSharedCollection = medics));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): IConsultatie {
    return {
      ...new Consultatie(),
      id: this.editForm.get(['id'])!.value,
      dataOra: this.editForm.get(['dataOra'])!.value ? dayjs(this.editForm.get(['dataOra'])!.value, DATE_TIME_FORMAT) : undefined,
      descriere: this.editForm.get(['descriere'])!.value,
      medic: this.editForm.get(['medic'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
