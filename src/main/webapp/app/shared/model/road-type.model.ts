export interface IRoadType {
    id?: number;
    name?: string;
    factor?: number;
}

export class RoadType implements IRoadType {
    constructor(public id?: number, public name?: string, public factor?: number) {}
}
