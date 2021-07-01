jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ConcluziiConsultatieService } from '../service/concluzii-consultatie.service';

import { ConcluziiConsultatieDeleteDialogComponent } from './concluzii-consultatie-delete-dialog.component';

describe('Component Tests', () => {
  describe('ConcluziiConsultatie Management Delete Component', () => {
    let comp: ConcluziiConsultatieDeleteDialogComponent;
    let fixture: ComponentFixture<ConcluziiConsultatieDeleteDialogComponent>;
    let service: ConcluziiConsultatieService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ConcluziiConsultatieDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ConcluziiConsultatieDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConcluziiConsultatieDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ConcluziiConsultatieService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
