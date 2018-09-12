import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { RoadType } from 'app/shared/model/road-type.model';
import { RoadTypeService } from './road-type.service';
import { RoadTypeComponent } from './road-type.component';
import { RoadTypeDetailComponent } from './road-type-detail.component';
import { RoadTypeUpdateComponent } from './road-type-update.component';
import { RoadTypeDeletePopupComponent } from './road-type-delete-dialog.component';
import { IRoadType } from 'app/shared/model/road-type.model';

@Injectable({ providedIn: 'root' })
export class RoadTypeResolve implements Resolve<IRoadType> {
    constructor(private service: RoadTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((roadType: HttpResponse<RoadType>) => roadType.body));
        }
        return of(new RoadType());
    }
}

export const roadTypeRoute: Routes = [
    {
        path: 'road-type',
        component: RoadTypeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'logValueApp.roadType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'road-type/:id/view',
        component: RoadTypeDetailComponent,
        resolve: {
            roadType: RoadTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.roadType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'road-type/new',
        component: RoadTypeUpdateComponent,
        resolve: {
            roadType: RoadTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.roadType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'road-type/:id/edit',
        component: RoadTypeUpdateComponent,
        resolve: {
            roadType: RoadTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.roadType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const roadTypePopupRoute: Routes = [
    {
        path: 'road-type/:id/delete',
        component: RoadTypeDeletePopupComponent,
        resolve: {
            roadType: RoadTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.roadType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
