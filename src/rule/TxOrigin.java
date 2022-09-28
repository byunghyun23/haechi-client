package rule;

import java.util.List;
import java.util.ArrayList;

import context.ExpressionContext;
import util.ValidationRule;
import node.Expression;

public class TxOrigin implements ValidationRule {
	List<String> characterCounts = null;
	
	public TxOrigin() {
		characterCounts = null;
	}
	
	@Override
	public boolean isImplement() {
		return true;
	}
	
	@Override
	public void analyze() {
		characterCounts = new ArrayList<String>();
		
		if(!characterCounts.isEmpty()) {
			characterCounts.clear();
		}
		
		ExpressionContext expressionContext = new ExpressionContext();
		List<Expression> txOrigins = expressionContext.getAllTxOrigins();
		for(Expression txOrigin : txOrigins) {
			characterCounts.add(txOrigin.getCharacterCount());
		}
	}

	@Override
	public Criticity getRuleCriticity() {
	    return Criticity.MAJOR;
	}

	@Override
	public String getRuleName() {
	    return "Use tx.origin";
	}

	@Override
	public String getComment() {
	    return "Potential vulnerability to tx.origin attack";
	}
    
	@Override
	public List<String> getCharacterCounts() {
		return characterCounts;
	}
}


