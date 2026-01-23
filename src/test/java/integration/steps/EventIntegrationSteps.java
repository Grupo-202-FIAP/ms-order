package integration.steps;

import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.repository.IEventRepository;
import integration.context.TestContext;
import integration.utils.TestDataBuilder;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EventIntegrationSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IEventRepository eventRepository;

    @Autowired
    private TestContext testContext;

    private Event returnedEvent;
    private Event[] eventList;

    @Dado("que o serviço de eventos está disponível")
    public void queOServicoDeEventosEstaDisponivel() {
        assertThat(restTemplate).isNotNull();
    }

    @E("que o banco de dados de eventos está limpo")
    public void queOBancoDeDadosDeEventosEstaLimpo() {
        eventRepository.deleteAll();
    }

    @Dado("que existe um evento cadastrado com {string} igual a {string}")
    public void queExisteUmEventoCadastradoComFiltro(String tipoFiltro, String valorFiltro) {
        Event event;
        if ("orderId".equals(tipoFiltro)) {
            UUID orderUuid = UUID.fromString(valorFiltro);
            event = TestDataBuilder.buildEvent(orderUuid);
        } else if ("transactionId".equals(tipoFiltro)) {
            UUID transactionUuid = UUID.fromString(valorFiltro);
            event = TestDataBuilder.buildEvent(UUID.randomUUID(), transactionUuid, "PROCESSED");
        } else {
            throw new IllegalArgumentException("Tipo de filtro desconhecido: " + tipoFiltro);
        }
        eventRepository.save(event);
    }

    @Quando("eu envio uma requisição GET para buscar evento por {string} igual a {string}")
    public void euEnvioUmaRequisicaoGetParaBuscarEventoPorFiltro(String tipoFiltro, String valorFiltro) {
        String url;
        if ("orderId".equals(tipoFiltro)) {
            url = "/api/event/filter?orderId=" + valorFiltro;
        } else if ("transactionId".equals(tipoFiltro)) {
            url = "/api/event/filter?transactionId=" + valorFiltro;
        } else {
            throw new IllegalArgumentException("Tipo de filtro desconhecido: " + tipoFiltro);
        }

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                mapper.findAndRegisterModules(); // Para suporte a Java 8 time
                returnedEvent = mapper.readValue(response.getBody(), Event.class);
            } catch (Exception e) {
                returnedEvent = null;
            }
        }
    }

    @Quando("eu busco evento por {string} com valor {string}")
    public void euBuscoEventoPorFiltroComValor(String tipoFiltro, String valor) {
        String url;
        if ("orderId".equals(tipoFiltro)) {
            url = "/api/event/filter?orderId=" + valor;
        } else if ("transactionId".equals(tipoFiltro)) {
            url = "/api/event/filter?transactionId=" + valor;
        } else if ("sem_filtro".equals(tipoFiltro)) {
            url = "/api/event/filter";
        } else {
            throw new IllegalArgumentException("Tipo de filtro desconhecido: " + tipoFiltro);
        }

        ResponseEntity<?> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        testContext.setLastResponse(response);
    }

    @Então("o evento retornado deve ter {string} igual a {string}")
    public void oEventoRetornadoDeveTerFiltro(String tipoFiltro, String valorEsperado) {
        assertThat(returnedEvent).isNotNull();
        if ("orderId".equals(tipoFiltro)) {
            assertThat(returnedEvent.getOrderId().toString()).isEqualTo(valorEsperado);
        } else if ("transactionId".equals(tipoFiltro)) {
            assertThat(returnedEvent.getTransactionId().toString()).isEqualTo(valorEsperado);
        }
    }

    @Dado("que existem {int} eventos cadastrados no sistema")
    public void queExistemEventosCadastradosNoSistema(int count) {
        for (int i = 0; i < count; i++) {
            Event event = TestDataBuilder.buildEvent(UUID.randomUUID());
            eventRepository.save(event);
        }
    }

    @Quando("eu envio uma requisição GET para listar todos os eventos")
    public void euEnvioUmaRequisicaoGetParaListarTodosOsEventos() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/event/all",
                HttpMethod.GET,
                null,
                String.class
        );
        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                mapper.findAndRegisterModules(); // Para suporte a Java 8 time
                eventList = mapper.readValue(response.getBody(), Event[].class);
            } catch (Exception e) {
                eventList = new Event[0];
            }
        }
    }

    @Então("a lista de eventos deve conter {int} eventos")
    public void aListaDeEventosDeveConterEventos(int expectedCount) {
        assertThat(eventList).hasSize(expectedCount);
    }

    @Então("a lista de eventos deve estar vazia")
    public void aListaDeEventosDeveEstarVazia() {
        assertThat(eventList).isEmpty();
    }

    @Quando("eu crio mais {int} eventos adicionais")
    public void euCrioMaisEventosAdicionais(int count) {
        for (int i = 0; i < count; i++) {
            Event event = TestDataBuilder.buildEvent(UUID.randomUUID());
            eventRepository.save(event);
        }
    }
}

