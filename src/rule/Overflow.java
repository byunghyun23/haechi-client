package rule;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
//import java.util.Map;
//import java.util.Stack;
//import java.util.HashMap;
//
//import context.FunctionDefinitionContext;
//import node.AST;
//import node.Expression;
//import node.FunctionDefinition;
//import node.VariableDeclarationStatement;
//import node.ExpressionStatement;
//import node.IfStatement;
//import node.WhileStatement;
//import node.ForStatement;
import util.ValidationRule;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;

// import com.microsoft.z3.*;

public class Overflow implements ValidationRule{
	List<String> characterCounts = null;

	public Overflow() {
		characterCounts = null;
	}
	
	@Override
	public boolean isImplement() {
		return false;
	}

	@Override
	public void analyze() {
		characterCounts = new ArrayList<String>();
		
		if(!characterCounts.isEmpty()) {
			characterCounts.clear();
		}
	}
	
	@Override
	public Criticity getRuleCriticity() {
	    return Criticity.CRITICAL;
	}

	@Override
	public String getRuleName() {
	    return "Overflow";
	}

	@Override
	public String getComment() {
	    return "Note the operation of integer variables";
	}
    
	@Override
	public List<String> getCharacterCounts() {
		return characterCounts;
	}
	
}

