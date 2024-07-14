import { createContext } from 'react';

export const defaultEditorConfig = {
  fontSize: 18,
  fileContent: 'public class Solution {\n\tpublic static void main(String[] args) ' +
    '{\n\t\t// Sua solução aqui\n\t}\n}\n',
  width: '100vw',
  heigth: () => {
    const tray = document.getElementById('tooltray');
    const height = (tray?.style.height ?? '76px');
    return `calc(100vh - ${height})`;
  },
  scrollMargin: () => {
    const tray = document.getElementById('tooltray');
    const height = tray?.style.height ? parseInt(tray.style.height.replace('px', '')) : 76;
    return window.outerHeight - height;
  }
};
export const EditorConfigContext = createContext(defaultEditorConfig);
