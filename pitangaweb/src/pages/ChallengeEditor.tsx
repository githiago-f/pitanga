import { Editor } from '../components/editor';
import { EditorConfigContext, defaultEditorConfig } from '../components/editor/editor-config.context';
import { TestsView } from '../components/tests-view';

export const ChallengeEditor = () => (
  <div>
    <EditorConfigContext.Provider value={defaultEditorConfig}>
      <Editor />
    </EditorConfigContext.Provider>
    <TestsView />
  </div>
);
