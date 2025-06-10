package br.edu.ifrs.poa.pitanga_code.infra.lib;

import org.antlr.v4.runtime.misc.MurmurHash;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.LoadBalanceAlgorithmProvider;

@Component
@Order(value = 2)
public class LBHashProvider implements LoadBalanceAlgorithmProvider {
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public int getNumber() {
        Character[] characters = new Character[value.length()];
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            characters[i] = Character.valueOf(c);
        }
        return MurmurHash.hashCode(characters, 10);
    }
}
