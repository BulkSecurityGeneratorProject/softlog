import { browser } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { RoadTypeComponentsPage, RoadTypeUpdatePage } from './road-type.page-object';

describe('RoadType e2e test', () => {
    let navBarPage: NavBarPage;
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

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
