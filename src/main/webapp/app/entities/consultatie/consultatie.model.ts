import * as dayjs from 'dayjs';
import { IMedic } from 'app/entities/medic/medic.model';
import { IClient } from 'app/entities/client/client.model';

export interface IConsultatie {
  id?: number;
  dataOra?: dayjs.Dayjs;
  descriere?: string;
  medic?: IMedic | null;
  client?: IClient | null;
}

export class Consultatie implements IConsultatie {
  constructor(
    public id?: number,
    public dataOra?: dayjs.Dayjs,
    public descriere?: string,
    public medic?: IMedic | null,
    public client?: IClient | null
  ) {}
}

export function getConsultatieIdentifier(consultatie: IConsultatie): number | undefined {
  return consultatie.id;
}
