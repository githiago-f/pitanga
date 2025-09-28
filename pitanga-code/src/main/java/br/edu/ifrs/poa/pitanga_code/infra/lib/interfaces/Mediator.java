package br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces;

public interface Mediator {
    static record Message<T>(String type, T data) {
    };

    <T> void dispatch(Message<T> message);
}
