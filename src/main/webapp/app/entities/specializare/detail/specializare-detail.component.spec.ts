import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpecializareDetailComponent } from './specializare-detail.component';

describe('Component Tests', () => {
  describe('Specializare Management Detail Component', () => {
    let comp: SpecializareDetailComponent;
    let fixture: ComponentFixture<SpecializareDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SpecializareDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ specializare: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SpecializareDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SpecializareDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load specializare on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.specializare).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
