
type Props = {
  onClick: () => void
}

export const ViewDoc = (props: Props) => (
  <button
    onClick={props.onClick}
    className='p-2 rounded w-full text-center relative shadow-md hover:shadow-sm border-1 border-gray-200 bg-rose-500 hover:bg-rose-700 active:bg-rose-900'>
    <img src="/pitanga-tcc/view-doc.svg" className="h-5 w-full text-center" alt="Ver descrição" />
    Descrição
  </button>
);
