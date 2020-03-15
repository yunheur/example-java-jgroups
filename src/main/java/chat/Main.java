package chat;

public class Main {
	public static void main(String[] args) throws Exception {
		String  props="udp.xml";
		String  name=null;
		boolean nohup=false;

		for(int i=0; i < args.length; i++) {
			if(args[i].equals("-props")) {
				props=args[++i];
				continue;
			}
			if(args[i].equals("-name")) {
				name=args[++i];
				continue;
			}
			if(args[i].equals("-nohup")) {
				nohup=true;
				continue;
			}
			help();
			return;
		}

		new Chat().start(props, name, nohup);
	}

	protected static void help() {
		System.out.println("Chat [-props XML config] [-name name] [-nohup]");
	}
}
