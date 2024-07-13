import { Link } from 'react-router-dom';
import { Title } from './Title';
import { ViewDoc } from './ViewDoc';

type Props = {
  title: string;
  solutionCodeChanged: boolean;
  onClickViewDoc: () => void;
};

export const ToolTray = (props: Props) => (
  <nav id="tooltray" className="w-full p-2">
    <div className="grid grid-cols-6">
      <Link
        to={'/'}
        className="col-span-1 p-2 text-cyan-500 text-center gap-1 flex justify-center items-center">
        <img className='h-5 w-full' src='/pitanga-tcc/back.svg' alt='voltar'/>
      </Link>
      <div className="col-span-3 p-1 flex items-center">
        <Title title={props.title} />
      </div>
      <div className="col-span-2">
        <ViewDoc onClick={props.onClickViewDoc} />
      </div>
    </div>
  </nav>
);
