import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LogValueSharedModule } from 'app/shared';
import {
    RoadTypeComponent,
    RoadTypeDetailComponent,
    RoadTypeUpdateComponent,
    RoadTypeDeletePopupComponent,
    RoadTypeDeleteDialogComponent,
    roadTypeRoute,
    roadTypePopupRoute
} from './';

const ENTITY_STATES = [...roadTypeRoute, ...roadTypePopupRoute];

@NgModule({
    imports: [LogValueSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        RoadTypeComponent,
        RoadTypeDetailComponent,
        RoadTypeUpdateComponent,
        RoadTypeDeleteDialogComponent,
        RoadTypeDeletePopupComponent
    ],
    entryComponents: [RoadTypeComponent, RoadTypeUpdateComponent, RoadTypeDeleteDialogComponent, RoadTypeDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LogValueRoadTypeModule {}
