import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IRoadType } from 'app/shared/model/road-type.model';
import { RoadTypeService } from './road-type.service';

@Component({
    selector: 'jhi-road-type-update',
    templateUrl: './road-type-update.component.html'
})
export class RoadTypeUpdateComponent implements OnInit {
    private _roadType: IRoadType;
    isSaving: boolean;

    constructor(private roadTypeService: RoadTypeService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ roadType }) => {
            this.roadType = roadType;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.roadType.id !== undefined) {
            this.subscribeToSaveResponse(this.roadTypeService.update(this.roadType));
        } else {
            this.subscribeToSaveResponse(this.roadTypeService.create(this.roadType));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRoadType>>) {
        result.subscribe((res: HttpResponse<IRoadType>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get roadType() {
        return this._roadType;
    }

    set roadType(roadType: IRoadType) {
        this._roadType = roadType;
    }
}
