package eu.wietsevenema.lang.oberon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import xtc.parser.ParseError;
import xtc.parser.Result;
import xtc.parser.SemanticValue;
import xtc.tree.Node;

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
	              System.out.println(e.dispatch((Node)v.value));
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
