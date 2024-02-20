import { Validation } from '../../domain/problem';

export const TestItem = (props: Validation) => (
  <li><b>{props.title}</b>&nbsp;{props.status}</li>
);
