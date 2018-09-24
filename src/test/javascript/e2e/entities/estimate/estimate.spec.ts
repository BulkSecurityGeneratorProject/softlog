import { browser, protractor } from 'protractor';
import { NavBarPage } from './../../page-objects/jhi-page-objects';
import { EstimateComponentsPage, EstimateUpdatePage } from './estimate.page-object';

describe('Estimate e2e test', () => {
    let navBarPage: NavBarPage;
    let estimateUpdatePage: EstimateUpdatePage;
    let estimateComponentsPage: EstimateComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Estimates', () => {
        navBarPage.goToEntity('estimate');
        estimateComponentsPage = new EstimateComponentsPage();
        expect(estimateComponentsPage.getTitle()).toMatch(/logValueApp.estimate.home.title/);
    });

    it('should load create Estimate page', () => {
        estimateComponentsPage.clickOnCreateButton();
        estimateUpdatePage = new EstimateUpdatePage();
        expect(estimateUpdatePage.getPageTitle()).toMatch(/logValueApp.estimate.home.createOrEditLabel/);
        estimateUpdatePage.cancel();
    });

    it('should create and save Estimates', () => {
        estimateComponentsPage.clickOnCreateButton();
        estimateUpdatePage.setPavedHighwayAmountInput('5');
        expect(estimateUpdatePage.getPavedHighwayAmountInput()).toMatch('5');
        estimateUpdatePage.setNonPavedHighwayAmountInput('5');
        expect(estimateUpdatePage.getNonPavedHighwayAmountInput()).toMatch('5');
        estimateUpdatePage
            .getContainsTollInput()
            .isSelected()
            .then(selected => {
                if (selected) {
                    estimateUpdatePage.getContainsTollInput().click();
                    expect(estimateUpdatePage.getContainsTollInput().isSelected()).toBeFalsy();
                } else {
                    estimateUpdatePage.getContainsTollInput().click();
                    expect(estimateUpdatePage.getContainsTollInput().isSelected()).toBeTruthy();
                }
            });
        estimateUpdatePage.setTollValueInput('5');
        expect(estimateUpdatePage.getTollValueInput()).toMatch('5');
        estimateUpdatePage.vehicleTypeSelectLastOption();
        estimateUpdatePage.save();
        expect(estimateUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});
