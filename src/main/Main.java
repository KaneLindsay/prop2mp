package main;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.FileInputStream;


public class Main {

    // Read the file with the SAME path of the main class.
    // Please put the all the test cases in the same path of the main class in your project too.

    // Test the expression of which the root of the AST is a binary expression.
    // private static final String FILE_PATH = "src/main/Example1.java";

    // Test the expression of which the root of the AST is a unary expression.
    private static final String FILE_PATH = "src/main/Example2.java";


    public static void main(String[] args) throws Exception {
        // Set up a minimal type solver that only looks at the classes used to run this sample.
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        // Configure JavaParser to use type resolution
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(FILE_PATH));

        // Find the first recognized expression, which is the boolean propositional logic formula.

        Expression root = cu.findAll(Expression.class).get(0);

        ParseProp(root);

    }

    public static void ParseProp (Expression child) throws Exception{


        System.out.println("--------------------------------");

        if (child instanceof EnclosedExpr) {
            child = ((EnclosedExpr) child).getInner();
        }

        if (child instanceof BinaryExpr) {
            System.out.println(((BinaryExpr) child).getLeft().toString());
            System.out.println(((BinaryExpr) child).getRight().toString());
            System.out.println(((BinaryExpr) child).getOperator().toString());

            Expression left =  ((BinaryExpr) child).getLeft();
            Expression right =  ((BinaryExpr) child).getRight();

            if (((BinaryExpr) child).getLeft() instanceof EnclosedExpr) {
                left = ((EnclosedExpr) ((BinaryExpr) child).getLeft()).getInner();
            }
            if (((BinaryExpr) child).getRight() instanceof EnclosedExpr) {
                right = ((EnclosedExpr) ((BinaryExpr) child).getRight()).getInner();
            }
            if (left instanceof BinaryExpr ||left instanceof UnaryExpr) {
                ParseProp(((BinaryExpr) child).getLeft());
            }
            if (right instanceof BinaryExpr ||right instanceof UnaryExpr) {
                ParseProp(((BinaryExpr) child).getRight());
            }
        }

        if (child instanceof UnaryExpr) { //

            System.out.println(((UnaryExpr) child).getOperator());
            System.out.println(((UnaryExpr) child).getExpression());

            Expression node2 = child;
            Expression child2 = ((UnaryExpr) child).getExpression();
            // for each node, also need to check whether it is enclosed expression with brackets
            if (child2 instanceof EnclosedExpr) {
                //System.out.println(((EnclosedExpr) child2).getInner());
                node2 = ((EnclosedExpr) child2).getInner();
            }

            if (node2 instanceof BinaryExpr) {
                ParseProp(node2);
            }
            if (node2 instanceof UnaryExpr) {
                ParseProp(node2);
            }

        }
    }
}

