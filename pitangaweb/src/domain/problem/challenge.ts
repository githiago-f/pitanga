import { ValidationResult } from './solution';

export class ValidationId {
  readonly id!: number;
  readonly challengeId!: string;
}

export class Validation {
  readonly id!: ValidationId;
  readonly testInput!: string;
  readonly expectedOutput!: string;
}

export class Challenge {
  public readonly id!: string;
  public readonly title!: string;
  public readonly description!: string;
  public readonly baseCode!: string;

  public readonly validations!: Validation[];

  get link() {
    return `/challenge/${this.id}`;
  }

  get validationResults() {
    return this.validations.map(ValidationResult.fromValidation);
  }
}
