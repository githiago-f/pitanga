import { FormEvent, useCallback, useEffect } from 'react';
import { Link } from 'react-router-dom';

export const CreateChallenge = () => {
  useEffect(() => {
    document.title = 'Pitanga | Criar desafio';
  }, []);

  const onSubmitForm = useCallback((event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
  }, []);

  return (
    <>
      <nav className="w-full p-2">
        <div className="grid grid-cols-6">
          <Link
            to={'/'}
            className="col-span-1 flex p-2 text-cyan-500 text-center gap-1">
            <img className='h-5 w-full' src='/pitanga-tcc/back.svg' alt='voltar'/>
          </Link>
          <h2 className='col-span-5 text-xl font-bold text-ellipsis overflow-hidden'>
            Criar desafio
          </h2>
        </div>
      </nav>
      <form className='p-3 space-y-3' onSubmit={onSubmitForm}>
        <input
          className='p-2 rounded-md text-lg border border-slate-800 w-full'
          type="text"
          name="title"
          id="title"
          placeholder='Titulo'
          aria-label='Titulo do desafio'
        />
        <textarea
          className='p-2 rounded-md text-lg border border-slate-800 w-full'
          name="description"
          id="description"
          placeholder='Descrição'
          aria-label='Descrição do desafio'
          cols={30}
          rows={10}
        ></textarea>
        <div className='space-x-2'>
          <button className='border rounded border-neutral-800 p-3 bg-slate-500 text-white' type='button'>Adicionar código</button>
          <button className='border rounded border-neutral-800 p-3 bg-slate-500 text-white' type='submit'>Enviar</button>
        </div>
      </form>
    </>
  );
};
