import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGrad, getGradIdentifier } from '../grad.model';

export type EntityResponseType = HttpResponse<IGrad>;
export type EntityArrayResponseType = HttpResponse<IGrad[]>;

@Injectable({ providedIn: 'root' })
export class GradService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/grads');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(grad: IGrad): Observable<EntityResponseType> {
    return this.http.post<IGrad>(this.resourceUrl, grad, { observe: 'response' });
  }

  update(grad: IGrad): Observable<EntityResponseType> {
    return this.http.put<IGrad>(`${this.resourceUrl}/${getGradIdentifier(grad) as number}`, grad, { observe: 'response' });
  }

  partialUpdate(grad: IGrad): Observable<EntityResponseType> {
    return this.http.patch<IGrad>(`${this.resourceUrl}/${getGradIdentifier(grad) as number}`, grad, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGrad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGrad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGradToCollectionIfMissing(gradCollection: IGrad[], ...gradsToCheck: (IGrad | null | undefined)[]): IGrad[] {
    const grads: IGrad[] = gradsToCheck.filter(isPresent);
    if (grads.length > 0) {
      const gradCollectionIdentifiers = gradCollection.map(gradItem => getGradIdentifier(gradItem)!);
      const gradsToAdd = grads.filter(gradItem => {
        const gradIdentifier = getGradIdentifier(gradItem);
        if (gradIdentifier == null || gradCollectionIdentifiers.includes(gradIdentifier)) {
          return false;
        }
        gradCollectionIdentifiers.push(gradIdentifier);
        return true;
      });
      return [...gradsToAdd, ...gradCollection];
    }
    return gradCollection;
  }
}
