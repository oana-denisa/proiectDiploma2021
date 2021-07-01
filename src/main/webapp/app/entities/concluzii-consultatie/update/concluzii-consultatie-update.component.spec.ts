jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ConcluziiConsultatieService } from '../service/concluzii-consultatie.service';
import { IConcluziiConsultatie, ConcluziiConsultatie } from '../concluzii-consultatie.model';
import { IConsultatie } from 'app/entities/consultatie/consultatie.model';
import { ConsultatieService } from 'app/entities/consultatie/service/consultatie.service';

import { ConcluziiConsultatieUpdateComponent } from './concluzii-consultatie-update.component';

describe('Component Tests', () => {
  describe('ConcluziiConsultatie Management Update Component', () => {
    let comp: ConcluziiConsultatieUpdateComponent;
    let fixture: ComponentFixture<ConcluziiConsultatieUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let concluziiConsultatieService: ConcluziiConsultatieService;
    let consultatieService: ConsultatieService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ConcluziiConsultatieUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ConcluziiConsultatieUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConcluziiConsultatieUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      concluziiConsultatieService = TestBed.inject(ConcluziiConsultatieService);
      consultatieService = TestBed.inject(ConsultatieService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call consultatie query and add missing value', () => {
        const concluziiConsultatie: IConcluziiConsultatie = { id: 456 };
        const consultatie: IConsultatie = { id: 35033 };
        concluziiConsultatie.consultatie = consultatie;

        const consultatieCollection: IConsultatie[] = [{ id: 18384 }];
        spyOn(consultatieService, 'query').and.returnValue(of(new HttpResponse({ body: consultatieCollection })));
        const expectedCollection: IConsultatie[] = [consultatie, ...consultatieCollection];
        spyOn(consultatieService, 'addConsultatieToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ concluziiConsultatie });
        comp.ngOnInit();

        expect(consultatieService.query).toHaveBeenCalled();
        expect(consultatieService.addConsultatieToCollectionIfMissing).toHaveBeenCalledWith(consultatieCollection, consultatie);
        expect(comp.consultatiesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const concluziiConsultatie: IConcluziiConsultatie = { id: 456 };
        const consultatie: IConsultatie = { id: 47897 };
        concluziiConsultatie.consultatie = consultatie;

        activatedRoute.data = of({ concluziiConsultatie });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(concluziiConsultatie));
        expect(comp.consultatiesCollection).toContain(consultatie);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const concluziiConsultatie = { id: 123 };
        spyOn(concluziiConsultatieService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ concluziiConsultatie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: concluziiConsultatie }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(concluziiConsultatieService.update).toHaveBeenCalledWith(concluziiConsultatie);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const concluziiConsultatie = new ConcluziiConsultatie();
        spyOn(concluziiConsultatieService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ concluziiConsultatie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: concluziiConsultatie }));
        saveSubject.complete();

        // THEN
        expect(concluziiConsultatieService.create).toHaveBeenCalledWith(concluziiConsultatie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const concluziiConsultatie = { id: 123 };
        spyOn(concluziiConsultatieService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ concluziiConsultatie });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(concluziiConsultatieService.update).toHaveBeenCalledWith(concluziiConsultatie);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackConsultatieById', () => {
        it('Should return tracked Consultatie primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackConsultatieById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
