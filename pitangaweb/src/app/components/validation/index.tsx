import { ValidationResult } from '../../../domain/problem/solution';
import './style.css';
import { ValidationItem } from './validation-item';

type Props = {
  validations?: ValidationResult[];
};

export const ValidationContainer = (props: Props) => {
  return (
    <div className='validation-container'>
      <ol>
        <li><h3>Validation results</h3></li>
        {props.validations?.map((test, i) => (
          <ValidationItem key={i} {...test} />
        ))}
      </ol>
    </div>
  );
};
