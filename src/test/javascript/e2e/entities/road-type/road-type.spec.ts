import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { RoadTypeComponentsPage, RoadTypeUpdatePage } from './road-type.page-object';

describe('RoadType e2e test', () => {
    let navBarPage: NavBarPage;
    let roadTypeUpdatePage: RoadTypeUpdatePage;
    let roadTypeComponentsPage: RoadTypeComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load RoadTypes', () => {
        navBarPage.goToEntity('road-type');
        roadTypeComponentsPage = new RoadTypeComponentsPage();
        expect(roadTypeComponentsPage.getTitle()).toMatch(/logValueApp.roadType.home.title/);
    });

    it('should load create RoadType page', () => {
        roadTypeComponentsPage.clickOnCreateButton();
        roadTypeUpdatePage = new RoadTypeUpdatePage();
        expect(roadTypeUpdatePage.getPageTitle()).toMatch(/logValueApp.roadType.home.createOrEditLabel/);
        roadTypeUpdatePage.cancel();
    });

    it('should create and save RoadTypes', () => {
        roadTypeComponentsPage.clickOnCreateButton();
        roadTypeUpdatePage.setNameInput('name');
        expect(roadTypeUpdatePage.getNameInput()).toMatch('name');
        roadTypeUpdatePage.setFactorInput('5');
        expect(roadTypeUpdatePage.getFactorInput()).toMatch('5');
        roadTypeUpdatePage.save();
        expect(roadTypeUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
