import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedic, getMedicIdentifier } from '../medic.model';

export type EntityResponseType = HttpResponse<IMedic>;
export type EntityArrayResponseType = HttpResponse<IMedic[]>;

@Injectable({ providedIn: 'root' })
export class MedicService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/medics');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(medic: IMedic): Observable<EntityResponseType> {
    return this.http.post<IMedic>(this.resourceUrl, medic, { observe: 'response' });
  }

  update(medic: IMedic): Observable<EntityResponseType> {
    return this.http.put<IMedic>(`${this.resourceUrl}/${getMedicIdentifier(medic) as number}`, medic, { observe: 'response' });
  }

  partialUpdate(medic: IMedic): Observable<EntityResponseType> {
    return this.http.patch<IMedic>(`${this.resourceUrl}/${getMedicIdentifier(medic) as number}`, medic, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedic>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedic[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMedicToCollectionIfMissing(medicCollection: IMedic[], ...medicsToCheck: (IMedic | null | undefined)[]): IMedic[] {
    const medics: IMedic[] = medicsToCheck.filter(isPresent);
    if (medics.length > 0) {
      const medicCollectionIdentifiers = medicCollection.map(medicItem => getMedicIdentifier(medicItem)!);
      const medicsToAdd = medics.filter(medicItem => {
        const medicIdentifier = getMedicIdentifier(medicItem);
        if (medicIdentifier == null || medicCollectionIdentifiers.includes(medicIdentifier)) {
          return false;
        }
        medicCollectionIdentifiers.push(medicIdentifier);
        return true;
      });
      return [...medicsToAdd, ...medicCollection];
    }
    return medicCollection;
  }
}
