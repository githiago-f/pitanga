
type Props = {
  onClick: () => void
}

export const ViewDoc = (props: Props) => (
  <button
    onClick={props.onClick}
    className='p-2 rounded w-full text-center relative shadow-md border-1 border-gray-200'>
    <img src="/pitanga-tcc/view-doc.svg" className="h-5 w-full text-center" alt="Ver descrição" />
    Descrição
  </button>
);
