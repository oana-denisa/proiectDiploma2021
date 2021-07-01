import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GradDetailComponent } from './grad-detail.component';

describe('Component Tests', () => {
  describe('Grad Management Detail Component', () => {
    let comp: GradDetailComponent;
    let fixture: ComponentFixture<GradDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GradDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ grad: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GradDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GradDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load grad on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.grad).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
