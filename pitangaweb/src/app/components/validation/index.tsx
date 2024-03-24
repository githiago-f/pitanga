import './style.css';
import { ValidationItem } from './validation-item';

type Props = {
  tests?: any[];
};

export const ValidationContainer = (props: Props) => {
  return (
    <div className='validation-container'>
      <ol>
        <li><h3>Validation results</h3></li>
        {props.tests?.map((test) => (
          <ValidationItem {...test} />
        ))}
      </ol>
    </div>
  );
};
