import { Validation } from '../../domain/problem';

export const ValidationItem = (props: Validation) => (
  <li><b>{props.input}</b> {props.resultStatus}</li>
);
