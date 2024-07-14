type Props = {
  isSaving: boolean;
  saveCode: () => void;
  shouldSave: boolean;
};

export const SaveButton = (props: Props) => (
  <button
    title={props.isSaving ? 'Salvando' : 'Salvar e testar'}
    disabled={props.isSaving}
    className='bg-green-600 min-w-12 min-h-12 relative p-3 rounded-full mb-3 text-center disabled:bg-slate-500'
    onClick={props.saveCode}
  >
    <span hidden={!props.shouldSave} className="absolute -top-0 -left-0">
      <span className="relative flex h-3 w-3">
        <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-500 opacity-75"></span>
        <span className="relative inline-flex rounded-full h-3 w-3 bg-red-600"></span>
      </span>
    </span>
    <img src="/pitanga-tcc/save.svg" alt="Salvar" className="h-5 w-full text-center"/>
  </button>
);
