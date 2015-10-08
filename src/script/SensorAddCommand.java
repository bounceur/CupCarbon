package script;


import device.SensorNode;

public final class SensorAddCommand {

	public static String detectKeyWord(String s) {		
		return s.replaceFirst("\\(", " (");
	}
	
	public static void addCommand(String instStr, SensorNode sensorNode, Script script) {
		instStr = detectKeyWord(instStr);
		String[] inst = instStr.split(" ");
		
		Command command = null;
		
		if (inst[0].toLowerCase().equals("delay")) {
			command = new Command_DELAY(sensorNode, inst[1]);
		}				
		if (inst[0].toLowerCase().equals("set")) {
			command = new Command_SET(sensorNode, inst[1], inst[2]);
		}	
		if (inst[0].toLowerCase().equals("read")) {
			command = new Command_READ(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("flush")) {
			command = new Command_FLUSH(sensorNode);
		}
		if (inst[0].toLowerCase().equals("send")) {			
			if(inst.length==2) command = new Command_SEND(sensorNode, inst[1], "*");
			if(inst.length==3) command = new Command_SEND(sensorNode, inst[1], inst[2]);
			if(inst.length==4) command = new Command_SEND(sensorNode, inst[1], inst[2], inst[3]);
			if(inst.length==5) command = new Command_SEND(sensorNode, inst[1], inst[2], inst[3], inst[4]);
		}
		if (inst[0].toLowerCase().equals("loop")) {
			command = new Command_LOOP(sensorNode);
		}
		if (inst[0].toLowerCase().equals("wait")) {
			if(inst.length==1) command = new Command_WAIT(sensorNode);
			if(inst.length==2) command = new Command_WAIT(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("stop")) {
			command = new Command_STOP(sensorNode);
		}
		if (inst[0].toLowerCase().equals("plus")) {
			command = new Command_PLUS(sensorNode, inst[1], inst[2], inst[3]);
		}
		if (inst[0].toLowerCase().equals("minus")) {
			command = new Command_MINUS(sensorNode, inst[1], inst[2], inst[3]);
		}
		if (inst[0].toLowerCase().equals("mult")) {
			command = new Command_MULT(sensorNode, inst[1], inst[2], inst[3]);
		}
		if (inst[0].toLowerCase().equals("div")) {
			command = new Command_DIV(sensorNode, inst[1], inst[2], inst[3]);
		}
		if (inst[0].toLowerCase().equals("mod")) {
			command = new Command_MOD(sensorNode, inst[1], inst[2], inst[3]);
		}
		if (inst[0].toLowerCase().equals("tab")) {
			command = new Command_TAB(sensorNode, inst[1], inst[2], inst[3]);
		}
		if (inst[0].toLowerCase().equals("atnd")) {
			if(inst.length==2) command = new Command_ATND(sensorNode, inst[1]);
			if(inst.length==3) command = new Command_ATND(sensorNode, inst[1], inst[2]);
		}
		if (inst[0].toLowerCase().equals("println")) {
			command =  new Command_PRINTLN(sensorNode, inst);
		}
		if (inst[0].toLowerCase().equals("packet")) {
			command = new Command_PACKET(sensorNode, inst);
		}
		if (inst[0].toLowerCase().equals("rpacket")) {
			command = new Command_RPACKET(sensorNode, inst);
		}
		if (inst[0].toLowerCase().equals("angle")) {
			command = new Command_ANGLE(sensorNode, inst[1], inst[2], inst[3], inst[4]);
		}
		if (inst[0].toLowerCase().equals("angle2")) {
			command = new Command_ANGLE2(sensorNode, inst[1], inst[2], inst[3], inst[4], inst[5], inst[6], inst[7]);
		}
		if (inst[0].toLowerCase().equals("mark")) {
			command = new Command_MARK(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("mmin")) {
			command = new Command_MMIN(sensorNode, inst[1], inst[2], inst[3], inst[4]);
		}
		if (inst[0].toLowerCase().equals("getpos2")) {
			command = new Command_GETPOS2(sensorNode, inst[1], inst[2]);
		}
		if (inst[0].toLowerCase().equals("buffer")) {
			command = new Command_BUFFER(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("battery")) {
			command = new Command_BATTERY(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("getpos")) {
			command = new Command_GETPOS(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("atget")) {
			command = new Command_ATGET(sensorNode, inst[1], inst[2]);
		}
		if (inst[0].toLowerCase().equals("rand")) {
			command = new Command_RAND(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("rgauss")) {
			command = new Command_RGAUSS(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("led")) {
			command = new Command_LED(sensorNode, inst[1], inst[2]);
		}
		if (inst[0].toLowerCase().equals("randb")) {
			command = new Command_RANDB(sensorNode, inst[1], inst[2], inst[3]);
		}
		if (inst[0].toLowerCase().equals("tset")) {
			command = new Command_TSET(sensorNode, inst[1], inst[2], inst[3], inst[4]);
		}
		if (inst[0].toLowerCase().equals("tget")) {
			command = new Command_TGET(sensorNode, inst[1], inst[2], inst[3], inst[4]);
		}
		if (inst[0].toLowerCase().equals("dreadsensor")) {
			command = new Command_DREADSENSOR(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("areadsensor")) {
			command = new Command_AREADSENSOR(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("function")) {
			command = new Command_FUNCTION(sensorNode, inst[1], inst[2], inst[3]);
		}
		if (inst[0].toLowerCase().equals("atni")) {
			command = new Command_ATID(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("atid")) {
			command = new Command_ATNID(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("atch")) {
			command = new Command_ATCH(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("atmy")) {
			command = new Command_ATMY(sensorNode, inst[1]);
		}
		if (inst[0].toLowerCase().equals("atpl")) {
			command = new Command_ATPL(sensorNode, inst[1]);
		}				
		if (inst[0].toLowerCase().equals("while")) {
			Command_WHILE commandWhile = new Command_WHILE(sensorNode, instStr);
			if (script.getCurrentWhile() != null){
				commandWhile.setParent(script.getCurrentWhile());
			}
			script.add(commandWhile);
			script.setCurrentWhile(commandWhile);
		}		
		if (inst[0].toLowerCase().equals("endwhile")) {
			Command_ENDWHILE commandWEndhile = new Command_ENDWHILE(sensorNode);
			commandWEndhile.setCurrentWhile(script.getCurrentWhile());
			script.removeCurrentWhile();
			script.add(commandWEndhile);
		}
		if (inst[0].toLowerCase().equals("for")) {
			Command_FOR cmdFor = null;
			if(inst.length==4) cmdFor = new Command_FOR(sensorNode, inst[1], inst[2], inst[3], "1");
			if(inst.length==5) cmdFor = new Command_FOR(sensorNode, inst[1], inst[2], inst[3], inst[4]);
			if (script.getCurrentFor() != null){
				cmdFor.setParent(script.getCurrentFor());
			}
			script.add(cmdFor);
			script.setCurrentFor(cmdFor);
		}
		if (inst[0].toLowerCase().equals("endfor")) {
			Command_ENDFOR cmdEndFor = new Command_ENDFOR(sensorNode);
			cmdEndFor.setCurrentFor(script.getCurrentFor());
			script.removeCurrentFor();
			script.add(cmdEndFor);
		}
		
		if (inst[0].toLowerCase().equals("if")) {
			Command_IF commandIf = new Command_IF(sensorNode, instStr);
			if (script.getCurrentIf() != null){
				commandIf.setParent(script.getCurrentIf());
			}			
			script.setCurrentIf(commandIf);
		}
		
		if (inst[0].toLowerCase().equals("else")) {
			command = new Command_ELSE(sensorNode);
			script.getCurrentIf().setElseIndex(script.getSizeCommands());
		}		
		
		if (inst[0].toLowerCase().equals("endif")) {
			command = new Command_ENDIF(sensorNode);
			script.getCurrentIf().setEndIfIndex(script.getSizeCommands());
			script.removeCurrentIf();
		}
		
		if (inst[0].toLowerCase().equals("rotate")) {
			command = new Command_ROTATE(sensorNode, inst[1]);
		}
		
		if (inst[0].toLowerCase().equals("coord")) {
			command = new Command_COORD(sensorNode, inst[1], inst[2]);
		}	
		
		//-------
		
		if (command != null) {
			script.add(command);
			command.setCurrentIf(script.getCurrentIf());
			command.setCurrentWhile(script.getCurrentWhile());
			command.setCurrentFor(script.getCurrentFor());
		}
		
		
	}
	
}
