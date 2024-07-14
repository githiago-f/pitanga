export const ShowVallidationsButton = (props: { onClick: () => void; show: boolean; }) => (
  <button
    title={props.show ? 'Fechar' : 'Abrir'}
    className='bg-white rounded-xl rounded-b-none p-3'
    onClick={props.onClick}
  >
    {!props.show ?
      <img src="/pitanga-tcc/chevron-up.svg" className='h-6'/> :
      <img src="/pitanga-tcc/chevron-down.svg" className='h-6'/>}
  </button>
);
