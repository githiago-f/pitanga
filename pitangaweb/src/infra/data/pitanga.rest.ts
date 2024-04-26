import { Challenge } from '../../domain/problem';
import { plainToInstance } from 'class-transformer';
import { apiBase } from './base';
import { Params, redirect } from 'react-router-dom';
import { Solution } from '../../domain/problem/solution';
import { Page } from './page.dto';

export async function listChallenges() {
  const challengesRaw = await apiBase.get<Page<Challenge[]>>('/challenges');
  if(!challengesRaw.data?.content) {
    throw new Error('Could not load page');
  }
  return challengesRaw.data?.content.map(c => plainToInstance(Challenge, c));
}

export async function getChallenge({ params }: {params: Params}) {
  const url = `/challenges/${params.challengeId}`;
  const challengeRaw = await apiBase.get<Challenge>(url);
  if(challengeRaw.status === 404) {
    return redirect('/?error=Challenge not found');
  }
  const solutionRaw = await apiBase.get<Solution>(url + '/solutions');
  const result = {
    challenge: plainToInstance(Challenge, challengeRaw.data),
    solution: undefined
  } as { challenge: Challenge, solution?: Solution };
  if(solutionRaw.status === 200) {
    result.solution = plainToInstance(Solution, solutionRaw.data);
  }
  return result;
}

type ChallengeSaveCommand = {
  title: string;
  description: string;
  baseCode: string;
  validations: { input: string; output: string; }[];
};

export async function saveChallenge(props: ChallengeSaveCommand) {
  const res = await apiBase.post('/challenges', {...props, creatorId: 2});
  console.log(res.data);
}

type SaveCommand = { code: string, language: string, challengeId: string};

export async function saveSolution(params: SaveCommand) {
  const url = `/challenges/${params.challengeId}/solutions`;
  const newSolution = await apiBase.put(url, {
    language: params.language,
    code: params.code
  });
  if(newSolution.status !== 200) {
    // TODO
    return;
  }
  const solutionRaw = await apiBase.get<Solution>(url);
  return plainToInstance(Solution, solutionRaw.data);
}
