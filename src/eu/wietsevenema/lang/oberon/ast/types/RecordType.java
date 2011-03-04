package eu.wietsevenema.lang.oberon.ast.types;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import xtc.tree.Node;
import eu.wietsevenema.lang.oberon.ast.expressions.Identifier;

public class RecordType extends VarType {

	private Map<Identifier, VarType> elements;

	public RecordType(List<Node> list) {
		this.elements = new TreeMap<Identifier, VarType>();
		if (!list.isEmpty()) {
			for (Node node : list) {
				List<?> identList = (List<?>) node.get(0);
				VarType type = (VarType) node.get(1);
				for (Object object : identList) {
					Identifier id = (Identifier) object;
					this.elements.put(id, type);
				}
			}
		}
	}

	public Set<Entry<Identifier,VarType>> entrySet(){
		return this.elements.entrySet();
	}
	
	
	


}
