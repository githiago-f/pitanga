import { plainToInstance } from 'class-transformer';

import { Challenge } from '../../domain/problem';

import { ValidationContainer } from '../components/validation';
import { Editor, EditorConfigContext, defaultEditorConfig } from '../components/editor';

const challenge = plainToInstance(Challenge, {
  id: 'uuid-challenge',
  title: 'Find the summation',
  description: '',
  validations: [
    { input: '1 2', output: '3', resultStatus: 'void' },
    { input: '3 2', output: '5', resultStatus: 'void' },
    { input: '2 3', output: '5', resultStatus: 'void' },
    { input: '1 1', output: '2', resultStatus: 'void' }
  ]
});

export const ChallengeEditor = () => (
  <>
    <EditorConfigContext.Provider value={defaultEditorConfig}>
      <Editor />
    </EditorConfigContext.Provider>
    <ValidationContainer tests={challenge.validations}/>
  </>
);
