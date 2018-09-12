import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IVehicleType } from 'app/shared/model/vehicle-type.model';

type EntityResponseType = HttpResponse<IVehicleType>;
type EntityArrayResponseType = HttpResponse<IVehicleType[]>;

@Injectable({ providedIn: 'root' })
export class VehicleTypeService {
    private resourceUrl = SERVER_API_URL + 'api/vehicle-types';

    constructor(private http: HttpClient) {}

    create(vehicleType: IVehicleType): Observable<EntityResponseType> {
        return this.http.post<IVehicleType>(this.resourceUrl, vehicleType, { observe: 'response' });
    }

    update(vehicleType: IVehicleType): Observable<EntityResponseType> {
        return this.http.put<IVehicleType>(this.resourceUrl, vehicleType, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IVehicleType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IVehicleType[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
