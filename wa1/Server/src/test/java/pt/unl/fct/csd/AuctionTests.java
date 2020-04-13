package pt.unl.fct.csd;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import pt.unl.fct.csd.Controller.AuctionController;
import pt.unl.fct.csd.Repository.UserAccountRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AuctionTests {

    //test helpers
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    //test "subjects"
    @Qualifier("ImpAuctionReplicator")
    @Autowired
    private AuctionController auctionController;

    @Autowired
    private UserAccountRepository userRepository;


    @Test
    void contextLoads() {
        assertThat(mockMvc).isNotNull();
        assertThat(objectMapper).isNotNull();
        assertThat(auctionController).isNotNull();
        assertThat(userRepository).isNotNull();
    }



}