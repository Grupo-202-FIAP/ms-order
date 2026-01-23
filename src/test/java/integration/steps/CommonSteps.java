package integration.steps;

import integration.context.TestContext;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Steps comuns compartilhados entre todas as features.
 * Centraliza validações genéricas para evitar duplicação.
 */
public class CommonSteps {

    @Autowired
    private TestContext testContext;

    @E("o status HTTP da resposta deve ser {int}")
    public void oStatusHttpDaRespostaDeveSerStatus(int expectedStatus) {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response).withFailMessage("Nenhuma resposta HTTP foi registrada no contexto").isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(expectedStatus);
    }

    @E("a mensagem de erro deve conter {string}")
    public void aMensagemDeErroDeveConter(String expectedMessage) {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response).withFailMessage("Nenhuma resposta HTTP foi registrada no contexto").isNotNull();
        
        String responseBody = (String) response.getBody();
        assertThat(responseBody)
                .isNotNull()
                .contains(expectedMessage);
    }

    @Então("a API deve retornar erro de validação")
    public void aApiDeveRetornarErroDeValidacao() {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }
}

