package integration.steps;

import com.nextime.order.infrastructure.persistence.document.Event;
import com.nextime.order.infrastructure.persistence.repository.IEventRepository;
import integration.utils.SqsTestSupport;
import integration.utils.TestDataBuilder;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ConsumerIntegrationSteps {

    @Autowired
    private SqsTestSupport sqsTestSupport;

    @Autowired
    private IEventRepository eventRepository;

    @Value("${spring.sqs.queues.order-callback-queue}")
    private String orderCallbackQueue;

    private UUID lastOrderId;
    private String lastStatus;

    @Dado("que o serviço de consumer está disponível")
    public void queOServicoDeConsumerEstaDisponivel() {
        assertThat(sqsTestSupport).isNotNull();
    }

    @E("que as filas SQS estão configuradas")
    public void queAsFilasSqsEstaoConfiguradas() {
        sqsTestSupport.createQueue(orderCallbackQueue);
        sqsTestSupport.purgeQueue(orderCallbackQueue);
    }

    @Quando("eu publico {int} mensagens válidas na fila com status {string}")
    public void euPublicoMensagensValidasNaFilaComStatus(int quantidade, String statusMensagem) {
        for (int i = 0; i < quantidade; i++) {
            UUID orderId = UUID.randomUUID();
            UUID transactionId = UUID.randomUUID();
            String payload = TestDataBuilder.buildValidEventJson(orderId, transactionId, statusMensagem);
            sqsTestSupport.sendMessage(orderCallbackQueue, payload);
        }
    }

    @Então("todas as {int} mensagens devem ser consumidas com sucesso")
    public void todasAsMensagensDevemSerConsumidasComSucesso(int expectedCount) {
        await()
                .atMost(15, SECONDS)
                .pollInterval(1, SECONDS)
                .untilAsserted(() -> {
                    long count = eventRepository.count();
                    assertThat(count).isEqualTo(expectedCount);
                });
    }

    @E("devem existir {int} eventos salvos no banco de dados")
    public void devemExistirEventosSalvosNoBancoDeDados(int expectedCount) {
        await()
                .atMost(10, SECONDS)
                .pollInterval(1, SECONDS)
                .untilAsserted(() -> {
                    long count = eventRepository.count();
                    assertThat(count).isEqualTo(expectedCount);
                });
    }

    @E("os eventos salvos devem ter status {string}")
    public void osEventosSalvosDevemTerStatus(String expectedStatus) {
        await()
                .atMost(10, SECONDS)
                .pollInterval(1, SECONDS)
                .untilAsserted(() -> {
                    List<Event> events = eventRepository.findAll();
                    assertThat(events).isNotEmpty();
                    assertThat(events).allMatch(e -> expectedStatus.equals(e.getStatus()));
                });
    }

    @Quando("eu publico uma mensagem com tipo {string} na fila")
    public void euPublicoUmaMensagemComTipoNaFila(String tipoProblema) {
        String payload;
        switch (tipoProblema) {
            case "json_invalido":
                payload = TestDataBuilder.buildMalformedJson();
                break;
            case "sem_orderId":
                payload = TestDataBuilder.buildInvalidEventJsonMissingOrderId();
                break;
            case "evento_nulo":
                payload = "null";
                break;
            default:
                throw new IllegalArgumentException("Tipo de problema desconhecido: " + tipoProblema);
        }

        sqsTestSupport.sendMessage(orderCallbackQueue, payload);
        // Aguarda processamento
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Então("a mensagem deve ser consumida mas descartada")
    public void aMensagemDeveSerConsumidaMasDescartada() {
        long count = eventRepository.count();
        assertThat(count).isZero();
    }

    @E("nenhum evento deve ser salvo no banco de dados")
    public void nenhumEventoDeveSerSalvoNoBancoDeDados() {
        long count = eventRepository.count();
        assertThat(count).isZero();
    }

    @E("o resultado do processamento deve ser {string}")
    public void oResultadoDoProcessamentoDeveSer(String resultadoEsperado) {
        if ("descartada_sem_erro".equals(resultadoEsperado)) {
            // Verifica que não há eventos salvos
            assertThat(eventRepository.count()).isZero();
        }
    }

    @Quando("mensagens com diferentes status são publicadas na fila:")
    public void mensagensComDiferentesStatusSaoPublicadasNaFila(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            String status = row.get("status");
            UUID orderId = UUID.randomUUID();
            UUID transactionId = UUID.randomUUID();
            String payload = TestDataBuilder.buildValidEventJson(orderId, transactionId, status);
            sqsTestSupport.sendMessage(orderCallbackQueue, payload);
        }
    }

    @Então("todas as mensagens devem ser processadas")
    public void todasAsMensagensDevemSerProcessadas() {
        await()
                .atMost(15, SECONDS)
                .pollInterval(1, SECONDS)
                .untilAsserted(() -> {
                    long count = eventRepository.count();
                    assertThat(count).isGreaterThan(0);
                });
    }

    @E("devem existir {int} eventos salvos com os respectivos status")
    public void devemExistirEventosSalvosComOsRespectivosStatus(int expectedCount) {
        await()
                .atMost(15, SECONDS)
                .pollInterval(1, SECONDS)
                .untilAsserted(() -> {
                    long count = eventRepository.count();
                    assertThat(count).isEqualTo(expectedCount);
                });

        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(expectedCount);
        assertThat(events).extracting(Event::getStatus)
                .containsExactlyInAnyOrder("PROCESSED", "PENDING", "EXPIRED");
    }
}

