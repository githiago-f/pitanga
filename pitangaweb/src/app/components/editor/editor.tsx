import { useContext } from 'react';
import AceEditor from 'react-ace';

import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/theme-chaos';
import 'ace-builds/src-noconflict/ext-language_tools';

import { EditorConfigContext } from './editor-config.context';

type Props = {
  customContent?: string;
  onChangeCode: (code: string) => void
}

export const Editor = ({ customContent, onChangeCode }: Props) => {
  const context = useContext(EditorConfigContext);
  return (
    <AceEditor
      mode="java"
      theme="chaos"
      name="code-editor"
      fontSize={context.fontSize}
      defaultValue={customContent ?? context.fileContent}
      editorProps={{ $blockScrolling: true }}
      height={context.heigth()}
      width={context.width}
      onChange={(value) => onChangeCode(value)}
    />
  );
};
