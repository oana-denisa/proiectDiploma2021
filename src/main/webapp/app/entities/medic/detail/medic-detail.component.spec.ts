import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MedicDetailComponent } from './medic-detail.component';

describe('Component Tests', () => {
  describe('Medic Management Detail Component', () => {
    let comp: MedicDetailComponent;
    let fixture: ComponentFixture<MedicDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MedicDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ medic: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MedicDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MedicDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load medic on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.medic).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
