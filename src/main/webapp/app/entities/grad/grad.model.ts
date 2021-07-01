export interface IGrad {
  id?: number;
  denumire?: string;
}

export class Grad implements IGrad {
  constructor(public id?: number, public denumire?: string) {}
}

export function getGradIdentifier(grad: IGrad): number | undefined {
  return grad.id;
}
