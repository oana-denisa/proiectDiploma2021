import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConcluziiConsultatie, getConcluziiConsultatieIdentifier } from '../concluzii-consultatie.model';

export type EntityResponseType = HttpResponse<IConcluziiConsultatie>;
export type EntityArrayResponseType = HttpResponse<IConcluziiConsultatie[]>;

@Injectable({ providedIn: 'root' })
export class ConcluziiConsultatieService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/concluzii-consultaties');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(concluziiConsultatie: IConcluziiConsultatie): Observable<EntityResponseType> {
    return this.http.post<IConcluziiConsultatie>(this.resourceUrl, concluziiConsultatie, { observe: 'response' });
  }

  update(concluziiConsultatie: IConcluziiConsultatie): Observable<EntityResponseType> {
    return this.http.put<IConcluziiConsultatie>(
      `${this.resourceUrl}/${getConcluziiConsultatieIdentifier(concluziiConsultatie) as number}`,
      concluziiConsultatie,
      { observe: 'response' }
    );
  }

  partialUpdate(concluziiConsultatie: IConcluziiConsultatie): Observable<EntityResponseType> {
    return this.http.patch<IConcluziiConsultatie>(
      `${this.resourceUrl}/${getConcluziiConsultatieIdentifier(concluziiConsultatie) as number}`,
      concluziiConsultatie,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConcluziiConsultatie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConcluziiConsultatie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConcluziiConsultatieToCollectionIfMissing(
    concluziiConsultatieCollection: IConcluziiConsultatie[],
    ...concluziiConsultatiesToCheck: (IConcluziiConsultatie | null | undefined)[]
  ): IConcluziiConsultatie[] {
    const concluziiConsultaties: IConcluziiConsultatie[] = concluziiConsultatiesToCheck.filter(isPresent);
    if (concluziiConsultaties.length > 0) {
      const concluziiConsultatieCollectionIdentifiers = concluziiConsultatieCollection.map(
        concluziiConsultatieItem => getConcluziiConsultatieIdentifier(concluziiConsultatieItem)!
      );
      const concluziiConsultatiesToAdd = concluziiConsultaties.filter(concluziiConsultatieItem => {
        const concluziiConsultatieIdentifier = getConcluziiConsultatieIdentifier(concluziiConsultatieItem);
        if (concluziiConsultatieIdentifier == null || concluziiConsultatieCollectionIdentifiers.includes(concluziiConsultatieIdentifier)) {
          return false;
        }
        concluziiConsultatieCollectionIdentifiers.push(concluziiConsultatieIdentifier);
        return true;
      });
      return [...concluziiConsultatiesToAdd, ...concluziiConsultatieCollection];
    }
    return concluziiConsultatieCollection;
  }
}
