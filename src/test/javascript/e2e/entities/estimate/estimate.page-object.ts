import { element, by, promise, ElementFinder } from 'protractor';

export class EstimateComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    title = element.all(by.css('jhi-estimate div h2#page-heading span')).first();

    clickOnCreateButton(): promise.Promise<void> {
        return this.createButton.click();
    }

    getTitle(): any {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EstimateUpdatePage {
    pageTitle = element(by.id('jhi-estimate-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    pavedHighwayAmountInput = element(by.id('field_pavedHighwayAmount'));
    nonPavedHighwayAmountInput = element(by.id('field_nonPavedHighwayAmount'));
    containsTollInput = element(by.id('field_containsToll'));
    tollValueInput = element(by.id('field_tollValue'));
    createdAtInput = element(by.id('field_createdAt'));
    roadTypeSelect = element(by.id('field_roadType'));
    vehicleTypeSelect = element(by.id('field_vehicleType'));

    getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    setPavedHighwayAmountInput(pavedHighwayAmount): promise.Promise<void> {
        return this.pavedHighwayAmountInput.sendKeys(pavedHighwayAmount);
    }

    getPavedHighwayAmountInput() {
        return this.pavedHighwayAmountInput.getAttribute('value');
    }

    setNonPavedHighwayAmountInput(nonPavedHighwayAmount): promise.Promise<void> {
        return this.nonPavedHighwayAmountInput.sendKeys(nonPavedHighwayAmount);
    }

    getNonPavedHighwayAmountInput() {
        return this.nonPavedHighwayAmountInput.getAttribute('value');
    }

    getContainsTollInput() {
        return this.containsTollInput;
    }
    setTollValueInput(tollValue): promise.Promise<void> {
        return this.tollValueInput.sendKeys(tollValue);
    }

    getTollValueInput() {
        return this.tollValueInput.getAttribute('value');
    }

    setCreatedAtInput(createdAt): promise.Promise<void> {
        return this.createdAtInput.sendKeys(createdAt);
    }

    getCreatedAtInput() {
        return this.createdAtInput.getAttribute('value');
    }

    roadTypeSelectLastOption(): promise.Promise<void> {
        return this.roadTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    roadTypeSelectOption(option): promise.Promise<void> {
        return this.roadTypeSelect.sendKeys(option);
    }

    getRoadTypeSelect(): ElementFinder {
        return this.roadTypeSelect;
    }

    getRoadTypeSelectedOption() {
        return this.roadTypeSelect.element(by.css('option:checked')).getText();
    }

    vehicleTypeSelectLastOption(): promise.Promise<void> {
        return this.vehicleTypeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    vehicleTypeSelectOption(option): promise.Promise<void> {
        return this.vehicleTypeSelect.sendKeys(option);
    }

    getVehicleTypeSelect(): ElementFinder {
        return this.vehicleTypeSelect;
    }

    getVehicleTypeSelectedOption() {
        return this.vehicleTypeSelect.element(by.css('option:checked')).getText();
    }

    save(): promise.Promise<void> {
        return this.saveButton.click();
    }

    cancel(): promise.Promise<void> {
        return this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}
