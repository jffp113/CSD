package pt.unl.fct.csd.Replication;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pt.unl.fct.csd.Controller.AuctionController;
import pt.unl.fct.csd.Controller.WalletController;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Model.VoidWrapper;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;

@PropertySource("classpath:application.properties")
@Component
public class Server extends DefaultSingleRecoverable implements Runnable{
	private final Logger logger =
			LoggerFactory.getLogger(Server.class);


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
		new ServiceReplica(ID, this, this);
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
			logger.info(String.format("Searching for %s to invoke.", path));
			switch(path){
				case CREATE_MONEY: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									walletController.createMoney((Transaction)objIn.readObject());
									logger.info("Successfully completed createMoney");
									return new VoidWrapper();
								}
						));
						break;
				case TRANSFER_MONEY: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									walletController.transferMoneyBetweenUsers((Transaction)objIn.readObject());
									logger.info("Successfully completed transferMoneyBetweenUsers");
									return new VoidWrapper();
								}
						));
						break;
				case REMOVE_MONEY: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									DualArgReplication<UserAccount, Long> dual =
											(DualArgReplication<UserAccount,Long>)objIn.readObject();
									walletController.removeMoney(dual.getArg1(), dual.getArg2());
									logger.info("Successfully completed removeMoney");
									return new VoidWrapper();
								}
						));
						break;
				case ADD_MONEY: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									DualArgReplication<UserAccount, Long> dual =
											(DualArgReplication<UserAccount,Long>)objIn.readObject();
									walletController.addMoney(dual.getArg1(), dual.getArg2());
									logger.info("Successfully completed addMoney");
									return new VoidWrapper();
								}
						));
						break;
				case GET_MONEY: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									//TODO log first?
									logger.info("Successfully completed addMoney");
									return walletController.currentAmount((String)objIn.readObject());
								}
						));
						break;
				case GET_LEDGER: objOut.
						writeObject(InvokerWrapper.catchInvocation(
							() -> {
								logger.info("Successfully completed ledgerOfClientTransfers");
								return walletController.ledgerOfClientTransfers().toArray();
							}
						));
						break;
				case GET_CLIENT_LEDGER: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Successfully completed ledgerOfClientTransfers");
									return walletController.ledgerOfClientTransfers((String)objIn.readObject()).toArray();
								}
						));
						break;
				default:
					logger.error("Not implemented");
					break;
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