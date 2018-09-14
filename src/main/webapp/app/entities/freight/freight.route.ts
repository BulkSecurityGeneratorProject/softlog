import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Estimate } from 'app/shared/model/estimate.model';
import { EstimateService } from '../estimate/estimate.service';
import { FreightComponent } from './freight.component';
import { IEstimate } from 'app/shared/model/estimate.model';

@Injectable({ providedIn: 'root' })
export class FreightResolve implements Resolve<IEstimate> {
    constructor(private service: EstimateService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((estimate: HttpResponse<Estimate>) => estimate.body));
        }
        return of(new Estimate());
    }
}

export const freightRoute: Routes = [
    {
        path: 'freight/new',
        component: FreightComponent,
        resolve: {
            estimate: FreightResolve
        },
        data: {
            authorities: ['ROLE_USER', 'ROLE_ADMIN'],
            pageTitle: 'logValueApp.freight.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
