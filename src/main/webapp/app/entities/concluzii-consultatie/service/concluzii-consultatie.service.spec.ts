import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConcluziiConsultatie, ConcluziiConsultatie } from '../concluzii-consultatie.model';

import { ConcluziiConsultatieService } from './concluzii-consultatie.service';

describe('Service Tests', () => {
  describe('ConcluziiConsultatie Service', () => {
    let service: ConcluziiConsultatieService;
    let httpMock: HttpTestingController;
    let elemDefault: IConcluziiConsultatie;
    let expectedResult: IConcluziiConsultatie | IConcluziiConsultatie[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ConcluziiConsultatieService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        diagnostic: 'AAAAAAA',
        tratament: 'AAAAAAA',
        observatii: 'AAAAAAA',
        controlUrmator: 'AAAAAAA',
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

      it('should create a ConcluziiConsultatie', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ConcluziiConsultatie()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ConcluziiConsultatie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            diagnostic: 'BBBBBB',
            tratament: 'BBBBBB',
            observatii: 'BBBBBB',
            controlUrmator: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ConcluziiConsultatie', () => {
        const patchObject = Object.assign({}, new ConcluziiConsultatie());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ConcluziiConsultatie', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            diagnostic: 'BBBBBB',
            tratament: 'BBBBBB',
            observatii: 'BBBBBB',
            controlUrmator: 'BBBBBB',
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

      it('should delete a ConcluziiConsultatie', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addConcluziiConsultatieToCollectionIfMissing', () => {
        it('should add a ConcluziiConsultatie to an empty array', () => {
          const concluziiConsultatie: IConcluziiConsultatie = { id: 123 };
          expectedResult = service.addConcluziiConsultatieToCollectionIfMissing([], concluziiConsultatie);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(concluziiConsultatie);
        });

        it('should not add a ConcluziiConsultatie to an array that contains it', () => {
          const concluziiConsultatie: IConcluziiConsultatie = { id: 123 };
          const concluziiConsultatieCollection: IConcluziiConsultatie[] = [
            {
              ...concluziiConsultatie,
            },
            { id: 456 },
          ];
          expectedResult = service.addConcluziiConsultatieToCollectionIfMissing(concluziiConsultatieCollection, concluziiConsultatie);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ConcluziiConsultatie to an array that doesn't contain it", () => {
          const concluziiConsultatie: IConcluziiConsultatie = { id: 123 };
          const concluziiConsultatieCollection: IConcluziiConsultatie[] = [{ id: 456 }];
          expectedResult = service.addConcluziiConsultatieToCollectionIfMissing(concluziiConsultatieCollection, concluziiConsultatie);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(concluziiConsultatie);
        });

        it('should add only unique ConcluziiConsultatie to an array', () => {
          const concluziiConsultatieArray: IConcluziiConsultatie[] = [{ id: 123 }, { id: 456 }, { id: 96221 }];
          const concluziiConsultatieCollection: IConcluziiConsultatie[] = [{ id: 123 }];
          expectedResult = service.addConcluziiConsultatieToCollectionIfMissing(
            concluziiConsultatieCollection,
            ...concluziiConsultatieArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const concluziiConsultatie: IConcluziiConsultatie = { id: 123 };
          const concluziiConsultatie2: IConcluziiConsultatie = { id: 456 };
          expectedResult = service.addConcluziiConsultatieToCollectionIfMissing([], concluziiConsultatie, concluziiConsultatie2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(concluziiConsultatie);
          expect(expectedResult).toContain(concluziiConsultatie2);
        });

        it('should accept null and undefined values', () => {
          const concluziiConsultatie: IConcluziiConsultatie = { id: 123 };
          expectedResult = service.addConcluziiConsultatieToCollectionIfMissing([], null, concluziiConsultatie, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(concluziiConsultatie);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
