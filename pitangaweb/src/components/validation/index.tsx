import './style.css';
import { ValidationItem } from './test-item';

type Props = {
  tests?: any[];
};

export const ValidationContainer = (props: Props) => {
  return (
    <div className='validation-container'>
      <ol>
        <li><h3>Tests</h3></li>
        {props.tests?.map((test) => (
          <ValidationItem {...test} />
        ))}
      </ol>
    </div>
  );
};
