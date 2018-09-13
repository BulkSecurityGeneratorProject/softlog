import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LogValueRoadTypeModule } from './road-type/road-type.module';
import { LogValueVehicleTypeModule } from './vehicle-type/vehicle-type.module';
import { LogValueEstimateModule } from './estimate/estimate.module';
import { LogValueFreightModule } from './freight/freight.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        LogValueRoadTypeModule,
        LogValueVehicleTypeModule,
        LogValueEstimateModule,
        LogValueFreightModule
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LogValueEntityModule {}
