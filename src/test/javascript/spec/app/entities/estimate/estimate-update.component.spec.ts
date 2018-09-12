/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LogValueTestModule } from '../../../test.module';
import { EstimateUpdateComponent } from 'app/entities/estimate/estimate-update.component';
import { EstimateService } from 'app/entities/estimate/estimate.service';
import { Estimate } from 'app/shared/model/estimate.model';

describe('Component Tests', () => {
    describe('Estimate Management Update Component', () => {
        let comp: EstimateUpdateComponent;
        let fixture: ComponentFixture<EstimateUpdateComponent>;
        let service: EstimateService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LogValueTestModule],
                declarations: [EstimateUpdateComponent]
            })
                .overrideTemplate(EstimateUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(EstimateUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EstimateService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Estimate(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.estimate = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Estimate();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.estimate = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
