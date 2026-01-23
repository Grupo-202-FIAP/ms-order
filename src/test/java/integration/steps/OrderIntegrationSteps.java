package integration.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextime.order.domain.enums.OrderStatus;
import com.nextime.order.infrastructure.controller.dto.request.OrderItemRequest;
import com.nextime.order.infrastructure.controller.dto.request.OrderRequest;
import com.nextime.order.infrastructure.controller.dto.response.OrderResponse;
import com.nextime.order.infrastructure.persistence.document.Order;
import com.nextime.order.infrastructure.persistence.repository.IOrderRepository;
import integration.consumer.ConsumeMessage;
import integration.context.TestContext;
import integration.utils.SqsTestSupport;
import integration.utils.TestDataBuilder;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.services.sqs.SqsClient;

public class OrderIntegrationSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IOrderRepository orderRepository;

    @Autowired
    private SqsTestSupport sqsTestSupport;

    @Autowired
    private SqsClient sqsClient;

    @Autowired
    private ConsumeMessage consumeMessage;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestContext testContext;

    @Value("${spring.sqs.queues.order-queue}")
    private String orderQueue;

    @Value("${spring.sqs.queues.order-callback-queue}")
    private String orderCallbackQueue;

    private OrderResponse createdOrder;
    private List<OrderResponse> orderList;

    @Dado("que o serviço de pedidos está disponível")
    public void queOServicoDePedidosEstaDisponivel() {
        // O serviço já está iniciado pelo Spring Boot Test
        assertThat(restTemplate).isNotNull();
    }

    @E("que o banco de dados está limpo")
    public void queOBancoDeDadosEstaLimpo() {
        orderRepository.deleteAll();
    }

    @E("que as filas SQS estão limpas")
    public void queAsFilasSqsEstaoLimpas() {
        sqsTestSupport.purgeQueue(orderQueue);
        sqsTestSupport.purgeQueue(orderCallbackQueue);
    }

    @Quando("eu crio um pedido com tipo {string}")
    public void euCrioUmPedidoComTipo(String tipoPedido) {
        UUID customerId = UUID.randomUUID();
        OrderRequest request;
        ResponseEntity<String> response;

        switch (tipoPedido) {
            case "valido":
                List<OrderItemRequest> item = List.of(
                        TestDataBuilder.buildOrderItemRequest(1L, 2, new BigDecimal("50.00"))
                );
                request = TestDataBuilder.buildValidOrderRequest(customerId, item);
                response = restTemplate.postForEntity("/api/order/create", request, String.class);
                testContext.setLastResponse(response);
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    try {
                        objectMapper.findAndRegisterModules();
                        createdOrder = objectMapper.readValue(response.getBody(), OrderResponse.class);
                    } catch (Exception e) {
                        createdOrder = null;
                    }
                }
                break;

            case "multiplos_itens":
                List<OrderItemRequest> items = List.of(
                        TestDataBuilder.buildOrderItemRequest(1L, 2, new BigDecimal("50.00")),
                        TestDataBuilder.buildOrderItemRequest(2L, 1, new BigDecimal("100.00")),
                        TestDataBuilder.buildOrderItemRequest(3L, 3, new BigDecimal("25.00"))
                );
                request = TestDataBuilder.buildValidOrderRequest(customerId, items);
                response = restTemplate.postForEntity("/api/order/create", request, String.class);
                testContext.setLastResponse(response);
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    try {
                        objectMapper.findAndRegisterModules();
                        createdOrder = objectMapper.readValue(response.getBody(), OrderResponse.class);
                    } catch (Exception e) {
                        createdOrder = null;
                    }
                }
                break;

            case "sem_itens":
                request = OrderRequest.builder()
                        .customerId(customerId)
                        .items(List.of())
                        .build();
                response = restTemplate.postForEntity("/api/order/create", request, String.class);
                testContext.setLastResponse(response);
                break;

            case "itens_nulos":
                request = OrderRequest.builder()
                        .customerId(customerId)
                        .items(null)
                        .build();
                response = restTemplate.postForEntity("/api/order/create", request, String.class);
                testContext.setLastResponse(response);
                break;

            default:
                throw new IllegalArgumentException("Tipo de pedido desconhecido: " + tipoPedido);
        }
    }

    @Então("o resultado da criação deve ser {string}")
    public void oResultadoDaCriacaoDeveSer(String resultado) {
        ResponseEntity<?> response = testContext.getLastResponse();
        assertThat(response).withFailMessage("Resposta HTTP não foi armazenada no contexto").isNotNull();
        
        int statusCode = response.getStatusCode().value();
        String responseBody = response.getBody() != null ? response.getBody().toString() : "null";
        
        if ("sucesso".equals(resultado)) {
            assertThat(response.getStatusCode().is2xxSuccessful())
                    .withFailMessage("Esperava status 2xx mas recebeu %d. Body: %s", statusCode, responseBody)
                    .isTrue();
        } else if ("erro".equals(resultado)) {
            assertThat(response.getStatusCode().isError())
                    .withFailMessage("Esperava status de erro mas recebeu %d. Body: %s", statusCode, responseBody)
                    .isTrue();
        }
    }

    @E("a resposta deve validar {string}")
    public void aRespostaDeveValidar(String validacao) {
        if ("pedido criado".equals(validacao)) {
            assertThat(createdOrder).isNotNull();
            assertThat(createdOrder.id()).isNotNull();
        } else if ("pedido com 3 itens".equals(validacao)) {
            assertThat(createdOrder).isNotNull();
            assertThat(createdOrder.items()).hasSize(3);
        } else if (validacao.contains("Lista de itens inválida")) {
            ResponseEntity<?> response = testContext.getLastResponse();
            assertThat(response).isNotNull();
            String responseBody = (String) response.getBody();
            assertThat(responseBody).contains("Lista de itens inválida");
        }
    }

    @Dado("que existem {int} pedidos cadastrados no sistema")
    public void queExistemPedidosCadastradosNoSistema(int count) {
        for (int i = 0; i < count; i++) {
            Order order = TestDataBuilder.buildOrder(UUID.randomUUID(), OrderStatus.RECEIVED);
            orderRepository.save(order);
        }
    }

    @Quando("eu envio uma requisição GET para listar todos os pedidos")
    public void euEnvioUmaRequisicaoGetParaListarTodosOsPedidos() {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/order",
                HttpMethod.GET,
                null,
                String.class
        );
        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                objectMapper.findAndRegisterModules();
                OrderResponse[] array = objectMapper.readValue(response.getBody(), OrderResponse[].class);
                orderList = array != null ? List.of(array) : List.of();
            } catch (Exception e) {
                orderList = List.of();
            }
        } else {
            orderList = List.of();
        }
    }

    @Dado("que existem os seguintes pedidos cadastrados:")
    public void queExistemOsSeguintesPedidosCadastrados(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            UUID customerId = UUID.fromString(row.get("customerId"));
            OrderStatus status = OrderStatus.valueOf(row.get("status"));
            Order order = TestDataBuilder.buildOrder(customerId, status);
            orderRepository.save(order);
        }
    }

    @Dado("que existem pedidos com os status {string}")
    public void queExistemPedidosComOsStatus(String statusList) {
        String[] statuses = statusList.split(",");
        for (String status : statuses) {
            UUID customerId = UUID.randomUUID();
            OrderStatus orderStatus = OrderStatus.valueOf(status.trim());
            Order order = TestDataBuilder.buildOrder(customerId, orderStatus);
            orderRepository.save(order);
        }
    }

    @E("todos os pedidos retornados devem ter status {string}")
    public void todosOsPedidosRetornadosDevemTerStatus(String expectedStatus) {
        OrderStatus status = OrderStatus.valueOf(expectedStatus);
        assertThat(orderList).allMatch(order -> order.status() == status);
    }

    @Quando("eu envio uma requisição GET para listar pedidos com status {string}")
    public void euEnvioUmaRequisicaoGetParaListarPedidosComStatus(String status) {
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/order/status?status=" + status,
                HttpMethod.GET,
                null,
                String.class
        );
        testContext.setLastResponse(response);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                objectMapper.findAndRegisterModules();
                OrderResponse[] array = objectMapper.readValue(response.getBody(), OrderResponse[].class);
                orderList = array != null ? List.of(array) : List.of();
            } catch (Exception e) {
                orderList = List.of();
            }
        } else {
            orderList = List.of();
        }
    }

    @Então("o pedido deve ser criado com sucesso")
    public void oPedidoDeveSerCriadoComSucesso() {
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.id()).isNotNull();
    }

    @E("uma mensagem deve ser publicada na fila SQS {string}")
    public void umaMensagemDeveSerPublicadaNaFilaSqs(String queueName) throws InterruptedException {
        // Aguarda um pouco para a mensagem ser publicada
        Thread.sleep(1000);
        String queueUrl = sqsTestSupport.resolveQueueUrl(queueName);
        List<String> messages = consumeMessage.receiveMessages(sqsClient, queueUrl, 1);
        assertThat(messages).isNotEmpty();
    }

    @E("o pedido deve conter {int} itens")
    public void oPedidoDeveConterItens(int expectedItemCount) {
        assertThat(createdOrder.items()).hasSize(expectedItemCount);
    }

    @E("a lista deve conter {int} pedidos")
    public void aListaDeveConterPedidos(int expectedCount) {
        ResponseEntity<?> response = testContext.getLastResponse();
        int statusCode = response != null ? response.getStatusCode().value() : 0;
        String responseBody = response != null && response.getBody() != null ? response.getBody().toString() : "null";
        
        assertThat(orderList)
                .withFailMessage("Esperava %d pedidos mas encontrou %d. Status HTTP: %d, Body: %s", 
                        expectedCount, orderList.size(), statusCode, responseBody)
                .hasSize(expectedCount);
    }

    @E("a lista deve estar vazia")
    public void aListaDeveEstarVazia() {
        assertThat(orderList).isEmpty();
    }

    @E("todos os pedidos devem ter status {string}")
    public void todosOsPedidosDevemTerStatus(String expectedStatus) {
        OrderStatus status = OrderStatus.valueOf(expectedStatus);
        assertThat(orderList).allMatch(order -> order.status() == status);
    }
}

