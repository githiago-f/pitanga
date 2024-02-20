import { createContext } from 'react';

export const defaultEditorConfig = {
  fontSize: 18,
  fileContent: 'public class Main {\n\tpublic static void main(String[] args) ' +
    '{\n\t\tSystem.out.println("Hello, World!");\n\t}\n}\n',
  width: '100vw',
  heigth: '100vh'
};
export const EditorConfigContext = createContext(defaultEditorConfig);
