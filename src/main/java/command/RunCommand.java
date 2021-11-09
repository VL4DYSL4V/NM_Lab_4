package command;

import framework.command.AbstractRunnableCommand;
import framework.utils.ConsoleUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RunCommand extends AbstractRunnableCommand {

    private static final String NAME = "run";

    public RunCommand() {
        super(NAME);
    }

    @Override
    public void execute(String[] strings) {
        int degree = (int) applicationState.getVariable("degree");
        UnivariateFunction function = (UnivariateFunction) applicationState.getVariable("function");
        List<Double> listX = getChebyshevNodes(degree, new Interval(-1.0, 1.0));
        List<Double> listY = listX.stream().map(function::value).collect(Collectors.toList());
        PolynomialFunction result = buildPolynomial(listX, listY);
        ConsoleUtils.println("Lagrange's polynomial:");
        ConsoleUtils.println(result.toString());

        checkResult(result, listX, listY);
    }

    private void checkResult (PolynomialFunction res, List<Double> listX, List<Double> listY) {
        String template = "result(x) - function(x) = %f";
        ConsoleUtils.println("\nCheck:");
        for (int i = 0; i < listX.size(); i++) {
            double delta = res.value(listX.get(i)) - listY.get(i);
            ConsoleUtils.println(String.format(template, delta));
        }
    }

    private PolynomialFunction buildPolynomial(List<Double> listX, List<Double> listY) {
        PolynomialFunction out = new PolynomialFunction(new double[]{0});
        for (int i = 0; i < listY.size(); i++) {
            PolynomialFunction L_i = buildL_i(i, listX);
            PolynomialFunction y_iAsFunction = new PolynomialFunction(new double[]{listY.get(i)});
            out = out.add(L_i.multiply(y_iAsFunction));
        }
        return out;
    }

    private PolynomialFunction buildL_i(int i, List<Double> listX) {
        double x_i = listX.get(i);
        PolynomialFunction out = new PolynomialFunction(new double[]{1});
        for (int k = 0; k < listX.size(); k++) {
            if (k == i) {
                continue;
            }
            double delta = x_i - listX.get(k);
            double[] coefficients = new double[]{-1 * listX.get(k) / delta, 1 / delta};
            out = out.multiply(new PolynomialFunction(coefficients));
        }
        return out;
    }

    private static List<Double> getChebyshevNodes(int degree, Interval interval) {
        Function<Integer, Double> computeNextX = (k) ->
                0.5 * (interval.getInf() + interval.getSup())
                        + 0.5 * (interval.getSup() - interval.getInf()) * Math.cos(Math.PI * (2.0 * k - 1) / (2.0 * degree));
        List<Double> out = new ArrayList<>(degree);
        for (int k = 1; k <= degree; k++) {
            out.add(computeNextX.apply(k));
        }
        return out;
    }
}
