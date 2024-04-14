import { Validation } from './validation';

export class Challenge {
  public readonly id!: string;
  public readonly title!: string;
  public readonly description!: string;
  public readonly baseCode!: string;

  public readonly validations?: Validation[];

  get link() {
    return `/${this.id}`;
  }
}
