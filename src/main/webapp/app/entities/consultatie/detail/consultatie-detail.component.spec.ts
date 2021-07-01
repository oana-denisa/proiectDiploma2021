import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConsultatieDetailComponent } from './consultatie-detail.component';

describe('Component Tests', () => {
  describe('Consultatie Management Detail Component', () => {
    let comp: ConsultatieDetailComponent;
    let fixture: ComponentFixture<ConsultatieDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ConsultatieDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ consultatie: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ConsultatieDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConsultatieDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load consultatie on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.consultatie).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
