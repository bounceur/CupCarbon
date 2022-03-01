/*----------------------------------------------------------------------------------------------------------------
 * CupCarbon: IoT Simulator
 * www.cupcarbon.com
 * ----------------------------------------------------------------------------------------------------------------
 * Copyright (C) 2013-2021 CupCarbon
 * ----------------------------------------------------------------------------------------------------------------
 * 
 * MQTT Module : to execute MQTT functions
 * 2 parts :
 * 	part 1 uses MQTT protocol for WSN simulation (send)
 * 	Part 2 uses MQTT protocol for MQTT communication (publish/subscribe)
 * 
 * ----------------------------------------------------------------------------------------------------------------
 **/

package device;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import cupcarbon.CupCarbon;
import map.MapLayer;
import simulation.Simulation;

/**
 * @author Ahcene Bounceur
 * @version 1.0
 */


public class IoTMqttModule  implements MqttCallback {

	public static String broker = "tcp://mqtt.eclipseprojects.io";
	//public static String broker = "tcp://broker.hivemq.com";
	public static String port = "1883";
	
	public static String user = "";
	public static String password = "";
	
	private final String startTopic = "cupcarbon_sim";
	private final String wsnPartOfTopic = "wsn_iot_simulation";
	
	private final String wsn_topic_unicast = startTopic+"/"+CupCarbon.cupcarbonSession+"/"+wsnPartOfTopic+"/unicast";
	private final String wsn_topic_broadcast = startTopic+"/"+CupCarbon.cupcarbonSession+"/"+wsnPartOfTopic+"/broadcast";
	private final String wsn_topic_multicast = startTopic+"/"+CupCarbon.cupcarbonSession+"/"+wsnPartOfTopic+"/multicast";
	
	public static String com_real_node_topic = "";
	
	private MqttClient client = null;
	
	private IoTNode iotNode;
	
	public IoTMqttModule(IoTNode iotNode) throws MqttException {
		this.iotNode = iotNode ;
		initConnexion();
	}
	
	public void initConnexion() throws MqttException {
		System.out.println(broker+":"+port);
		String comp_broker = broker+":"+port;		
		
		client = new MqttClient(comp_broker, "cupcarbon_"+MqttClient.generateClientId());
		
		if(user.isEmpty())
			client.connect();
		else {
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setUserName(user);
			connOpts.setPassword(password.toCharArray());
			client.connect(connOpts);
		}
		
		client.setCallback(this);
		client.subscribe(wsn_topic_unicast+"/"+iotNode.getId(), 0);
		client.subscribe(wsn_topic_broadcast, 0);
	}
	
	public void setMy(int my) {
		try {
			client.unsubscribe(wsn_topic_multicast+"/"+iotNode.getCurrentRadioModule().getMy());
			client.subscribe(wsn_topic_multicast+"/"+my);
			iotNode.getCurrentRadioModule().setMy(my);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void setId(int id) {
		try {
			client.unsubscribe(wsn_topic_unicast+"/"+iotNode.getId());
			client.subscribe(wsn_topic_unicast+"/"+id);
			iotNode.setId(id);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isSensorDetecting() {
		return iotNode.isSensorDetecting(); 
	}
	
	public void setMy() {
		try {
			client.subscribe(wsn_topic_multicast+"/"+iotNode.getCurrentRadioModule().getMy());
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void setId() {
		try {
			client.subscribe(wsn_topic_unicast+"/"+iotNode.getId());
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteMy() {
		try {
			client.unsubscribe(wsn_topic_multicast+"/"+iotNode.getCurrentRadioModule().getMy());
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteId() {
		try {
			client.unsubscribe(wsn_topic_unicast+"/"+iotNode.getId());
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void subscribe(String topic) {
		try {
			client.subscribe(topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void unsubscribe(String topic) {
		try {
			client.unsubscribe(topic);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	 
	public void publish(String topic, String message, int ... args) throws MqttPersistenceException, MqttException {
		int qos = 0;
		if(args.length==1) qos = args[0];
		MqttMessage mqttMessage = new MqttMessage();
	    mqttMessage.setPayload(message.getBytes());
	    
	    mqttMessage.setQos(qos);
	    
	    client.publish(topic, mqttMessage);
	}
	
	public void publish(String topic, int message, int ... args) throws MqttPersistenceException, MqttException {
		int qos = 0;
		if(args.length==1) qos = args[0];
		MqttMessage mqttMessage = new MqttMessage();
	    mqttMessage.setPayload((message+"").getBytes());
	    
	    mqttMessage.setQos(qos);
	    
	    client.publish(topic, mqttMessage);
	}
	
	public void publish(String topic, double message, int ... args) throws MqttPersistenceException, MqttException {
		int qos = 0;
		if(args.length==1) qos = args[0];
		MqttMessage mqttMessage = new MqttMessage();
	    mqttMessage.setPayload((message+"").getBytes());
	    
	    mqttMessage.setQos(qos);
	    
	    client.publish(topic, mqttMessage);
	}
	
	public void send(String message, int ... args) throws MqttPersistenceException, MqttException {
		MqttMessage mqttMessage = new MqttMessage();
	    mqttMessage.setPayload(message.getBytes());
	    mqttMessage.setQos(0);
	    
	    if(args.length == 0) {
	    	client.publish(wsn_topic_broadcast, mqttMessage);
	    }
	    if(args.length == 1) {
	    	client.publish(wsn_topic_unicast+"/"+args[0], mqttMessage);
	    }
	    if(args.length > 0 && args[0]==0) {
	    	client.publish(wsn_topic_multicast+"/"+args[1], mqttMessage);
	    }
	}
		
	@Override
	public void connectionLost(Throwable cause) {}

	@Override
	public void messageArrived(String rec_topic, MqttMessage payload) throws Exception {
		if(Simulation.simulating) {
			String message = new String(payload.getPayload());
			
			if(inWsnTopics(rec_topic)) {
				int r_id = Integer.valueOf(message.substring(0, message.indexOf('#')));
				IoTNode r_iotNode = DeviceList.getIoTNodeById(r_id);
				if(iotNode.radioDetect(r_iotNode) && iotNode.canCommunicateWith(r_iotNode)) {
					String r_message = message.substring(message.indexOf('#')+1);
					iotNode.addMessage(r_message);
					iotNode.addToSenders(r_iotNode);
				}
			}
			else {
				if(rec_topic.startsWith(IoTMqttModule.com_real_node_topic+"/s/"+iotNode.getId())) {
					
					System.out.println(Thread.currentThread().getStackTrace()[2].getClassName());
					System.out.println(message);
					
					if(message.contentEquals("YES") || message.contentEquals("YES1")) {
						iotNode.setConnected(true);
						MapLayer.repaint();
					}
					if(message.startsWith("MARK")) {
						iotNode.setMarked(message.split(":")[1].equals("1"));
						MapLayer.repaint();
					}
					if(message.startsWith("GPS")) {
						iotNode.setLongitude(Double.parseDouble(message.split(":")[2]));
						iotNode.setLatitude(Double.parseDouble(message.split(":")[3]));
						iotNode.setElevation(Double.parseDouble(message.split(":")[4]));
						MapLayer.repaint();
					}
				}
				else {
					iotNode.getInterpreter().exec("callback(\""+rec_topic+"\", \""+message+"\")");
				}
			}
		}
	}
	
	public boolean inWsnTopics(String topic) {
		if(topic.startsWith(startTopic+"/"+CupCarbon.cupcarbonSession+"/"+wsnPartOfTopic+"/")) return true;
		return false;
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {}
	
	public int getMy() {
		return iotNode.getCurrentRadioModule().getMy();
	}


}