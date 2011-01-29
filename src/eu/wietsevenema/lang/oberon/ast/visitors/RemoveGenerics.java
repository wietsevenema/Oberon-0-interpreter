package eu.wietsevenema.lang.oberon.ast.visitors;

import java.lang.reflect.InvocationTargetException;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import eu.wietsevenema.lang.oberon.ast.expressions.BinaryExpression;
import eu.wietsevenema.lang.oberon.ast.expressions.Expression;
import eu.wietsevenema.lang.oberon.exceptions.ExpressionNotFoundException;
import eu.wietsevenema.lang.oberon.exceptions.TransformedException;

public class RemoveGenerics extends Visitor {

	/** Transform the children of this node 
	 * @throws TransformedException */
	public GNode visit(GNode n) throws TransformedException {
		for (int i = 0; i < n.size(); i++) {
			Object o = n.get(i);
			if (o instanceof Node) {
				Object transformed = dispatch((Node) o);
				if(transformed == null){
					throw new TransformedException("Transforming node " + o.toString() + "failed");
				}
				n.set(i, dispatch((Node) o));
			}
		}
		return n;
	}

	public Node visit(Node n) {
		return n;
	}
	
	/**
	 * Some magic to transform a generic expression node to it's static typed equivalent. 
	 * @param n
	 * @return
	 * @throws ExpressionNotFoundException 
	 */
	private BinaryExpression createBinaryExpression(GNode n) throws ExpressionNotFoundException {
		Expression left, right;
		String token;
		left = (Expression)dispatch(n.getNode(0)); 
		token = n.getString(1); 
		right = (Expression)dispatch(n.getNode(2)); 
		
		try {
			@SuppressWarnings("unchecked")
			Class<BinaryExpression> expression = (Class<BinaryExpression>) Class.forName("eu.wietsevenema.lang.oberon.ast."+n.getName());
			Class<?>[] args={ 	Class.forName("eu.wietsevenema.lang.oberon.ast.Expression"), 
								Class.forName("eu.wietsevenema.lang.oberon.ast.Expression"),
								Class.forName("java.lang.String")
								};
			BinaryExpression result = (BinaryExpression)expression.getConstructor(args).newInstance(left, right, token);
			return result;
		} catch (ClassNotFoundException e){
			throw new ExpressionNotFoundException("Binary expression '" + n.getName() + "' has no corresponding class definition.");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public BinaryExpression visitEqualityExpression(GNode n) throws ExpressionNotFoundException {
		return createBinaryExpression(n);
	}

	public BinaryExpression visitAdditiveExpression(GNode n) throws ExpressionNotFoundException {
		return createBinaryExpression(n);
	}

	public BinaryExpression visitMultiplicativeExpression(GNode n) throws ExpressionNotFoundException {
		return createBinaryExpression(n);
	}
	
	public BinaryExpression visitLogicalConjunctiveExpression(GNode n) throws ExpressionNotFoundException {
		return createBinaryExpression(n);
	}
	
	public BinaryExpression visitLogicalDisjunctiveExpression(GNode n) throws ExpressionNotFoundException {
		return createBinaryExpression(n);
	}
	
	
	
	
}