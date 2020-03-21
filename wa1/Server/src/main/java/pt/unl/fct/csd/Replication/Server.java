package pt.unl.fct.csd.Replication;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pt.unl.fct.csd.Controller.AuctionController;

import javax.annotation.PostConstruct;

@PropertySource("classpath:application.properties")
@Component
public class Server extends DefaultSingleRecoverable implements Runnable{


	@Value("${replica.id}")
	private int ID;

	@PostConstruct
	public void init(){
		new Thread(this).start();
	}

	@Override
	public void run() {
		new ServiceReplica(ID, this, this);
	}

	@Override
	public byte[] appExecuteOrdered(byte[] bytes, MessageContext messageContext) {
		return new byte[0];
	}

	@Override
	public byte[] appExecuteUnordered(byte[] bytes, MessageContext messageContext) {
		return new byte[0];
	}

	@Override
	public void installSnapshot(byte[] bytes) {

	}

	@Override
	public byte[] getSnapshot() {
		return new byte[0];
	}

}