jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ConsultatieService } from '../service/consultatie.service';
import { IConsultatie, Consultatie } from '../consultatie.model';
import { IMedic } from 'app/entities/medic/medic.model';
import { MedicService } from 'app/entities/medic/service/medic.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { ConsultatieUpdateComponent } from './consultatie-update.component';

describe('Component Tests', () => {
  describe('Consultatie Management Update Component', () => {
    let comp: ConsultatieUpdateComponent;
    let fixture: ComponentFixture<ConsultatieUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let consultatieService: ConsultatieService;
    let medicService: MedicService;
    let clientService: ClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ConsultatieUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ConsultatieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConsultatieUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      consultatieService = TestBed.inject(ConsultatieService);
      medicService = TestBed.inject(MedicService);
      clientService = TestBed.inject(ClientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Medic query and add missing value', () => {
        const consultatie: IConsultatie = { id: 456 };
        const medic: IMedic = { id: 59887 };
        consultatie.medic = medic;

        const medicCollection: IMedic[] = [{ id: 21445 }];
        spyOn(medicService, 'query').and.returnValue(of(new HttpResponse({ body: medicCollection })));
        const additionalMedics = [medic];
        const expectedCollection: IMedic[] = [...additionalMedics, ...medicCollection];
        spyOn(medicService, 'addMedicToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ consultatie });
        comp.ngOnInit();

        expect(medicService.query).toHaveBeenCalled();
        expect(medicService.addMedicToCollectionIfMissing).toHaveBeenCalledWith(medicCollection, ...additionalMedics);
        expect(comp.medicsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Client query and add missing value', () => {
        const consultatie: IConsultatie = { id: 456 };
        const client: IClient = { id: 52291 };
        consultatie.client = client;

        const clientCollection: IClient[] = [{ id: 47270 }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ consultatie });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const consultatie: IConsultatie = { id: 456 };
        const medic: IMedic = { id: 9176 };
        consultatie.medic = medic;
        const client: IClient = { id: 76676 };
        consultatie.client = client;

        activatedRoute.data = of({ consultatie });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(consultatie));
        expect(comp.medicsSharedCollection).toContain(medic);
        expect(comp.clientsSharedCollection).toContain(client);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const consultatie = { id: 123 };
        spyOn(consultatieService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ consultatie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: consultatie }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(consultatieService.update).toHaveBeenCalledWith(consultatie);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const consultatie = new Consultatie();
        spyOn(consultatieService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ consultatie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: consultatie }));
        saveSubject.complete();

        // THEN
        expect(consultatieService.create).toHaveBeenCalledWith(consultatie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const consultatie = { id: 123 };
        spyOn(consultatieService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ consultatie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(consultatieService.update).toHaveBeenCalledWith(consultatie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMedicById', () => {
        it('Should return tracked Medic primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMedicById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
