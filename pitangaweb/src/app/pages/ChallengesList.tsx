import { useEffect } from 'react';
import { ChallengeListItem } from '../../domain/problem';
import { ChallengeItem } from '../components/challenge/item';
import { useLoaderData } from 'react-router-dom';
import { FabCreateChallenge } from '../components/fab-create-challenge';
import { ListFallback } from '../components/fallback';
import { Labels } from '../assets/i18n';

export const ChallengesList = () => {
  const challenges = useLoaderData() as ChallengeListItem[];

  useEffect(() => {
    document.title = 'Pitanga';
  }, []);

  return (
    <div className='px-2'>
      <ListFallback itens={challenges} fallbackText={Labels.challenge.list.fallback}>
        {challenge => <ChallengeItem key={challenge.id} challenge={challenge} />}
      </ListFallback>
      <FabCreateChallenge />
    </div>
  );
};
