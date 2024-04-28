import { useState } from 'react';
import { ValidationEditorItem } from './validation-editor-item';

export const ValidationEditor = () => {
  const [validations, setValidations] = useState([] as {index:number}[]);

  const addValidaiton    = () =>
    setValidations((v) => [...v, { index: (v.at(-1)?.index??1) + 1 }]);
  const removeValidaiton = (index: number) =>
    setValidations((v) => [...v].filter(i => i.index !== index));

  return (
    <>
      <button
        className='p-3 bg-rose-500 rounded-lg border border-rose-700 text-white'
        onClick={addValidaiton}
        type="button"
      >Adicionar Validação</button>
      <div className='grid gap-1 lg:grid-cols-2 md:grid-cols-2 sm:grid-cols-2 grid-cols-1'>
        {validations.map((i) => (
          <ValidationEditorItem
            index={i.index}
            key={i.index}
            onRemove={removeValidaiton}
          />
        ))}
      </div>
    </>
  );
};
