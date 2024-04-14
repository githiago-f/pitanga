import { useState } from 'react';
import { ValidationResult } from '../../../domain/problem/solution';
import './style.css';
import { ValidationItem } from './validation-item';

type Props = {
  results?: ValidationResult[];
  validations: ValidationResult[];
};

export const ValidationContainer = (props: Props) => {
  const [show, setShow] = useState(false);
  return (
    <div className={'validation-container ' + (show ? 'bottom-0' : '-bottom-36') }>
      <button
        className='bg-white rounded-xl rounded-b-none p-3' 
        onClick={() => setShow(!show)}
      >
        {!show ? 
          <img src="chevron-up.svg" className='h-6'/> : 
          <img src="chevron-down.svg" className='h-6'/>}
      </button>
      <ol>
        <li className='py-2 text-center font-bold'><h3>Testes</h3></li>
        {(props.results ?? props.validations).map((test, i) => (
          <ValidationItem key={i} {...test} />
        ))}
      </ol>
    </div>
  );
};
