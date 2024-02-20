import { useContext } from 'react';
import AceEditor from 'react-ace';

import 'ace-builds/src-noconflict/mode-java';
import 'ace-builds/src-noconflict/theme-chaos';
import 'ace-builds/src-noconflict/ext-language_tools';

import { EditorConfigContext } from './editor-config.context';

export const Editor = () => {
  const context = useContext(EditorConfigContext);
  return (
    <AceEditor
      mode="java"
      theme="chaos"
      name="code-editor"
      fontSize={context.fontSize}
      defaultValue={context.fileContent}
      editorProps={{ $blockScrolling: true }}
      height={context.heigth}
      width={context.width}
    />
  );
};
