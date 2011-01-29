package eu.wietsevenema.lang.oberon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Reader;

import eu.wietsevenema.lang.oberon.ast.visitors.PrettyPrinter;
import eu.wietsevenema.lang.oberon.ast.visitors.RemoveGenerics;

import xtc.parser.ParseError;
import xtc.parser.Result;
import xtc.parser.SemanticValue;
import xtc.tree.Node;
import xtc.tree.Printer;

public class Interpreter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    if ((null == args) || (0 == args.length)) {
	      System.err.println("Usage: <file-name>+");
	
	    } else {
	      for (int i=0; i<args.length; i++) {
	        System.err.println("Processing " + args[i] + " ...");
	
	        Reader   in = null;
	        try {
	          in        = new BufferedReader(new FileReader(args[i]));
	          Oberon p  = 
	            new Oberon(in, args[i], (int)new File(args[i]).length());
	          Result r  = p.pProgram(0);
	
	          if (r.hasValue()) {
	            SemanticValue v = (SemanticValue)r;
	
	            if (v.value instanceof Node) {
	              RemoveGenerics e = new RemoveGenerics();
	              Node result;
	              result = (Node) e.dispatch((Node)v.value);
	              Printer ptr = new
	                Printer(new BufferedWriter(new OutputStreamWriter(System.out)));
	              PrettyPrinter pp = new PrettyPrinter(ptr);
	              pp.dispatch(result);
	              ptr.flush();
	              
	            } else {
	              System.out.println(v.value.toString());
	            }
	
	          } else {
	            ParseError err = (ParseError)r;
	            if (-1 == err.index) {
	              System.err.println("  Parse error");
	            } else {
	              System.err.println("  " + p.location(err.index) + ": " + err.msg);
	            }
	          }
	
	        } catch (Throwable x) {
	          while (null != x.getCause()) {
	            x = x.getCause();
	          }
	          x.printStackTrace();
	        } finally {
	          try {
	            in.close();
	          } catch (Throwable x) {
	            /* Ignore. */
	          }
	        }
	      }
	    }
	  }
	
}
