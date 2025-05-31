package br.edu.ifrs.poa.pitanga_code.infra.lib;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import br.edu.ifrs.poa.pitanga_code.infra.lib.interfaces.LoadBalanceAlgorithmProvider;

@Primary
@Component
public class LBRoundRobbingProvider implements LoadBalanceAlgorithmProvider {
    private int counter = 0;

    @Override
    public int getNumber() {
        if (counter + 1 == Integer.MAX_VALUE) {
            counter = 0;
        }
        return counter++;
    }
}
