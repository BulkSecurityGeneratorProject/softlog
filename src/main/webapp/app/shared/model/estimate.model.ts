import { Moment } from 'moment';

export interface IEstimate {
    id?: number;
    pavedHighwayAmount?: number;
    nonPavedHighwayAmount?: number;
    loadAmount?: number;
    containsToll?: boolean;
    tollValue?: number;
    createdAt?: Moment;
    vehicleTypeId?: number;
    ownerId?: Number;
    ownerName?: string;
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
        public vehicleTypeId?: number,
        public ownerId?: Number,
        public ownerName?: string
    ) {
        this.containsToll = false;
    }
}
