import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISpecializare, Specializare } from '../specializare.model';

import { SpecializareService } from './specializare.service';

describe('Service Tests', () => {
  describe('Specializare Service', () => {
    let service: SpecializareService;
    let httpMock: HttpTestingController;
    let elemDefault: ISpecializare;
    let expectedResult: ISpecializare | ISpecializare[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SpecializareService);
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

      it('should create a Specializare', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Specializare()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Specializare', () => {
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

      it('should partial update a Specializare', () => {
        const patchObject = Object.assign(
          {
            denumire: 'BBBBBB',
          },
          new Specializare()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Specializare', () => {
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

      it('should delete a Specializare', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSpecializareToCollectionIfMissing', () => {
        it('should add a Specializare to an empty array', () => {
          const specializare: ISpecializare = { id: 123 };
          expectedResult = service.addSpecializareToCollectionIfMissing([], specializare);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(specializare);
        });

        it('should not add a Specializare to an array that contains it', () => {
          const specializare: ISpecializare = { id: 123 };
          const specializareCollection: ISpecializare[] = [
            {
              ...specializare,
            },
            { id: 456 },
          ];
          expectedResult = service.addSpecializareToCollectionIfMissing(specializareCollection, specializare);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Specializare to an array that doesn't contain it", () => {
          const specializare: ISpecializare = { id: 123 };
          const specializareCollection: ISpecializare[] = [{ id: 456 }];
          expectedResult = service.addSpecializareToCollectionIfMissing(specializareCollection, specializare);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(specializare);
        });

        it('should add only unique Specializare to an array', () => {
          const specializareArray: ISpecializare[] = [{ id: 123 }, { id: 456 }, { id: 62177 }];
          const specializareCollection: ISpecializare[] = [{ id: 123 }];
          expectedResult = service.addSpecializareToCollectionIfMissing(specializareCollection, ...specializareArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const specializare: ISpecializare = { id: 123 };
          const specializare2: ISpecializare = { id: 456 };
          expectedResult = service.addSpecializareToCollectionIfMissing([], specializare, specializare2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(specializare);
          expect(expectedResult).toContain(specializare2);
        });

        it('should accept null and undefined values', () => {
          const specializare: ISpecializare = { id: 123 };
          expectedResult = service.addSpecializareToCollectionIfMissing([], null, specializare, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(specializare);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
