
type Props = {
  onClick: () => void
}

export const ViewDoc = (props: Props) => (
  <button onClick={props.onClick} className='p-2 rounded w-full text-center relative'>
    <img src="view-doc.svg" className="h-5 w-full text-center" alt="Ver documentação" />
  </button>
);
