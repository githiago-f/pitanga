import { useState } from "react";
import { ValidationResult, ValidationStatus } from "../../../domain/problem/solution";

const FAIL_STYLE = 'border-red-800 bg-red-500 text-white';
const SUCCESS_STYLE = 'border-lime-800 bg-lime-500 text-lime-50';

const getStyle = (status: ValidationStatus) => status === ValidationStatus.PASS ? 
  SUCCESS_STYLE : 
  FAIL_STYLE;

export const ValidationItem = (props: ValidationResult) => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <li 
      onClick={() => setIsOpen(!isOpen)} 
      className={'border rounded p-1 ' + getStyle(props.status)}
    >
      Input: {props.input}
      <pre hidden={!isOpen} className="border border-gray-700 bg-gray-700 rounded-sm p-2">
        {props.output}
      </pre>
    </li>
  );
};
