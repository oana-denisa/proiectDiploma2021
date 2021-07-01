jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MedicService } from '../service/medic.service';
import { IMedic, Medic } from '../medic.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IGrad } from 'app/entities/grad/grad.model';
import { GradService } from 'app/entities/grad/service/grad.service';
import { ISpecializare } from 'app/entities/specializare/specializare.model';
import { SpecializareService } from 'app/entities/specializare/service/specializare.service';

import { MedicUpdateComponent } from './medic-update.component';

describe('Component Tests', () => {
  describe('Medic Management Update Component', () => {
    let comp: MedicUpdateComponent;
    let fixture: ComponentFixture<MedicUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let medicService: MedicService;
    let userService: UserService;
    let gradService: GradService;
    let specializareService: SpecializareService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MedicUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MedicUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MedicUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      medicService = TestBed.inject(MedicService);
      userService = TestBed.inject(UserService);
      gradService = TestBed.inject(GradService);
      specializareService = TestBed.inject(SpecializareService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const medic: IMedic = { id: 456 };
        const user: IUser = { id: 27699 };
        medic.user = user;

        const userCollection: IUser[] = [{ id: 87926 }];
        spyOn(userService, 'query').and.returnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        spyOn(userService, 'addUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ medic });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Grad query and add missing value', () => {
        const medic: IMedic = { id: 456 };
        const grad: IGrad = { id: 62259 };
        medic.grad = grad;

        const gradCollection: IGrad[] = [{ id: 20653 }];
        spyOn(gradService, 'query').and.returnValue(of(new HttpResponse({ body: gradCollection })));
        const additionalGrads = [grad];
        const expectedCollection: IGrad[] = [...additionalGrads, ...gradCollection];
        spyOn(gradService, 'addGradToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ medic });
        comp.ngOnInit();

        expect(gradService.query).toHaveBeenCalled();
        expect(gradService.addGradToCollectionIfMissing).toHaveBeenCalledWith(gradCollection, ...additionalGrads);
        expect(comp.gradsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Specializare query and add missing value', () => {
        const medic: IMedic = { id: 456 };
        const specializare: ISpecializare = { id: 45706 };
        medic.specializare = specializare;

        const specializareCollection: ISpecializare[] = [{ id: 3629 }];
        spyOn(specializareService, 'query').and.returnValue(of(new HttpResponse({ body: specializareCollection })));
        const additionalSpecializares = [specializare];
        const expectedCollection: ISpecializare[] = [...additionalSpecializares, ...specializareCollection];
        spyOn(specializareService, 'addSpecializareToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ medic });
        comp.ngOnInit();

        expect(specializareService.query).toHaveBeenCalled();
        expect(specializareService.addSpecializareToCollectionIfMissing).toHaveBeenCalledWith(
          specializareCollection,
          ...additionalSpecializares
        );
        expect(comp.specializaresSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const medic: IMedic = { id: 456 };
        const user: IUser = { id: 47918 };
        medic.user = user;
        const grad: IGrad = { id: 58850 };
        medic.grad = grad;
        const specializare: ISpecializare = { id: 60484 };
        medic.specializare = specializare;

        activatedRoute.data = of({ medic });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(medic));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.gradsSharedCollection).toContain(grad);
        expect(comp.specializaresSharedCollection).toContain(specializare);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medic = { id: 123 };
        spyOn(medicService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medic });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: medic }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(medicService.update).toHaveBeenCalledWith(medic);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medic = new Medic();
        spyOn(medicService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medic });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: medic }));
        saveSubject.complete();

        // THEN
        expect(medicService.create).toHaveBeenCalledWith(medic);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const medic = { id: 123 };
        spyOn(medicService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ medic });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(medicService.update).toHaveBeenCalledWith(medic);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackGradById', () => {
        it('Should return tracked Grad primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackGradById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackSpecializareById', () => {
        it('Should return tracked Specializare primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackSpecializareById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
