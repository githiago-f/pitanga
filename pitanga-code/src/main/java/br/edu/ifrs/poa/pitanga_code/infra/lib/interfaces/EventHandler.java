package br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces;

public interface EventHandler<T> {
    void execute(T data);
}
