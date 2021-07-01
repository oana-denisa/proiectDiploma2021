import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConcluziiConsultatieDetailComponent } from './concluzii-consultatie-detail.component';

describe('Component Tests', () => {
  describe('ConcluziiConsultatie Management Detail Component', () => {
    let comp: ConcluziiConsultatieDetailComponent;
    let fixture: ComponentFixture<ConcluziiConsultatieDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ConcluziiConsultatieDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ concluziiConsultatie: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ConcluziiConsultatieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConcluziiConsultatieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load concluziiConsultatie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.concluziiConsultatie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
