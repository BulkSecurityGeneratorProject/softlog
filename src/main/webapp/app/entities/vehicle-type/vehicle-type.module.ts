import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LogValueSharedModule } from 'app/shared';
import {
    VehicleTypeComponent,
    VehicleTypeDetailComponent,
    VehicleTypeUpdateComponent,
    VehicleTypeDeletePopupComponent,
    VehicleTypeDeleteDialogComponent,
    vehicleTypeRoute,
    vehicleTypePopupRoute
} from './';

const ENTITY_STATES = [...vehicleTypeRoute, ...vehicleTypePopupRoute];

@NgModule({
    imports: [LogValueSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        VehicleTypeComponent,
        VehicleTypeDetailComponent,
        VehicleTypeUpdateComponent,
        VehicleTypeDeleteDialogComponent,
        VehicleTypeDeletePopupComponent
    ],
    entryComponents: [VehicleTypeComponent, VehicleTypeUpdateComponent, VehicleTypeDeleteDialogComponent, VehicleTypeDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LogValueVehicleTypeModule {}
