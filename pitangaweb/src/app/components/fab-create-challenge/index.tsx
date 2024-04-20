import { Link } from 'react-router-dom';

export const FabCreateChallenge = () => (
  <Link
    className='p-3 rounded-full bg-lime-600 fixed right-0 bottom-0 mb-4 mr-4'
    to={'/create-challenge'}>
    <img className='h-8 w-8' src='/pitanga-tcc/plus.svg'/>
  </Link>
);
