import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Estimate } from 'app/shared/model/estimate.model';
import { EstimateService } from './estimate.service';
import { EstimateComponent } from './estimate.component';
import { EstimateDetailComponent } from './estimate-detail.component';
import { EstimateUpdateComponent } from './estimate-update.component';
import { EstimateDeletePopupComponent } from './estimate-delete-dialog.component';
import { IEstimate } from 'app/shared/model/estimate.model';

@Injectable({ providedIn: 'root' })
export class EstimateResolve implements Resolve<IEstimate> {
    constructor(private service: EstimateService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((estimate: HttpResponse<Estimate>) => estimate.body));
        }
        return of(new Estimate());
    }
}

export const estimateRoute: Routes = [
    {
        path: 'estimate',
        component: EstimateComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'logValueApp.estimate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'estimate/:id/view',
        component: EstimateDetailComponent,
        resolve: {
            estimate: EstimateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.estimate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'estimate/new',
        component: EstimateUpdateComponent,
        resolve: {
            estimate: EstimateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.estimate.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'estimate/:id/edit',
        component: EstimateUpdateComponent,
        resolve: {
            estimate: EstimateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.estimate.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const estimatePopupRoute: Routes = [
    {
        path: 'estimate/:id/delete',
        component: EstimateDeletePopupComponent,
        resolve: {
            estimate: EstimateResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.estimate.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
