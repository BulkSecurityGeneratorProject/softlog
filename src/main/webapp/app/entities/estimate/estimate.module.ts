import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LogValueSharedModule } from 'app/shared';
import {
    EstimateComponent,
    EstimateDetailComponent,
    EstimateUpdateComponent,
    EstimateDeletePopupComponent,
    EstimateDeleteDialogComponent,
    estimateRoute,
    estimatePopupRoute
} from './';

const ENTITY_STATES = [...estimateRoute, ...estimatePopupRoute];

@NgModule({
    imports: [LogValueSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        EstimateComponent,
        EstimateDetailComponent,
        EstimateUpdateComponent,
        EstimateDeleteDialogComponent,
        EstimateDeletePopupComponent
    ],
    entryComponents: [EstimateComponent, EstimateUpdateComponent, EstimateDeleteDialogComponent, EstimateDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LogValueEstimateModule {}
