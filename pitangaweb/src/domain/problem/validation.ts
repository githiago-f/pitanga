export enum ValidationStatus {
  OK = 'Ok',
  FAIL = 'Fail',
  NONE = 'None'
}

export class Validation {
  public readonly title: string;
  public readonly description: string;
  public readonly status: ValidationStatus;

  public readonly input: string;
  public readonly output: string;
}
