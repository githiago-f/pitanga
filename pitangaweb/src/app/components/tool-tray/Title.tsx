export const Title = (props: { title: string; }) => (
  <h2 className='text-xl font-bold text-ellipsis overflow-hidden'>
    {props.title}
  </h2>
)