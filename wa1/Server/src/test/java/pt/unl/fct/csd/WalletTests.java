package pt.unl.fct.csd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pt.unl.fct.csd.Controller.WalletController;
import pt.unl.fct.csd.Model.Transaction;
import pt.unl.fct.csd.Model.UserAccount;
import pt.unl.fct.csd.Repository.UserAccountRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class WalletTests {

    //test helpers
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    //test "subjects"
    @Autowired
    private WalletController walletController;
    @Autowired
    private UserAccountRepository userRepository;

    @Test
    void contextLoads() {
        assertThat(mockMvc).isNotNull();
        assertThat(objectMapper).isNotNull();
        assertThat(walletController).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    void testMoneyCreationOK() throws Exception{
        final String username1 = "user1";
        final long amount = 1;

        performCreateMoney(Transaction.createToWithAmount(username1,amount)).andExpect(status().isOk());
        Optional<UserAccount> user = userRepository.findById(username1);
        assertThat(user.isPresent() && user.get().getMoney().equals(amount));

        performCreateMoney(Transaction.createToWithAmount(username1,amount)).andExpect(status().isOk());
        user = userRepository.findById(username1);
        assertThat(user.isPresent() && user.get().getMoney().equals(amount*2));
    }

    @Test
    void testCurrentAmountOK() throws Exception {
        final String username1 = "user1";
        final long amount = 1;

        performCreateMoney(Transaction.createToWithAmount(username1,amount)).andExpect(status().isOk());
        performGetCurrentAmount(username1).andExpect(status().isOk()).andExpect(content().json(Long.toString(amount)));
    }

    @Test
    void testCurrentAmountNotFound() throws Exception {
        final String username1 = "user1";

        assertThat(!userRepository.findById(username1).isPresent());
        performGetCurrentAmount(username1).andExpect(status().isNotFound());
    }
    @Test
    void testMoneyCreationAmountNegative() throws Exception {
        final String username1 = "user1";
        final long amount = -1;

        performCreateMoney(Transaction.createToWithAmount(username1,amount)).andExpect(status().isBadRequest());
        assertThat(!userRepository.findById(username1).isPresent());
    }

    @Test
    void testMoneyCreationForSystem() throws Exception {
        final String username1 = "SYSTEM";
        final long amount = 1000;

        performCreateMoney(Transaction.createToWithAmount(username1,amount)).andExpect(status().isBadRequest());
        assertThat(!userRepository.findById(username1).isPresent());
    }

    @Test
    void testMoneyTransferOK() throws Exception{
        final String username1 = "user1", username2 = "user2";
        final long amount1 = 9000L;
        final long amount2 = 5000L;
        final long transferAmount = 4000L;

        performCreateMoney(Transaction.createToWithAmount(username1,amount1))
                .andExpect(status().isOk());
        performCreateMoney(Transaction.createToWithAmount(username2,amount2))
                .andExpect(status().isOk());

        performTransferMoney(Transaction.createFromToWithAmount(username1,username2,transferAmount))
                .andExpect(status().isOk());
        Optional<UserAccount> user1 = userRepository.findById(username1), user2 = userRepository.findById(username2);
        //using user.isPresent() to avoid warnings
        assertThat((user1.isPresent() && user1.get().getMoney().equals(amount1 - transferAmount)
                && user2.isPresent() && user2.get().getMoney().equals(amount2 + transferAmount)));

        //TODO maybe this can be removed? already tested
        performGetCurrentAmount(username1)
                .andExpect(status().isOk())
                .andExpect(content().json(Long.toString(amount1 - transferAmount)));

        performGetCurrentAmount(username2)
                .andExpect(status().isOk())
                .andExpect(content().json(Long.toString(amount2 + transferAmount)));
    }

    @Test
    void testMoneyTransferInvalid() throws Exception{
        final String username1 = "user1", username2 = "user2" , system = "SYSTEM";
        final long amount1 = 9000L;
        final long amount2 = 5000L;
        final long transferAmount1 = 4000L;
        final long transferAmount2 = -1L;

        performCreateMoney(Transaction.createToWithAmount(username1,amount1))
                .andExpect(status().isOk());

        performCreateMoney(Transaction.createToWithAmount(username2,amount2))
                .andExpect(status().isOk());

        performTransferMoney(Transaction.createFromToWithAmount(username1,username2,transferAmount2))
                .andExpect(status().isBadRequest());

        Optional<UserAccount> user1 = userRepository.findById(username1), user2 = userRepository.findById(username2);
        //using user.isPresent() to avoid warnings
        assertThat((user1.isPresent() && user1.get().getMoney().equals(amount1)
                && user2.isPresent() && user2.get().getMoney().equals(amount2)));

        performTransferMoney(Transaction.createFromToWithAmount(username1,system,transferAmount1))
                .andExpect(status().isBadRequest());

         user1 = userRepository.findById(username1);
         user2 = userRepository.findById(username2);
        //using user.isPresent() to avoid warnings
        assertThat((user1.isPresent() && user1.get().getMoney().equals(amount1)
                && user2.isPresent() && user2.get().getMoney().equals(amount2)));

        performTransferMoney(Transaction.createFromToWithAmount(username1,system,transferAmount1))
                .andExpect(status().isBadRequest());

        user1 = userRepository.findById(username1);
        user2 = userRepository.findById(username2);
        //using user.isPresent() to avoid warnings
        assertThat((user1.isPresent() && user1.get().getMoney().equals(amount1)
                && user2.isPresent() && user2.get().getMoney().equals(amount2)));
    }

    @Test
    void testMoneyTransferUserNotFound() throws Exception{
        final String username1 = "user1", username2 = "user2" , system = "SYSTEM";
        final long amount1 = 9000L;
        final long transferAmount1 = 4000L;

        performCreateMoney(Transaction.createToWithAmount(username1,amount1))
                .andExpect(status().isOk());

        performTransferMoney(Transaction.createFromToWithAmount(username1,username2,transferAmount1))
                .andExpect(status().isNotFound());

        Optional<UserAccount> user = userRepository.findById(username1);
        //using user.isPresent() to avoid warnings
        assertThat((user.isPresent() && user.get().getMoney().equals(amount1)));

        performTransferMoney(Transaction.createFromToWithAmount(username2,username1,transferAmount1))
                .andExpect(status().isNotFound());

        user = userRepository.findById(username1);
        assertThat((user.isPresent() && user.get().getMoney().equals(amount1)));
    }

    @Test
    void testGetLedger() throws Exception {
        final String username1 = "user1",username2 = "user2" , system = "SYSTEM";
        final long amount1 = 9000L;
        List<Transaction> globalLedger = new LinkedList<>();
        List<Transaction> clientLedger = new LinkedList<>();
        Transaction transaction1 = Transaction.createFromToWithAmount(system,username1,amount1);
        Transaction transaction2 = Transaction.createFromToWithAmount(system,username2,amount1);
        Transaction transaction3 = Transaction.createFromToWithAmount(username1,username2,amount1);

        performCreateMoney(Transaction.createToWithAmount(username1,amount1))
                .andExpect(status().isOk());
        globalLedger.add(transaction1);
        clientLedger.add(transaction1);

        performGetClientLedger(username1)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(clientLedger)));

        performGetLedger()
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(globalLedger)));

        globalLedger.add(transaction2);

        performCreateMoney(Transaction.createToWithAmount(username2,amount1))
                .andExpect(status().isOk());


        performGetClientLedger(username1)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(clientLedger)));


        performGetLedger()
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(globalLedger)));


        clientLedger.add(transaction3);
        globalLedger.add(transaction3);

        performTransferMoney(transaction3);

        performGetClientLedger(username1)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(clientLedger)));


        performGetLedger()
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(globalLedger)));

    }


    private ResultActions performCreateMoney(Transaction transaction) throws Exception {
        return mockMvc.perform(post(walletController.BASE_URL + walletController.CREATE_MONEY)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(transaction)));
    }

    private ResultActions performTransferMoney(Transaction transaction) throws Exception {
        return mockMvc.perform(post(walletController.BASE_URL + walletController.TRANSFER_MONEY)
                .contentType(APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(transaction)));
    }

    private ResultActions performGetCurrentAmount(String username) throws Exception {
        return mockMvc.perform(get(walletController.BASE_URL + walletController.GET_MONEY, username)
                .contentType(APPLICATION_JSON_VALUE));
    }

    private ResultActions performGetClientLedger(String username) throws Exception {
        return mockMvc.perform(get(walletController.BASE_URL + walletController.GET_CLIENT_LEDGER, username)
                .contentType(APPLICATION_JSON_VALUE));
    }

    private ResultActions performGetLedger() throws Exception {
        return mockMvc.perform(get(walletController.BASE_URL + walletController.GET_LEDGER)
                .contentType(APPLICATION_JSON_VALUE));
    }


}