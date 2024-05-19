import { ReactElement } from 'react';

type Props<T> = {
  itens: T[];
  children: (item: T) => ReactElement
  fallbackText?: string;
};

export function ListFallback<T>(props: Props<T>) {
  if(props.itens.length > 0)
    return props.itens.map(props.children);
  return (
    <h3 className='pt-3 text-center text-xl font-medium'>
      {props.fallbackText ?? 'Nenhum item encontrado'}
    </h3>
  );
}
