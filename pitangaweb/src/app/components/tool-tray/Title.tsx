export const Title = (props: { title: string; onClick?: () => void; }) => (
  <h2
    onClick={props.onClick}
    className='text-xl max-h-7 font-bold text-ellipsis overflow-hidden text-nowrap'>
    {props.title}
  </h2>
);
