package pt.unl.fct.csd.Replication;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pt.unl.fct.csd.Controller.AuctionController;
import pt.unl.fct.csd.Controller.WalletController;
import pt.unl.fct.csd.Model.Transaction;

import javax.annotation.PostConstruct;
import java.io.*;

@PropertySource("classpath:application.properties")
@Component
public class Server extends DefaultSingleRecoverable implements Runnable{


	@Value("${replica.id}")
	private int ID;

	@Qualifier("ImpWallet")
	@Autowired
	private WalletController walletController;

	@Qualifier("ImpAuction")
	@Autowired
	private AuctionController auctionController;

	@PostConstruct
	public void init(){
		new Thread(this).start();
	}

	@Override
	public void run() {
		new ServiceReplica(ID, this, this);
	}

	@Override
	public byte[] appExecuteOrdered(byte[] command, MessageContext messageContext) {
		return invokeCommand(command);
	}

	@Override
	public byte[] appExecuteUnordered(byte[] command, MessageContext messageContext) {
		return invokeCommand(command);
	}

	private byte[] invokeCommand(byte[] command) {
		try (ByteArrayInputStream byteIn = new ByteArrayInputStream(command);
			 ObjectInput objIn = new ObjectInputStream(byteIn);
			 ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			 ObjectOutput objOut = new ObjectOutputStream(byteOut);) {

			Path path = (Path)objIn.readObject();

			switch(path){
				case CREATE_MONEY: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									Transaction transaction = (Transaction)objIn.readObject();
									walletController.createMoney(transaction);
									return 1;
								}
						));
			}

			objOut.flush();
			byteOut.flush();

			return byteOut.toByteArray();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public void installSnapshot(byte[] bytes) {

	}

	@Override
	public byte[] getSnapshot() {
		return new byte[0];
	}

}