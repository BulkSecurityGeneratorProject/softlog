/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { LogValueTestModule } from '../../../test.module';
import { VehicleTypeDeleteDialogComponent } from 'app/entities/vehicle-type/vehicle-type-delete-dialog.component';
import { VehicleTypeService } from 'app/entities/vehicle-type/vehicle-type.service';

describe('Component Tests', () => {
    describe('VehicleType Management Delete Component', () => {
        let comp: VehicleTypeDeleteDialogComponent;
        let fixture: ComponentFixture<VehicleTypeDeleteDialogComponent>;
        let service: VehicleTypeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [LogValueTestModule],
                declarations: [VehicleTypeDeleteDialogComponent]
            })
                .overrideTemplate(VehicleTypeDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(VehicleTypeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VehicleTypeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
