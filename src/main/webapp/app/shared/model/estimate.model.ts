import { Moment } from 'moment';

export interface IEstimate {
    id?: number;
    pavedHighwayAmount?: number;
    nonPavedHighwayAmount?: number;
    loadAmount?: number;
    containsToll?: boolean;
    tollValue?: number;
    createdAt?: Moment;
    roadTypeId?: number;
    vehicleTypeId?: number;
}

export class Estimate implements IEstimate {
    constructor(
        public id?: number,
        public pavedHighwayAmount?: number,
        public nonPavedHighwayAmount?: number,
        public loadAmount?: number,
        public containsToll?: boolean,
        public tollValue?: number,
        public createdAt?: Moment,
        public roadTypeId?: number,
        public vehicleTypeId?: number
    ) {
        this.containsToll = false;
    }
}
