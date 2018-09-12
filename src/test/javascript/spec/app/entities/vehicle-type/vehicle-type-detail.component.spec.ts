/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LogValueTestModule } from '../../../test.module';
import { VehicleTypeDetailComponent } from 'app/entities/vehicle-type/vehicle-type-detail.component';
import { VehicleType } from 'app/shared/model/vehicle-type.model';

describe('Component Tests', () => {
    describe('VehicleType Management Detail Component', () => {
        let comp: VehicleTypeDetailComponent;
        let fixture: ComponentFixture<VehicleTypeDetailComponent>;
        const route = ({ data: of({ vehicleType: new VehicleType(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LogValueTestModule],
                declarations: [VehicleTypeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(VehicleTypeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(VehicleTypeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.vehicleType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
