import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IEstimate } from 'app/shared/model/estimate.model';
import { EstimateService } from './estimate.service';
import { IRoadType } from 'app/shared/model/road-type.model';
import { RoadTypeService } from 'app/entities/road-type';
import { IVehicleType } from 'app/shared/model/vehicle-type.model';
import { VehicleTypeService } from 'app/entities/vehicle-type';

@Component({
    selector: 'jhi-estimate-update',
    templateUrl: './estimate-update.component.html'
})
export class EstimateUpdateComponent implements OnInit {
    private _estimate: IEstimate;
    isSaving: boolean;

    roadtypes: IRoadType[];

    vehicletypes: IVehicleType[];
    createdAt: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private estimateService: EstimateService,
        private roadTypeService: RoadTypeService,
        private vehicleTypeService: VehicleTypeService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ estimate }) => {
            this.estimate = estimate;
        });
        this.roadTypeService.query().subscribe(
            (res: HttpResponse<IRoadType[]>) => {
                this.roadtypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.vehicleTypeService.query().subscribe(
            (res: HttpResponse<IVehicleType[]>) => {
                this.vehicletypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.estimate.createdAt = moment(this.createdAt, DATE_TIME_FORMAT);
        if (this.estimate.id !== undefined) {
            this.subscribeToSaveResponse(this.estimateService.update(this.estimate));
        } else {
            this.subscribeToSaveResponse(this.estimateService.create(this.estimate));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEstimate>>) {
        result.subscribe((res: HttpResponse<IEstimate>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackRoadTypeById(index: number, item: IRoadType) {
        return item.id;
    }

    trackVehicleTypeById(index: number, item: IVehicleType) {
        return item.id;
    }
    get estimate() {
        return this._estimate;
    }

    set estimate(estimate: IEstimate) {
        this._estimate = estimate;
        this.createdAt = moment(estimate.createdAt).format(DATE_TIME_FORMAT);
    }
}
