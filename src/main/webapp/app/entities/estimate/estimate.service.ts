import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEstimate } from 'app/shared/model/estimate.model';

type EntityResponseType = HttpResponse<IEstimate>;
type EntityArrayResponseType = HttpResponse<IEstimate[]>;

@Injectable({ providedIn: 'root' })
export class EstimateService {
    private resourceUrl = SERVER_API_URL + 'api/estimates';

    constructor(private http: HttpClient) {}

    create(estimate: IEstimate): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(estimate);
        return this.http
            .post<IEstimate>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(estimate: IEstimate): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(estimate);
        return this.http
            .put<IEstimate>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IEstimate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEstimate[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(estimate: IEstimate): IEstimate {
        const copy: IEstimate = Object.assign({}, estimate, {
            createdAt: estimate.createdAt != null && estimate.createdAt.isValid() ? estimate.createdAt.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((estimate: IEstimate) => {
            estimate.createdAt = estimate.createdAt != null ? moment(estimate.createdAt) : null;
        });
        return res;
    }
}
