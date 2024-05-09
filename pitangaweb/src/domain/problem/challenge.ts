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

export enum ChallengeLevel {

}

export class Challenge {
  public readonly id!: string;
  public readonly title!: string;
  public readonly description!: string;
  public readonly level!: ChallengeLevel;
  public readonly baseCode!: string;

  public readonly validations!: Validation[];

  get validationResults() {
    return this.validations.map(ValidationResult.fromValidation);
  }
}

export enum SolutionStatus { SOLVED, NOT_STARTED, STARTED }

export class ChallengeListItem {
  public readonly id!: string;
  public readonly title!: string;
  public readonly level!: ChallengeLevel;
  public readonly status!: SolutionStatus;

  get link() {
    return `/challenge/${this.id}`;
  }
}
