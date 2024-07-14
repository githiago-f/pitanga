import AceEditor from 'react-ace';

import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/theme-chaos';
import 'ace-builds/src-noconflict/ext-language_tools';
import { defaultEditorConfig } from './editor-config.context';
import { Tooltip } from '../tooltip';
import { useState } from 'react';

type Props = {
  baseCode: string;
  setBaseCode: (val: string) => void
};

const Editor = ({ baseCode, setBaseCode }: Props) => (
  <AceEditor
    mode="java"
    theme="chaos"
    name="code-editor"
    fontSize={defaultEditorConfig.fontSize}
    defaultValue={baseCode}
    editorProps={{ $blockScrolling: true }}
    height="calc(100vh - 5rem)"
    scrollMargin={[0, screen.height > 672 ? 672 : screen.height, 0, 50]}
    width={`calc(${screen.width > 672 ? 672 : screen.width}px - 1.5rem)`}
    enableBasicAutocompletion={true}
    onChange={(value) => setBaseCode(value)}
  />
);

export const ChallengeEditor = (props: Props) => {
  const [isOpen, setIsOpen] = useState(true);

  return (
    <div>
      <div onClick={() => setIsOpen(!isOpen)} className='flex items-center'>
        <h3 className='p-2 text-lg font-bold'>Código base</h3>
        <Tooltip message="Não altere o nome da classe publica, ela será a classe main."/>
      </div>
      <hr/>
      <div className='transition-height' hidden={!isOpen}>
        <Editor
          baseCode={props.baseCode}
          setBaseCode={props.setBaseCode}
        />
      </div>
    </div>
  );
};
