/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { LogValueTestModule } from '../../../test.module';
import { VehicleTypeUpdateComponent } from 'app/entities/vehicle-type/vehicle-type-update.component';
import { VehicleTypeService } from 'app/entities/vehicle-type/vehicle-type.service';
import { VehicleType } from 'app/shared/model/vehicle-type.model';

describe('Component Tests', () => {
    describe('VehicleType Management Update Component', () => {
        let comp: VehicleTypeUpdateComponent;
        let fixture: ComponentFixture<VehicleTypeUpdateComponent>;
        let service: VehicleTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LogValueTestModule],
                declarations: [VehicleTypeUpdateComponent]
            })
                .overrideTemplate(VehicleTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(VehicleTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VehicleTypeService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new VehicleType(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.vehicleType = entity;
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
                    const entity = new VehicleType();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.vehicleType = entity;
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
