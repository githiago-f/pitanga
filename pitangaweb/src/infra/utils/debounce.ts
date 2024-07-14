export const debounce = <CB extends Function> (cb: CB) => {
  let timer: number;
  return (...e: unknown[]) => {
    clearTimeout(timer);
    timer = setTimeout(() => cb(...e), 1000);
  };
};
