import { Validation } from './validation';

export class Challenge {
  public readonly id!: string;
  public readonly title!: string;
  public readonly description!: string;

  public readonly validations?: Validation[];
}
