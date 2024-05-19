export const Title = (props: { title: string; }) => (
  <h2 className='text-xl max-h-6 font-bold text-ellipsis overflow-hidden text-nowrap'>
    {props.title}
  </h2>
);
