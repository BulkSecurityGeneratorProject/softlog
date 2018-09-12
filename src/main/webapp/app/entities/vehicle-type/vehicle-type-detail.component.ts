import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVehicleType } from 'app/shared/model/vehicle-type.model';

@Component({
    selector: 'jhi-vehicle-type-detail',
    templateUrl: './vehicle-type-detail.component.html'
})
export class VehicleTypeDetailComponent implements OnInit {
    vehicleType: IVehicleType;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ vehicleType }) => {
            this.vehicleType = vehicleType;
        });
    }

    previousState() {
        window.history.back();
    }
}
