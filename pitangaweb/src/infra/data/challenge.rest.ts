import { Challenge } from "../../domain/problem";
import { plainToInstance } from "class-transformer";
import { apiBase } from "./base";
import { Params } from "react-router-dom";
import { Solution } from "../../domain/problem/solution";

export async function listChallenges() {
    const challengesRaw = await apiBase.get<Challenge[]>('/challenges');
    return challengesRaw.data.map(c => plainToInstance(Challenge, c));
}

export async function getChallenge({ params }: {params: Params}) {
    const url = `/challenges/${params.challengeId}`;
    const challengeRaw = await apiBase.get<Challenge>(url);
    if(challengeRaw.status === 404) {
        // TODO THROW ERROR
    }
    const solutionRaw = await apiBase.get<Solution>(url + '/solutions');
    const result = {
        challenge: plainToInstance(Challenge, challengeRaw.data), 
        solution: undefined 
    } as { challenge: Challenge, solution?: Solution };
    if(solutionRaw.status === 200) {
        result.solution = plainToInstance(Solution, solutionRaw.data);
        console.log(result);
    }
    return result;
}
