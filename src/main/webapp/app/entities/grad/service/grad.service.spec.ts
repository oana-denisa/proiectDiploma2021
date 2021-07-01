import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGrad, Grad } from '../grad.model';

import { GradService } from './grad.service';

describe('Service Tests', () => {
  describe('Grad Service', () => {
    let service: GradService;
    let httpMock: HttpTestingController;
    let elemDefault: IGrad;
    let expectedResult: IGrad | IGrad[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GradService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        denumire: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Grad', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Grad()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Grad', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            denumire: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Grad', () => {
        const patchObject = Object.assign(
          {
            denumire: 'BBBBBB',
          },
          new Grad()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Grad', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            denumire: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Grad', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGradToCollectionIfMissing', () => {
        it('should add a Grad to an empty array', () => {
          const grad: IGrad = { id: 123 };
          expectedResult = service.addGradToCollectionIfMissing([], grad);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(grad);
        });

        it('should not add a Grad to an array that contains it', () => {
          const grad: IGrad = { id: 123 };
          const gradCollection: IGrad[] = [
            {
              ...grad,
            },
            { id: 456 },
          ];
          expectedResult = service.addGradToCollectionIfMissing(gradCollection, grad);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Grad to an array that doesn't contain it", () => {
          const grad: IGrad = { id: 123 };
          const gradCollection: IGrad[] = [{ id: 456 }];
          expectedResult = service.addGradToCollectionIfMissing(gradCollection, grad);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(grad);
        });

        it('should add only unique Grad to an array', () => {
          const gradArray: IGrad[] = [{ id: 123 }, { id: 456 }, { id: 68384 }];
          const gradCollection: IGrad[] = [{ id: 123 }];
          expectedResult = service.addGradToCollectionIfMissing(gradCollection, ...gradArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const grad: IGrad = { id: 123 };
          const grad2: IGrad = { id: 456 };
          expectedResult = service.addGradToCollectionIfMissing([], grad, grad2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(grad);
          expect(expectedResult).toContain(grad2);
        });

        it('should accept null and undefined values', () => {
          const grad: IGrad = { id: 123 };
          expectedResult = service.addGradToCollectionIfMissing([], null, grad, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(grad);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
