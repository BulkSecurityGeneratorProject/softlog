import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEstimate } from 'app/shared/model/estimate.model';

@Component({
    selector: 'jhi-estimate-detail',
    templateUrl: './estimate-detail.component.html'
})
export class EstimateDetailComponent implements OnInit {
    estimate: IEstimate;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ estimate }) => {
            this.estimate = estimate;
        });
    }

    previousState() {
        window.history.back();
    }
}
