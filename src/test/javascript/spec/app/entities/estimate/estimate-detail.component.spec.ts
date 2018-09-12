/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LogValueTestModule } from '../../../test.module';
import { EstimateDetailComponent } from 'app/entities/estimate/estimate-detail.component';
import { Estimate } from 'app/shared/model/estimate.model';

describe('Component Tests', () => {
    describe('Estimate Management Detail Component', () => {
        let comp: EstimateDetailComponent;
        let fixture: ComponentFixture<EstimateDetailComponent>;
        const route = ({ data: of({ estimate: new Estimate(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LogValueTestModule],
                declarations: [EstimateDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(EstimateDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EstimateDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.estimate).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
