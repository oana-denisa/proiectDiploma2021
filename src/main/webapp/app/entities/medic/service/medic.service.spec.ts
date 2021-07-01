import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMedic, Medic } from '../medic.model';

import { MedicService } from './medic.service';

describe('Service Tests', () => {
  describe('Medic Service', () => {
    let service: MedicService;
    let httpMock: HttpTestingController;
    let elemDefault: IMedic;
    let expectedResult: IMedic | IMedic[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MedicService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        numeSiPrenume: 'AAAAAAA',
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

      it('should create a Medic', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Medic()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Medic', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            numeSiPrenume: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Medic', () => {
        const patchObject = Object.assign(
          {
            numeSiPrenume: 'BBBBBB',
          },
          new Medic()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Medic', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            numeSiPrenume: 'BBBBBB',
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

      it('should delete a Medic', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMedicToCollectionIfMissing', () => {
        it('should add a Medic to an empty array', () => {
          const medic: IMedic = { id: 123 };
          expectedResult = service.addMedicToCollectionIfMissing([], medic);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(medic);
        });

        it('should not add a Medic to an array that contains it', () => {
          const medic: IMedic = { id: 123 };
          const medicCollection: IMedic[] = [
            {
              ...medic,
            },
            { id: 456 },
          ];
          expectedResult = service.addMedicToCollectionIfMissing(medicCollection, medic);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Medic to an array that doesn't contain it", () => {
          const medic: IMedic = { id: 123 };
          const medicCollection: IMedic[] = [{ id: 456 }];
          expectedResult = service.addMedicToCollectionIfMissing(medicCollection, medic);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(medic);
        });

        it('should add only unique Medic to an array', () => {
          const medicArray: IMedic[] = [{ id: 123 }, { id: 456 }, { id: 76555 }];
          const medicCollection: IMedic[] = [{ id: 123 }];
          expectedResult = service.addMedicToCollectionIfMissing(medicCollection, ...medicArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const medic: IMedic = { id: 123 };
          const medic2: IMedic = { id: 456 };
          expectedResult = service.addMedicToCollectionIfMissing([], medic, medic2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(medic);
          expect(expectedResult).toContain(medic2);
        });

        it('should accept null and undefined values', () => {
          const medic: IMedic = { id: 123 };
          expectedResult = service.addMedicToCollectionIfMissing([], null, medic, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(medic);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
