import { Challenge } from '../../domain/problem';
import { plainToInstance } from 'class-transformer';
import { apiBase } from './base';
import { Params, redirect } from 'react-router-dom';
import { Solution } from '../../domain/problem/solution';

export async function listChallenges() {
    const challengesRaw = await apiBase.get<Challenge[]>('/challenges');
    return challengesRaw.data.map(c => plainToInstance(Challenge, c));
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
