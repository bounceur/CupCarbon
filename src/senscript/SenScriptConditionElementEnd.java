package script;

import device.SensorNode;

public class ConditionElementEnd extends ConditionElement{
	
	
	//protected boolean value;
		protected Condition condition;
		
		
		
		public ConditionElementEnd(SensorNode sensor, String cond){
			
			String [] inst = Condition.getTwoParts(cond);
			
			
			switch(inst[2]){
			case ">":
				condition = new Condition_GREATER(sensor, inst[0], inst[1]);
				break;
			case ">=":
				condition = new Condition_GREATEREQUAL(sensor, inst[0], inst[1]);
				break;
			case "<":
				condition = new Condition_LESS(sensor, inst[0], inst[1]);
				break;
			case "<=":
				condition = new Condition_LESSEQUAL(sensor, inst[0], inst[1]);
				break;
			case "==":
				condition = new Condition_EQUAL(sensor, inst[0], inst[1]);
				break;
			case "!=":
				condition = new Condition_NOTEQUAL(sensor, inst[0], inst[1]);
				break;	
			default:
			}
		}

		
		public boolean evaluate(){
			value = condition.evaluate();
			return value;
		}


		public Condition getCondition() {
			return condition;
		}

		public void setCondition(Condition condition) {
			this.condition = condition;
		}
		
}
