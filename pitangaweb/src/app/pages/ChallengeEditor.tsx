import { Editor, EditorConfigContext, defaultEditorConfig } from '../components/editor';
import { Challenge } from '../../domain/problem';
import { useLoaderData } from 'react-router-dom';
import { Solution } from '../../domain/problem/solution';
import { ValidationContainer } from '../components/validation';

export const ChallengeEditor = () => {
  const {challenge, solution} = useLoaderData() as { 
    challenge: Challenge, 
    solution?: Solution
  };
  return (
    <>
      <EditorConfigContext.Provider value={defaultEditorConfig}>
        <Editor customContent={solution?.code ?? challenge.baseCode}/>
      </EditorConfigContext.Provider>
      <ValidationContainer validations={solution?.validationResults}/>
    </>
  );
}
