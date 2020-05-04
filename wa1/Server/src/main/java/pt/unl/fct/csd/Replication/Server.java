package pt.unl.fct.csd.Replication;

import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pt.unl.fct.csd.Controller.AuctionController;
import pt.unl.fct.csd.Controller.SmartContractController;
import pt.unl.fct.csd.Controller.WalletController;
import pt.unl.fct.csd.Model.Bid;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Model.VoidWrapper;

import javax.annotation.PostConstruct;
import java.io.*;

@PropertySource("classpath:application.properties")
@Component
public class Server extends DefaultSingleRecoverable implements Runnable{
	private final Logger logger =
			LoggerFactory.getLogger(Server.class);


	@Value("${replica.id}")
	private int ID;

	@Value("${replica.byzantineBehaviour}")
	private boolean isByzantine;

	@Autowired
	private ApplicationContext context;

	private WalletController walletController;

	@Qualifier("ImpAuction")
	@Autowired
	private AuctionController auctionController;

	@Qualifier("ImpSmart")
	@Autowired
	private SmartContractController smartContractController;

	@PostConstruct
	public void init(){
		this.walletController = (WalletController)context.getBean((isByzantine ? "walletByz" : "ImpWallet"));
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
									logger.info("Successfully completed currentAmount");
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
									return walletController.
											ledgerOfClientTransfers((String)objIn.readObject()).toArray();
								}
						));
						break;

				/************************************Auction Path Starts***********************************************/

				case CREATE_AUCTION:objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Create auction successfully invoked");
									String clientId = (String) objIn.readObject();
									return auctionController.createAuction(clientId);
								}
						));
					break;
				case CREATE_BID_AUCTION: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Create auction bid successfully invoked");
									Bid bid = (Bid) objIn.readObject();
									return auctionController.makeBid(bid);
								}
						));
					break;
				case TERMINATE_AUCTION: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Terminate auction successfully invoked");
									Long auctionId = (Long) objIn.readObject();
									auctionController.terminateAuction(auctionId);
									return new VoidWrapper();
								}
						));
					break;
				case GET_CLOSE_BID: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Get close bid successfully invoked");
									Long auctionId = (Long) objIn.readObject();
									return auctionController.getCloseBid(auctionId);
								}
						));
					break;
				case GET_OPEN_AUCTIONS: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Get open auctions successfully invoked");
									return auctionController.getOpenAuctions().toArray();
								}
						));
					break;
				case GET_CLOSED_AUCTIONS: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Get close auctions successfully invoked");
									return auctionController.getClosedAuction().toArray();
								}
						));
					break;
				case GET_AUCTION_BIDS: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Get auction bids successfully invoked");
									Long auctionId = (Long) objIn.readObject();
									return auctionController.getAuctionBids(auctionId).toArray();
								}
						));
					break;
				case GET_CLIENT_BIDS: objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Get client bids successfully invoked");
									String clientId = (String) objIn.readObject();
									return auctionController.getClientBids(clientId).toArray();
								}
						));
					break;

				/************************************SMART Path Starts***********************************************/
				case CREATE_SMART:objOut.
						writeObject(InvokerWrapper.catchInvocation(
						() -> {
							logger.info("Get CREATE_SMART successfully invoked");
							DualArgReplication<String, byte[]> dual =
									(DualArgReplication<String, byte[]>)objIn.readObject();

							smartContractController.createSmart(dual.getArg1(),dual.getArg2());

							return new VoidWrapper();
						}
				));
					break;
				case REMOVE_SMART:objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Get REMOVE_SMART successfully invoked");
									smartContractController.deleteSmartContract((String)objIn.readObject());
									return new VoidWrapper();
								}
						));
					break;
				case LIST_SMART:objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Get LIST_SMART successfully invoked");
									return smartContractController.ledgerSmartContracts().toArray();
								}
						));
			  		break;
				case GET_SMART:objOut.
						writeObject(InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Get GET_SMART successfully invoked");
									return smartContractController.getSmartContract((String)objIn.readObject());
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
		logger.error("Not implemented");
	}

	@Override
	public byte[] getSnapshot() {
		logger.error("Not implemented");
		return new byte[0];
	}

}