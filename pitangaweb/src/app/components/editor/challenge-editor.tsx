import AceEditor from 'react-ace';

import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/theme-chaos';
import 'ace-builds/src-noconflict/ext-language_tools';
import { defaultEditorConfig } from './editor-config.context';

type Props = {
  baseCode: string;
  setBaseCode: (val: string) => void
};

export const ChallengeEditor = ({ baseCode, setBaseCode }: Props) => (
  <AceEditor
    mode="java"
    theme="chaos"
    name="code-editor"
    fontSize={defaultEditorConfig.fontSize}
    defaultValue={baseCode}
    editorProps={{ $blockScrolling: true }}
    height={`calc(${defaultEditorConfig.heigth()} - 5rem)`}
    width={`calc(${defaultEditorConfig.width} - 2.75rem)`}
    onChange={(value) => setBaseCode(value)}
  />
);
