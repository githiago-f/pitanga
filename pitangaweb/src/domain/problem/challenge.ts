import { Labels } from '../../app/assets/i18n';
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
  EASY = 'EASY',
  MEDIUM = 'MEDIUM',
  HARD = 'HARD',
  PRO = 'PRO'
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

export enum SolutionStatus {
  SOLVED = 'SOLVED',
  NOT_STARTED = 'NOT_STARTED',
  STARTED = 'STARTED'
}

export class ChallengeListItem {
  public readonly id!: string;
  public readonly title!: string;
  public readonly level!: ChallengeLevel;
  public readonly status!: SolutionStatus;

  get statusLabel(): string {
    return Labels.challenge.list.item.status[this.status];
  }

  get link() {
    return `/challenge/${this.id}`;
  }
}
