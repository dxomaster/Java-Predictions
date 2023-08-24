package factory;

import engine.jaxb.schema.generated.PRDActivation;
import rule.utils.Activation;

public class ActivationFactory {
    public static Activation createActivation(PRDActivation prdActivation) {
        if (prdActivation == null) {
            return new Activation();
        }
        if (prdActivation.getTicks() != null && prdActivation.getProbability() != null) {
            return new Activation(prdActivation.getTicks(), prdActivation.getProbability());
        } else if (prdActivation.getTicks() != null) {
            return new Activation(prdActivation.getTicks());
        } else {
            return new Activation(prdActivation.getProbability());
        }

    }

}
