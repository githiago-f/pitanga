import { plainToInstance } from 'class-transformer';
import { Challenge, Validation } from './challenge';

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
  readonly status!: ValidationStatus | null;
  readonly validation!: ValidationResultId;

  public static fromValidation(validation: Validation) {
    return plainToInstance(ValidationResult, {
      expectedOutput: validation.expectedOutput,
      input: validation.testInput,
      output: '',
      status: null,
    } as ValidationResult)
  }
}

export class Solution {
  public readonly code!: string;
  public readonly sollutionId!: SolutionId;

  public readonly challenge!: Challenge;
  public readonly validationResults!: ValidationResult[];
}
