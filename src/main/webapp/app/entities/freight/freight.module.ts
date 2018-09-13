import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { LogValueSharedModule } from 'app/shared';
import {
    FreightComponent,
    freightRoute
} from './';

const ENTITY_STATES = [...freightRoute];

@NgModule({
    imports: [LogValueSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FreightComponent
    ],
    entryComponents: [FreightComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LogValueFreightModule {}
