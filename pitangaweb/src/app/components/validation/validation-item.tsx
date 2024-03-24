import { Validation } from '../../domain/problem';

export const ValidationItem = (props: Validation) => (
  <li>
    <table>
      <tbody>
        <td>{props.input}</td>
        <td>{props.output}</td>
        <td>{props.resultStatus}</td>
      </tbody>
    </table>
  </li>
);
