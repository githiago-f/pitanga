import { useEffect } from 'react';
import { Challenge } from '../../domain/problem';
import { ChallengeItem } from '../components/challenge/item';
import { useLoaderData } from 'react-router-dom';
import { FabCreateChallenge } from '../components/fab-create-challenge';

export const ChallengesList = () => {
  const challenges = useLoaderData() as Challenge[];

  useEffect(() => {
    document.title = 'Pitanga';
  }, []);

  return (
    <div>
      {challenges?.map(challenge =>
        <ChallengeItem key={challenge.id} challenge={challenge} />
      )}
      <FabCreateChallenge />
    </div>
  );
};
