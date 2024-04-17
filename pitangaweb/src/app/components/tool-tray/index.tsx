import { useState } from 'react';
import { Link } from 'react-router-dom';
import { SaveButton } from './SaveButton';
import { Title } from './Title';
import { ViewDoc } from './ViewDoc';

type Props = {
  title: string;
  onSave: () => Promise<void>;
  solutionCodeChanged: boolean;
  onClickViewDoc: () => void;
};

export const ToolTray = (props: Props) => {
  const [isSaving, setIsSaving] = useState(false);

  const saveCode = () => {
    setIsSaving(true);
    props.onSave().then(() => {
      setIsSaving(false);
    });
  };

  return (
    <nav id="tooltray" className="w-full p-2">
      <div className="grid grid-cols-6">
        <Link
          to={'/'}
          className="col-span-1 p-1 text-cyan-500 text-center gap-1">Voltar</Link>
        <div className="col-span-3 p-1">
          <Title title={props.title} />
        </div>
        <div className="col-span-1">
          <ViewDoc onClick={props.onClickViewDoc} />
        </div>
        <div className="col-span-1">
          <SaveButton
            shouldSave={props.solutionCodeChanged}
            isSaving={isSaving}
            saveCode={saveCode}
          />
        </div>
      </div>
    </nav>
  );
};
