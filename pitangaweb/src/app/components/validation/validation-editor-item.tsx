import { FC } from 'react';

type Props = {
  index: number;
  onRemove: (index: number) => void;
}

export const ValidationEditorItem: FC<Props> = (props) => (
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
