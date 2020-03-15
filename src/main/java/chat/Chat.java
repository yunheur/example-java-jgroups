package chat;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ObjectMessage;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.Address;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Chat implements Receiver {
	protected JChannel channel;
	protected static final String CLUSTER="chat";

	public void viewAccepted(View new_view) {
		System.out.println("** view: " + new_view);
	}

	public void suspect(Address suspected_mbr) {
		System.out.printf("[ %s ]님이 방을 나갔습니다.\n", suspected_mbr);
	}

	public void receive(Message msg) {
		String line="[" + msg.getSrc() + "]: " + msg.getObject();
		System.out.println(line);
	}

	/** Method called from other app, injecting channel */
	public void start(JChannel ch) throws Exception {
		channel=ch;
		channel.setReceiver(this);
		channel.connect(CLUSTER);
		eventLoop();
		channel.close();
	}

	public void start(String props, String name, boolean nohup) throws Exception {
		channel=new JChannel(props);
		if(name != null)
			channel.name(name);
		channel.setReceiver(this);
		channel.connect(CLUSTER);
		if(!nohup) {
			eventLoop();
			channel.close();
		}
	}

	private void eventLoop() {
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			try {
				System.out.print("> "); System.out.flush();
				String line=in.readLine().toLowerCase();
				if(line.startsWith("quit") || line.startsWith("exit")) {
					break;
				}
				Message msg=new ObjectMessage(null, line);
				channel.send(msg);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}