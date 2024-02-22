import './style.css';
import { TestItem } from './test-item';

type Props = {
  tests?: any[];
};

export const ValidationContainer = (props: Props) => {
  return (
    <div className='validation-container'>
      <ol>
        <li><h3>Tests</h3></li>
        {props.tests?.map((test) => (
          <TestItem {...test} />
        ))}
      </ol>
    </div>
  );
};
