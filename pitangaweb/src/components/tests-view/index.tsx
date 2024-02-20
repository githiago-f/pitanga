import './style.css';
import { TestItem } from './test-item';

type Props = {
  tests?: any[]
};

export const TestsView = (props: Props) => {
  return (
    <div className='test-container'>
      <ul>
        <li><h3>Tests</h3></li>
        {props.tests?.map((test) => (
          <TestItem {...test} />
        ))}
      </ul>
    </div>
  );
};
