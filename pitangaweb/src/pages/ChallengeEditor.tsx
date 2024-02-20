import { Editor } from '../components/editor';
import { EditorConfigContext, defaultEditorConfig } from '../components/editor/editor-config.context';
import { TestsView } from '../components/tests-view';
import { plainToInstance } from 'class-transformer';
import { Challenge } from '../domain/problem';

const challenge = plainToInstance(Challenge, {
  validations: [
    { title: 'teste 1', status: 'OK', description: 'TESTE NUMERO 1' }
  ]
});

export const ChallengeEditor = () => (
  <div>
    <EditorConfigContext.Provider value={defaultEditorConfig}>
      <Editor />
    </EditorConfigContext.Provider>
    <TestsView tests={challenge.validations}/>
  </div>
);
