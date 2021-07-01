export interface ISpecializare {
  id?: number;
  denumire?: string;
}

export class Specializare implements ISpecializare {
  constructor(public id?: number, public denumire?: string) {}
}

export function getSpecializareIdentifier(specializare: ISpecializare): number | undefined {
  return specializare.id;
}
