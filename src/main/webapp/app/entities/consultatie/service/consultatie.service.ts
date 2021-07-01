import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsultatie, getConsultatieIdentifier } from '../consultatie.model';

export type EntityResponseType = HttpResponse<IConsultatie>;
export type EntityArrayResponseType = HttpResponse<IConsultatie[]>;

@Injectable({ providedIn: 'root' })
export class ConsultatieService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/consultaties');

  //
  public listaConsultatiiUrl = this.applicationConfigService.getEndpointFor('api/getListaConsultatii');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(consultatie: IConsultatie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consultatie);
    return this.http
      .post<IConsultatie>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(consultatie: IConsultatie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consultatie);
    return this.http
      .put<IConsultatie>(`${this.resourceUrl}/${getConsultatieIdentifier(consultatie) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(consultatie: IConsultatie): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(consultatie);
    return this.http
      .patch<IConsultatie>(`${this.resourceUrl}/${getConsultatieIdentifier(consultatie) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IConsultatie>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IConsultatie[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConsultatieToCollectionIfMissing(
    consultatieCollection: IConsultatie[],
    ...consultatiesToCheck: (IConsultatie | null | undefined)[]
  ): IConsultatie[] {
    const consultaties: IConsultatie[] = consultatiesToCheck.filter(isPresent);
    if (consultaties.length > 0) {
      const consultatieCollectionIdentifiers = consultatieCollection.map(consultatieItem => getConsultatieIdentifier(consultatieItem)!);
      const consultatiesToAdd = consultaties.filter(consultatieItem => {
        const consultatieIdentifier = getConsultatieIdentifier(consultatieItem);
        if (consultatieIdentifier == null || consultatieCollectionIdentifiers.includes(consultatieIdentifier)) {
          return false;
        }
        consultatieCollectionIdentifiers.push(consultatieIdentifier);
        return true;
      });
      return [...consultatiesToAdd, ...consultatieCollection];
    }
    return consultatieCollection;
  }

  //
  getListaConsultatii(): Observable<IConsultatie[]> {
    return this.http.get<IConsultatie[]>(`${this.listaConsultatiiUrl}`);
  }

  protected convertDateFromClient(consultatie: IConsultatie): IConsultatie {
    return Object.assign({}, consultatie, {
      dataOra: consultatie.dataOra?.isValid() ? consultatie.dataOra.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataOra = res.body.dataOra ? dayjs(res.body.dataOra) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((consultatie: IConsultatie) => {
        consultatie.dataOra = consultatie.dataOra ? dayjs(consultatie.dataOra) : undefined;
      });
    }
    return res;
  }
}
