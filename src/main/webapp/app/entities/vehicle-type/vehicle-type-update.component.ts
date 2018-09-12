import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IVehicleType } from 'app/shared/model/vehicle-type.model';
import { VehicleTypeService } from './vehicle-type.service';

@Component({
    selector: 'jhi-vehicle-type-update',
    templateUrl: './vehicle-type-update.component.html'
})
export class VehicleTypeUpdateComponent implements OnInit {
    private _vehicleType: IVehicleType;
    isSaving: boolean;

    constructor(private vehicleTypeService: VehicleTypeService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ vehicleType }) => {
            this.vehicleType = vehicleType;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.vehicleType.id !== undefined) {
            this.subscribeToSaveResponse(this.vehicleTypeService.update(this.vehicleType));
        } else {
            this.subscribeToSaveResponse(this.vehicleTypeService.create(this.vehicleType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IVehicleType>>) {
        result.subscribe((res: HttpResponse<IVehicleType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get vehicleType() {
        return this._vehicleType;
    }

    set vehicleType(vehicleType: IVehicleType) {
        this._vehicleType = vehicleType;
    }
}
