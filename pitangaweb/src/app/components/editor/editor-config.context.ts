import { createContext } from 'react';

export const defaultEditorConfig = {
  fontSize: 18,
  fileContent: 'public class Solution {\n\tpublic static void main(String[] args) ' +
    '{\n\t\t// Sua solução aqui\n\t}\n}\n',
  width: '100vw',
  heigth: () => {
    const tray = document.getElementById("tooltray");
    const height = (tray?.style.height ?? '52px');
    return `calc(100vh - ${height})`;
  }
};
export const EditorConfigContext = createContext(defaultEditorConfig);
