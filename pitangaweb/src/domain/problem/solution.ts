import { Challenge } from './challenge';

export class SolutionId {
  readonly hash!: string;
  readonly challengeId!: string;
  readonly submitterId!: number;
}

export enum ValidationStatus {
  PASS = "PASS",
  FAIL = "FAIL"
}

export class ValidationResultId {
  readonly challengeId!: string
  readonly id!: number;
}

export class ValidationResult {
  readonly expectedOutput!: string;
  readonly input!: string;
  readonly output!: string;
  readonly status!: ValidationStatus;
  readonly validation!: ValidationResultId;
}

export class Solution {
  public readonly code!: string;
  public readonly sollutionId!: SolutionId;

  public readonly challenge!: Challenge;
  public readonly validationResults!: ValidationResult[];
}
