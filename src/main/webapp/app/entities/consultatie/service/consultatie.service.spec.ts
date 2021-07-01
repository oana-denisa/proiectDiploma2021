import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IConsultatie, Consultatie } from '../consultatie.model';

import { ConsultatieService } from './consultatie.service';

describe('Service Tests', () => {
  describe('Consultatie Service', () => {
    let service: ConsultatieService;
    let httpMock: HttpTestingController;
    let elemDefault: IConsultatie;
    let expectedResult: IConsultatie | IConsultatie[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ConsultatieService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dataOra: currentDate,
        descriere: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataOra: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Consultatie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataOra: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataOra: currentDate,
          },
          returnedFromService
        );

        service.create(new Consultatie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Consultatie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dataOra: currentDate.format(DATE_TIME_FORMAT),
            descriere: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataOra: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Consultatie', () => {
        const patchObject = Object.assign(
          {
            descriere: 'BBBBBB',
          },
          new Consultatie()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataOra: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Consultatie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dataOra: currentDate.format(DATE_TIME_FORMAT),
            descriere: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataOra: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Consultatie', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addConsultatieToCollectionIfMissing', () => {
        it('should add a Consultatie to an empty array', () => {
          const consultatie: IConsultatie = { id: 123 };
          expectedResult = service.addConsultatieToCollectionIfMissing([], consultatie);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(consultatie);
        });

        it('should not add a Consultatie to an array that contains it', () => {
          const consultatie: IConsultatie = { id: 123 };
          const consultatieCollection: IConsultatie[] = [
            {
              ...consultatie,
            },
            { id: 456 },
          ];
          expectedResult = service.addConsultatieToCollectionIfMissing(consultatieCollection, consultatie);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Consultatie to an array that doesn't contain it", () => {
          const consultatie: IConsultatie = { id: 123 };
          const consultatieCollection: IConsultatie[] = [{ id: 456 }];
          expectedResult = service.addConsultatieToCollectionIfMissing(consultatieCollection, consultatie);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(consultatie);
        });

        it('should add only unique Consultatie to an array', () => {
          const consultatieArray: IConsultatie[] = [{ id: 123 }, { id: 456 }, { id: 62050 }];
          const consultatieCollection: IConsultatie[] = [{ id: 123 }];
          expectedResult = service.addConsultatieToCollectionIfMissing(consultatieCollection, ...consultatieArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const consultatie: IConsultatie = { id: 123 };
          const consultatie2: IConsultatie = { id: 456 };
          expectedResult = service.addConsultatieToCollectionIfMissing([], consultatie, consultatie2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(consultatie);
          expect(expectedResult).toContain(consultatie2);
        });

        it('should accept null and undefined values', () => {
          const consultatie: IConsultatie = { id: 123 };
          expectedResult = service.addConsultatieToCollectionIfMissing([], null, consultatie, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(consultatie);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
