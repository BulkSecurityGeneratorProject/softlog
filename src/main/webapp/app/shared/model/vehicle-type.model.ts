export interface IVehicleType {
    id?: number;
    name?: string;
    factor?: number;
    regularLoad?: number;
    maximumLoad?: number;
    vehicleTypeName?: string;
}

export class VehicleType implements IVehicleType {
    constructor(
        public id?: number,
        public name?: string,
        public factor?: number,
        public regularLoad?: number,
        public maximumLoad?: number,
        public vehicleTypeName?: string
    ) {}
}
