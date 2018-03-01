package cupcarbon_script;

public class CupEvalCondition {

	protected CupScript script;

	public CupEvalCondition(CupScript script) {
		this.script = script;
	}

	public CupConditionElement initCondition(String condition) {
		condition = condition.replaceAll(" ", "");
		int frst = condition.indexOf("(");
		int last = condition.lastIndexOf(")");

		if (frst == 0 && last == condition.length() - 1)
			if (condition.indexOf("(", frst + 1) < condition.indexOf(")", frst + 1))
				condition = condition.substring(frst + 1, last);

		if (getNbConditions(condition) == 0) {
			CupConditionElementEnd cond = new CupConditionElementEnd(script, condition);
			return cond;
		}

		String[] tCondition = getTwoParts(condition);

		if (tCondition == null)
			return null;

		if (tCondition[2] == "&&") {
			CupConditionElementAnd cond = new CupConditionElementAnd(initCondition(tCondition[0]), initCondition(tCondition[1]));
			return cond;
		}

		if (tCondition[2] == "||") {
			CupConditionElementOr cond = new CupConditionElementOr(initCondition(tCondition[0]), initCondition(tCondition[1]));
			return cond;
		}

		return null;
	}

	public String[] getTwoParts(String condition) {
		int idxAnd;
		String res[] = new String[3];
		String block1;
		String block2;

		int fromIndex = 0;

		idxAnd = condition.indexOf("&&", fromIndex);
		while (idxAnd != -1) {
			block1 = condition.substring(0, idxAnd);
			block2 = condition.substring(idxAnd + 2, condition.length());
			res[0] = block1;
			res[1] = block2;
			res[2] = "&&";
			if (correctNbBrackets(block1) && correctNbBrackets(block2))
				return res;
			fromIndex = idxAnd + 2;
			idxAnd = condition.indexOf("&&", fromIndex);
		}

		idxAnd = condition.indexOf("||");
		while (idxAnd != -1) {
			block1 = condition.substring(0, idxAnd);
			block2 = condition.substring(idxAnd + 2, condition.length());
			res[0] = block1;
			res[1] = block2;
			res[2] = "||";
			if (correctNbBrackets(block1) && correctNbBrackets(block2))
				return res;
			fromIndex = idxAnd + 2;
			idxAnd = condition.indexOf("||", fromIndex);
		}

		return null;
	}

	public int getNbConditions(String condition) {
		int compBrOPen = 0;
		int compBrClose = 0;

		for (int i = 0; i < condition.length(); i++) {
			if (condition.charAt(i) == '(')
				compBrOPen++;
			else if (condition.charAt(i) == ')') {
				compBrClose++;
			}
		}

		if (compBrOPen == compBrClose)
			return compBrOPen;

		return -1;
	}

	public boolean correctNbBrackets(String condition) {
		int compBrOPen = 0;
		int compBrClose = 0;

		for (int i = 0; i < condition.length(); i++) {
			if (condition.charAt(i) == '(')
				compBrOPen++;
			else if (condition.charAt(i) == ')') {
				compBrClose++;
			}
		}

		if (compBrOPen == compBrClose)
			return true;

		return false;
	}

}
