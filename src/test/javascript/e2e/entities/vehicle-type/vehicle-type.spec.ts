import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { VehicleTypeComponentsPage, VehicleTypeUpdatePage } from './vehicle-type.page-object';

describe('VehicleType e2e test', () => {
    let navBarPage: NavBarPage;
    let vehicleTypeUpdatePage: VehicleTypeUpdatePage;
    let vehicleTypeComponentsPage: VehicleTypeComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load VehicleTypes', () => {
        navBarPage.goToEntity('vehicle-type');
        vehicleTypeComponentsPage = new VehicleTypeComponentsPage();
        expect(vehicleTypeComponentsPage.getTitle()).toMatch(/logValueApp.vehicleType.home.title/);
    });

    it('should load create VehicleType page', () => {
        vehicleTypeComponentsPage.clickOnCreateButton();
        vehicleTypeUpdatePage = new VehicleTypeUpdatePage();
        expect(vehicleTypeUpdatePage.getPageTitle()).toMatch(/logValueApp.vehicleType.home.createOrEditLabel/);
        vehicleTypeUpdatePage.cancel();
    });

    it('should create and save VehicleTypes', () => {
        vehicleTypeComponentsPage.clickOnCreateButton();
        vehicleTypeUpdatePage.setNameInput('name');
        expect(vehicleTypeUpdatePage.getNameInput()).toMatch('name');
        vehicleTypeUpdatePage.setFactorInput('5');
        expect(vehicleTypeUpdatePage.getFactorInput()).toMatch('5');
        vehicleTypeUpdatePage.setRegularLoadInput('5');
        expect(vehicleTypeUpdatePage.getRegularLoadInput()).toMatch('5');
        vehicleTypeUpdatePage.setMaximumLoadInput('5');
        expect(vehicleTypeUpdatePage.getMaximumLoadInput()).toMatch('5');
        vehicleTypeUpdatePage.save();
        expect(vehicleTypeUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
