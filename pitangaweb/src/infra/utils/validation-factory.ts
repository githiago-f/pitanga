export type ValidationFormField = {
  [key: `input-${number}`]: HTMLTextAreaElement;
  [key: `output-${number}`]: HTMLTextAreaElement;
}

export const validationFactory = (elements: ValidationFormField): { input: string; output: string; }[] => {
  const inputKeys = Object.keys(elements)
    .filter(i => i.startsWith('input') || i.startsWith('output')) as
        (`input-${number}` | `output-${number}`)[];

  const validations = new Map<number, {input: string, output: string}>();
  for (const key of inputKeys) {
    const [inputType, index] = key.split('-') as ['input'|'output', string];
    const value = validations.get(Number(index)) ?? {} as {input: string, output: string};
    value[inputType] = elements[key].value;
    validations.set(Number(index), value);
  }
  return new Array(...validations.values());
};
