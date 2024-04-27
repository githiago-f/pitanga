import { FC, useCallback, useState } from 'react';

type ItemProps = {
  index: number;
  onRemove: (index: number) => void;
}

const ValidationEditorItem: FC<ItemProps> = (props) => (
  <div className='grid grid-cols-5 p-2 border border-slate-800 rounded-lg bg-slate-100 '>
    <button
      className='p-3 text-center justify-center items-center flex'
      onClick={() => props.onRemove(props.index)}
      type="button"
    >
      <img src='/pitanga-tcc/close.svg' className='h-4 w-4' />
    </button>
    <div className='flex flex-col col-span-4'>
      <textarea
        className='p-1 rounded border'
        rows={1}
        required
        minLength={1}
        name={'input-' + props.index}
        id={'input-' + props.index}
        placeholder="Input de dados"
      />
      <textarea
        className='p-1 rounded border'
        rows={1}
        required
        minLength={1}
        name={'output-' + props.index}
        id={'output-' + props.index}
        placeholder="Output esperado"
      />
    </div>
  </div>
);

type Validation = {input: string; expectedOutput: string; index: number;};

export const ValidationEditor = () => {
  const [validations, setValidations] = useState([] as Validation[]);

  const addValidaiton = useCallback(() => {
    const vals = [...validations];
    const index = (vals[vals.length - 1]?.index  ?? 1) + 1;
    vals.push({ input: '', expectedOutput: '', index });
    setValidations(vals);
  }, [validations]);

  const removeValidaiton = useCallback((index: number) => {
    const vals = [...validations].filter(i => i.index !== index);
    setValidations(vals);
  }, [validations]);

  return (
    <>
      <button onClick={addValidaiton} type="button">Adicionar Validação</button>
      <div className='grid gap-1 lg:grid-cols-4 md:grid-cols-3 sm:grid-cols-2 grid-cols-1'>
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
