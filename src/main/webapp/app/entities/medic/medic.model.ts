import { IUser } from 'app/entities/user/user.model';
import { IGrad } from 'app/entities/grad/grad.model';
import { ISpecializare } from 'app/entities/specializare/specializare.model';

export interface IMedic {
  id?: number;
  numeSiPrenume?: string;
  user?: IUser | null;
  grad?: IGrad | null;
  specializare?: ISpecializare | null;
}

export class Medic implements IMedic {
  constructor(
    public id?: number,
    public numeSiPrenume?: string,
    public user?: IUser | null,
    public grad?: IGrad | null,
    public specializare?: ISpecializare | null
  ) {}
}

export function getMedicIdentifier(medic: IMedic): number | undefined {
  return medic.id;
}
