import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpecializare, getSpecializareIdentifier } from '../specializare.model';

export type EntityResponseType = HttpResponse<ISpecializare>;
export type EntityArrayResponseType = HttpResponse<ISpecializare[]>;

@Injectable({ providedIn: 'root' })
export class SpecializareService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/specializares');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(specializare: ISpecializare): Observable<EntityResponseType> {
    return this.http.post<ISpecializare>(this.resourceUrl, specializare, { observe: 'response' });
  }

  update(specializare: ISpecializare): Observable<EntityResponseType> {
    return this.http.put<ISpecializare>(`${this.resourceUrl}/${getSpecializareIdentifier(specializare) as number}`, specializare, {
      observe: 'response',
    });
  }

  partialUpdate(specializare: ISpecializare): Observable<EntityResponseType> {
    return this.http.patch<ISpecializare>(`${this.resourceUrl}/${getSpecializareIdentifier(specializare) as number}`, specializare, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISpecializare>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpecializare[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSpecializareToCollectionIfMissing(
    specializareCollection: ISpecializare[],
    ...specializaresToCheck: (ISpecializare | null | undefined)[]
  ): ISpecializare[] {
    const specializares: ISpecializare[] = specializaresToCheck.filter(isPresent);
    if (specializares.length > 0) {
      const specializareCollectionIdentifiers = specializareCollection.map(
        specializareItem => getSpecializareIdentifier(specializareItem)!
      );
      const specializaresToAdd = specializares.filter(specializareItem => {
        const specializareIdentifier = getSpecializareIdentifier(specializareItem);
        if (specializareIdentifier == null || specializareCollectionIdentifiers.includes(specializareIdentifier)) {
          return false;
        }
        specializareCollectionIdentifiers.push(specializareIdentifier);
        return true;
      });
      return [...specializaresToAdd, ...specializareCollection];
    }
    return specializareCollection;
  }
}
