package pt.unl.fct.csd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pt.unl.fct.csd.Controller.WalletController;
import pt.unl.fct.csd.Repository.UserAccountRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CsdProjectApplicationTests {

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

    final String username1 = "user1", username2 = "user2";
    final long amount = 9001L;

    @Test
    void contextLoads() {
        assertThat(mockMvc).isNotNull();
        assertThat(objectMapper).isNotNull();
        assertThat(walletController).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
        //tests createMoney and getCurrentAmount
    void moneyIsCreated() throws Exception {
        performGetCurrentAmount(username1)
                .andExpect(status().isNotFound());

        //create users, create money, checks amount
        performCreateMoney(new Transaction(username1, amount))
                .andExpect(status().isOk());

        assertThat(userRepository.findById(username1).get().getMoney().equals(amount));

        performGetCurrentAmount(username1)
                .andExpect(status().isOk()).andExpect(content().json(Long.toString(amount)));

        performCreateMoney(new Transaction(username2, amount))
                .andExpect(status().isOk());
        assertThat(userRepository.findById(username2).get().getMoney().equals(amount));

        //just create money and give it to user1
        performCreateMoney(new Transaction(username1, amount))
                .andExpect(status().isOk());
        assertThat(userRepository.findById(username1).get().getMoney() == amount * 2);

        //tries to "destroy" money
        performCreateMoney(new Transaction(username1, amount * -1))
                .andExpect(status().isBadRequest());
        assertThat(!userRepository.findById(username1).get().getMoney().equals(amount));

        //final amount check
        performGetCurrentAmount(username1)
                .andExpect(status().isOk()).andExpect(content().json(Long.toString(amount * 2)));

    }

    //TODO add more tests

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