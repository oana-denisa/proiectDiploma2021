import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface IClient {
  id?: number;
  numeSiPrenume?: string;
  dataNastere?: dayjs.Dayjs | null;
  adresa?: string;
  telefon?: string;
  email?: string;
  user?: IUser | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public numeSiPrenume?: string,
    public dataNastere?: dayjs.Dayjs | null,
    public adresa?: string,
    public telefon?: string,
    public email?: string,
    public user?: IUser | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
