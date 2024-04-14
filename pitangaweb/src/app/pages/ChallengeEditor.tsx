import { Editor, EditorConfigContext, defaultEditorConfig } from '../components/editor';
import { Challenge } from '../../domain/problem';
import { useLoaderData, useNavigation } from 'react-router-dom';
import { Solution } from '../../domain/problem/solution';
import { ValidationContainer } from '../components/validation';
import { ToolTray } from '../components/tool-tray';
import { useCallback, useState } from 'react';
import { saveSolution } from '../../infra/data/pitanga.rest';
import { DescriptionModal } from '../components/description-modal';

export const ChallengeEditor = () => {
  const navigation = useNavigation();
  const {challenge, solution: currentSolution} = useLoaderData() as { 
    challenge: Challenge, 
    solution?: Solution
  };
  const [solution, setSolution] = useState(currentSolution);
  const [code, setCode] = useState(solution?.code ?? challenge.baseCode);
  const [viewDescription, setViewDescription] = useState(false);

  if(navigation.state === 'loading') {
    return (
      <span className="relative flex h-3 w-3">
        <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-sky-400 opacity-75"></span>
        <span className="relative inline-flex rounded-full h-3 w-3 bg-sky-500"></span>
      </span>
    )
  }

  const executeCode = useCallback(async () => {
    const sol = await saveSolution({ 
      language: 'java', 
      code: code, 
      challengeId: challenge.id
    });
    setSolution(sol);
  }, [code, challenge]);

  return (
    <>
      <ToolTray
        title={challenge.title}
        onSave={executeCode}
        onClickViewDoc={() => setViewDescription(!viewDescription)}
        solutionCodeChanged={code !== solution?.code}
      />
      <DescriptionModal
        show={viewDescription}
        onClose={() => setViewDescription(false)}
        title={challenge.title}
        description={challenge.description}
      />
      <EditorConfigContext.Provider value={defaultEditorConfig}>
        <Editor customContent={code} onChangeCode={setCode}/>
      </EditorConfigContext.Provider>
      <ValidationContainer
        validations={challenge.validationResults} 
        results={solution?.validationResults}
      />
    </>
  );
}
