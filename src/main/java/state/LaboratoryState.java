package state;

import framework.state.AbstractApplicationState;
import lombok.Getter;
import org.apache.commons.math3.analysis.UnivariateFunction;

@Getter
public class LaboratoryState extends AbstractApplicationState {

    private final UnivariateFunction function = (x) -> x + Math.pow(x, 2) + Math.sin(x);

    private final int degree = 14;

    @Override
    protected void initVariableNameToGettersMap() {
        variableNameToGetter.put("function", this::getFunction);
        variableNameToGetter.put("degree", this::getDegree);
    }
}
