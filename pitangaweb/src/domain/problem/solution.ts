import { Challenge } from './challenge';

export enum SolutionStatus {
  OK = 'Ok'
}

export class Solution {
  public readonly id: string;
  public readonly version: number;
  public readonly code: string;
  public readonly language: string;

  public readonly challenge: Challenge;

  public get status(): SolutionStatus {
    
  }
}
