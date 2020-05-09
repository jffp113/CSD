package pt.unl.fct.csd.Replication;

import bftsmart.reconfiguration.util.RSAKeyLoader;
import bftsmart.tom.MessageContext;
import bftsmart.tom.ServiceReplica;
import bftsmart.tom.server.defaultservices.DefaultSingleRecoverable;
import bftsmart.tom.util.TOMUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import pt.unl.fct.csd.Controller.AuctionController;
import pt.unl.fct.csd.Controller.WalletController;
import pt.unl.fct.csd.Model.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

@PropertySource("classpath:application.properties")
@Component
public class Server extends DefaultSingleRecoverable implements Runnable{

    private static final String DEFAULT_KEY_CONFIG = "";

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

	private RSAKeyLoader keyLoader;

	@PostConstruct
	public void init(){
		this.walletController = (WalletController)context.getBean((isByzantine ? "walletByz" : "ImpWallet"));
		new ServiceReplica(ID, this, this);
		this.keyLoader = new RSAKeyLoader(ID, DEFAULT_KEY_CONFIG);
	}

	@Override
	public void run() {
		new ServiceReplica(ID, this, this);
	}

	@Override
	public byte[] appExecuteOrdered(byte[] command, MessageContext messageContext) {
        return sendFullReply(invokeCommand(command));
	}

	@Override
	public byte[] appExecuteUnordered(byte[] command, MessageContext messageContext) {
		return sendFullReply(invokeCommand(command));
	}

	private byte[] invokeCommand(byte[] command) {
		InvokerWrapper<String> a;
		try (ByteArrayInputStream byteIn = new ByteArrayInputStream(command);
			 ObjectInput objIn = new ObjectInputStream(byteIn);
			 ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			 ObjectOutput objOut = new ObjectOutputStream(byteOut);) {

			Path path = (Path)objIn.readObject();
			logger.info(String.format("Searching for %s to invoke.", path));
			switch(path){
				case CREATE_MONEY: {
					InvokerWrapper<VoidWrapper> result = InvokerWrapper.catchInvocation(
							() -> {
								walletController.createMoney((Transaction) objIn.readObject());
								logger.info("Successfully completed createMoney");
								return new VoidWrapper();
							}
					);
					return new Gson().toJson(result).getBytes();
				}
				case TRANSFER_MONEY: {
					InvokerWrapper<VoidWrapper> result = InvokerWrapper.catchInvocation(
						() -> {
							walletController.transferMoneyBetweenUsers((Transaction) objIn.readObject());
							logger.info("Successfully completed transferMoneyBetweenUsers");
							return new VoidWrapper();
						}
					);
					return new Gson().toJson(result).getBytes();
				}
				case REMOVE_MONEY: {
					InvokerWrapper<VoidWrapper> result = InvokerWrapper.catchInvocation(
							() -> {
								DualArgReplication<UserAccount, Long> dual =
										(DualArgReplication<UserAccount, Long>) objIn.readObject();
								walletController.removeMoney(dual.getArg1(), dual.getArg2());
								logger.info("Successfully completed removeMoney");
								return new VoidWrapper();
							}
					);
					return new Gson().toJson(result).getBytes();
				}
				case ADD_MONEY: {
						InvokerWrapper<VoidWrapper> result = InvokerWrapper.catchInvocation(
								() -> {
									DualArgReplication<UserAccount, Long> dual =
											(DualArgReplication<UserAccount,Long>)objIn.readObject();
									walletController.addMoney(dual.getArg1(), dual.getArg2());
									logger.info("Successfully completed addMoney");
									return new VoidWrapper();
								}
						);
					return new Gson().toJson(result).getBytes();
				}
				case GET_MONEY: {
						InvokerWrapper<Long> result = InvokerWrapper.catchInvocation(
								() -> {
									//TODO log first?
									logger.info("Successfully completed currentAmount");
									return walletController.currentAmount((String)objIn.readObject());
								}
						);
					return new Gson().toJson(result).getBytes();
				}
				case GET_LEDGER: {
					InvokerWrapper<Transaction[]> result = InvokerWrapper.catchInvocation(
							() -> {
								logger.info("Successfully completed ledgerOfClientTransfers");
								List<Transaction> transactions = walletController.ledgerOfClientTransfers();
								return transactions.toArray(new Transaction[0]);
							}
						);
					return new Gson().toJson(result).getBytes();
				}
				case GET_CLIENT_LEDGER: {
					InvokerWrapper<Transaction[]> result = InvokerWrapper.catchInvocation(
							() -> {
								logger.info("Successfully completed ledgerOfClientTransfers");
								return (Transaction[]) walletController.
										ledgerOfClientTransfers((String) objIn.readObject()).toArray();
							}
					);
					return new Gson().toJson(result).getBytes();
				}

				/************************************Auction Path Starts***********************************************/

				case CREATE_AUCTION: {
					InvokerWrapper<Long> result = InvokerWrapper.catchInvocation(
							() -> {
								logger.info("Create auction successfully invoked");
								String clientId = (String) objIn.readObject();
								return auctionController.createAuction(clientId);
							}
					);
					return new Gson().toJson(result).getBytes();
				}
				case CREATE_BID_AUCTION: {
					InvokerWrapper<Long> result = InvokerWrapper.catchInvocation(
							() -> {
								logger.info("Create auction bid successfully invoked");
								Bid bid = (Bid) objIn.readObject();
								return auctionController.makeBid(bid);
							}
					);
					return new Gson().toJson(result).getBytes();
				}
				case TERMINATE_AUCTION: {
					InvokerWrapper<VoidWrapper> result = InvokerWrapper.catchInvocation(
							() -> {
								logger.info("Terminate auction successfully invoked");
								Long auctionId = (Long) objIn.readObject();
								auctionController.terminateAuction(auctionId);
								return new VoidWrapper();
							}
					);
					return new Gson().toJson(result).getBytes();
				}
				case GET_CLOSE_BID: {
					InvokerWrapper<Bid> result = InvokerWrapper.catchInvocation(
							() -> {
								logger.info("Get close bid successfully invoked");
								Long auctionId = (Long) objIn.readObject();
								return auctionController.getCloseBid(auctionId);
							}
					);
					return new Gson().toJson(result).getBytes();
				}
				case GET_OPEN_AUCTIONS: {
					InvokerWrapper<Auction[]> result = InvokerWrapper.catchInvocation(
							() -> {
								logger.info("Get open auctions successfully invoked");
								return (Auction[]) auctionController.getOpenAuctions().toArray();
							}
					);
					return new Gson().toJson(result).getBytes();
				}
				case GET_CLOSED_AUCTIONS: {
					InvokerWrapper<Auction[]> result = InvokerWrapper.catchInvocation(
								() -> {
									logger.info("Get close auctions successfully invoked");
									return (Auction[]) auctionController.getClosedAuction().toArray();
								}
						);
					return new Gson().toJson(result).getBytes();
				}
				case GET_AUCTION_BIDS: {
					InvokerWrapper<Bid[]> result = InvokerWrapper.catchInvocation(
							() -> {
								logger.info("Get auction bids successfully invoked");
								Long auctionId = (Long) objIn.readObject();
								return (Bid[]) auctionController.getAuctionBids(auctionId).toArray();
							}
					);
					return new Gson().toJson(result).getBytes();
				}
				case GET_CLIENT_BIDS: {
					InvokerWrapper<Bid[]> result = InvokerWrapper.catchInvocation(
							() -> {
								logger.info("Get client bids successfully invoked");
								String clientId = (String) objIn.readObject();
								return (Bid[]) auctionController.getClientBids(clientId).toArray();
							}
					);
					return new Gson().toJson(result).getBytes();
				}
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

	private byte[] sendFullReply(byte[] serverReply) {
		logger.info("sendFullReply: reply len=" + serverReply.length);

        try (ByteArrayOutputStream bS = new ByteArrayOutputStream();
             DataOutputStream dS = new DataOutputStream(bS)) {
            return tryToSendFullReply(serverReply, bS, dS);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private byte[] tryToSendFullReply(byte[] serverReply, ByteArrayOutputStream bS, DataOutputStream dS)
            throws Exception {
        dS.writeInt(serverReply.length);
        dS.write(serverReply);
        byte[] signedAnswer = sign(serverReply);
		logger.info("tryToSendFullReply: signed reply len="+signedAnswer.length);
        dS.writeInt(signedAnswer.length);
        dS.write(signedAnswer);
        return bS.toByteArray();
    }

    private byte[] sign(byte[] serverReply) throws Exception {
	    return TOMUtil.signMessage(keyLoader.loadPrivateKey(), serverReply);
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