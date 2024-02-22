import { plainToInstance } from 'class-transformer';

import { Challenge } from '../domain/problem';

import { ValidationContainer } from '../components/validation';
import { Editor, EditorConfigContext, defaultEditorConfig } from '../components/editor';

const challenge = plainToInstance(Challenge, {});

export const ChallengeEditor = () => (
  <>
    <EditorConfigContext.Provider value={defaultEditorConfig}>
      <Editor />
    </EditorConfigContext.Provider>
    <ValidationContainer tests={challenge.validations}/>
  </>
);
