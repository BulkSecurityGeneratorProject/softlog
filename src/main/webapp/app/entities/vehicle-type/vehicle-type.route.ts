import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { VehicleType } from 'app/shared/model/vehicle-type.model';
import { VehicleTypeService } from './vehicle-type.service';
import { VehicleTypeComponent } from './vehicle-type.component';
import { VehicleTypeDetailComponent } from './vehicle-type-detail.component';
import { VehicleTypeUpdateComponent } from './vehicle-type-update.component';
import { VehicleTypeDeletePopupComponent } from './vehicle-type-delete-dialog.component';
import { IVehicleType } from 'app/shared/model/vehicle-type.model';

@Injectable({ providedIn: 'root' })
export class VehicleTypeResolve implements Resolve<IVehicleType> {
    constructor(private service: VehicleTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((vehicleType: HttpResponse<VehicleType>) => vehicleType.body));
        }
        return of(new VehicleType());
    }
}

export const vehicleTypeRoute: Routes = [
    {
        path: 'vehicle-type',
        component: VehicleTypeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'logValueApp.vehicleType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'vehicle-type/:id/view',
        component: VehicleTypeDetailComponent,
        resolve: {
            vehicleType: VehicleTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.vehicleType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'vehicle-type/new',
        component: VehicleTypeUpdateComponent,
        resolve: {
            vehicleType: VehicleTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.vehicleType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'vehicle-type/:id/edit',
        component: VehicleTypeUpdateComponent,
        resolve: {
            vehicleType: VehicleTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.vehicleType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const vehicleTypePopupRoute: Routes = [
    {
        path: 'vehicle-type/:id/delete',
        component: VehicleTypeDeletePopupComponent,
        resolve: {
            vehicleType: VehicleTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'logValueApp.vehicleType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
