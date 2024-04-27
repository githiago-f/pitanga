import './style.css';

export const Tooltip = ({message}: { message: string; }) => (
  <span className='tooltip'>
    <img src='/pitanga-tcc/question.svg' className='h-4 w-4'/>
    <span className='tooltiptext'>{message}</span>
  </span>
);
