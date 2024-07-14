import { Link } from 'react-router-dom';
import { ChallengeListItem } from '../../../domain/problem';

type Props = {challenge: ChallengeListItem}

export const ChallengeItem = ({ challenge }: Props) => (
  <div className="border border-rose-300 shadow rounded-md p-4 max-w-2xl w-full mx-auto my-2">
    <div className="w-full grid grid-cols-7">
      <div className="rounded-full bg-rose-800 h-10 w-10 col-span-1"></div>
      <div className="space-y-3 grid grid-cols-2 sm:grid-cols-3 gap-4 col-span-6 pl-2">
        <div className='col-span-2'>
          <h4
            aria-label={`Titulo do desafio: ${challenge.title}`}
            className="rounded text-ellipsis overflow-hidden max-h-6">
            {challenge.title}
          </h4>
          <span
            aria-label={challenge.levelLabel}
            title={challenge.levelLabel}
            className='font-semibold text-xs text-slate-200 bg-green-600 p-1 rounded-full'
          >{challenge.levelLabel}</span>
        </div>

        <Link
          aria-label={challenge.statusLabel + ' ' + challenge.title}
          className="rounded bg-rose-500 text-white p-1 text-center col-span-1"
          to={challenge.link}>{challenge.statusLabel}</Link>
      </div>
    </div>
  </div>
);
