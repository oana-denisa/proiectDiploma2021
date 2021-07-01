jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SpecializareService } from '../service/specializare.service';
import { ISpecializare, Specializare } from '../specializare.model';

import { SpecializareUpdateComponent } from './specializare-update.component';

describe('Component Tests', () => {
  describe('Specializare Management Update Component', () => {
    let comp: SpecializareUpdateComponent;
    let fixture: ComponentFixture<SpecializareUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let specializareService: SpecializareService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SpecializareUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SpecializareUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecializareUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      specializareService = TestBed.inject(SpecializareService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const specializare: ISpecializare = { id: 456 };

        activatedRoute.data = of({ specializare });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(specializare));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const specializare = { id: 123 };
        spyOn(specializareService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ specializare });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: specializare }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(specializareService.update).toHaveBeenCalledWith(specializare);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const specializare = new Specializare();
        spyOn(specializareService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ specializare });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: specializare }));
        saveSubject.complete();

        // THEN
        expect(specializareService.create).toHaveBeenCalledWith(specializare);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const specializare = { id: 123 };
        spyOn(specializareService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ specializare });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(specializareService.update).toHaveBeenCalledWith(specializare);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
