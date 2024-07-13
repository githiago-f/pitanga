import { Challenge } from '../../domain/problem';
import { useLoaderData } from 'react-router-dom';
import { Solution } from '../../domain/problem/solution';
import { ValidationContainer } from '../components/validation';
import { ToolTray } from '../components/tool-tray';
import { useCallback, useEffect, useRef, useState } from 'react';
import { saveSolution } from '../../infra/data/pitanga.rest';
import { DescriptionModal } from '../components/description-modal';
import { EditorConfigContext, defaultEditorConfig } from '../components/editor/editor-config.context';
import { Editor } from '../components/editor/editor';
import { debounce } from '../../infra/utils/throttle';

export const ChallengeEditor = () => {
  const {challenge, solution: currentSolution} = useLoaderData() as {
    challenge: Challenge,
    solution?: Solution
  };
  const [isSaving, setIsSaving] = useState(false);
  const [solution, setSolution] = useState(currentSolution);
  const [code, setCode] = useState(solution?.code ?? challenge.baseCode);
  const [viewDescription, setViewDescription] = useState(false);

  const executeCodeListener = useRef(debounce((code: string) => {
    const request = {
      language: 'java',
      code,
      challengeId: challenge.id
    };
    saveSolution(request).then(sol => {
      setSolution(sol);
      setIsSaving(false);
    });
  }));

  useEffect(() => {
    document.title = 'Pitanga | ' + challenge.title;
  }, [challenge]);

  const persistCode = async (code: string) => {
    setCode(code);
    setIsSaving(true);
    executeCodeListener.current(code);
  };

  return (
    <>
      <ToolTray
        title={challenge.title}
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
        <Editor customContent={code} onChangeCode={persistCode}/>
      </EditorConfigContext.Provider>
      <ValidationContainer
        validations={challenge.validationResults}
        results={solution?.validationResults}
      />
    </>
  );
};
