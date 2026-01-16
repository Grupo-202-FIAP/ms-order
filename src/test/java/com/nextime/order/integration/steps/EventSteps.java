package com.nextime.order.integration.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.E;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EventSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private List<?> eventos;
    private String orderId;
    private String transactionId;

    @Dado("que o servidor está rodando para eventos")
    public void servidorEstaRodandoParaEventos() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Quando("envio uma requisição GET para {string} sem parâmetros")
    public void envioUmaRequisicaoGetSemParametros(String endpoint) {
        response = RestAssured
                .given()
                .accept("application/json")
                .when()
                .get(endpoint);
    }

    @Dado("que tenho um {string} e um {string} validos")
    public void queTenhoOrderIdETransactionIdValidos(String orderId, String transactionId) {
        this.orderId = orderId;
        this.transactionId = transactionId;
    }

    @Quando("envio uma requisição GET para o endpoint {string} {string}")
    public void envioRequisicaoGetParaEndpointComFiltro(String endpoint, String tipoFiltro) {

        var request = RestAssured
                .given()
                .accept("application/json");

        switch (tipoFiltro) {
            case "com orderId" ->
                    request.queryParam("orderId", orderId);

            case "com transactionId" ->
                    request.queryParam("transactionId", transactionId);

            case "com orderId e transactionId" ->
                    request.queryParam("orderId", orderId)
                            .queryParam("transactionId", transactionId);

            default ->
                    throw new IllegalArgumentException("Tipo de filtro inválido: " + tipoFiltro);
        }

        response = request.get(endpoint);
    }

    @Entao("devo receber uma resposta com status {int}")
    public void devoReceberUmaRespostaComStatus(Integer statusCode) {
        assertThat(response.getStatusCode(), equalTo(statusCode));
    }

    @Entao("devo receber uma resposta de evento com status {int}")
    public void devoReceberUmaRespostaDeEventoComStatus(Integer statusCode) {
        assertThat(response.getStatusCode(), equalTo(statusCode));
    }

    @E("a resposta deve conter uma lista de eventos")
    public void respostaDeveConterListaDeEventos() {
        eventos = response.jsonPath().getList("$");
        assertThat(eventos, notNullValue());
        assertThat(eventos, instanceOf(List.class));
    }

    @E("a lista deve conter pelo menos {int} evento")
    public void listaDeveConterPeloMenosEventos(Integer quantidadeMinima) {
        assertThat(eventos.size(), greaterThanOrEqualTo(quantidadeMinima));
    }

    @E("a resposta deve conter um evento com id válido")
    public void respostaDeveConterEventoComIdValido() {
        String id = response.jsonPath().getString("id");
        assertThat(id, notNullValue());
        assertThat(id, not(isEmptyString()));
    }

    @E("a resposta deve conter um evento válido")
    public void respostaDeveConterEventoValido() {
        Map<String, Object> evento = response.jsonPath().getMap("$");
        assertThat(evento, notNullValue());
        assertThat(evento.isEmpty(), is(false));
    }

    @E("a resposta deve conter um evento com orderId correspondente")
    public void respostaDeveConterEventoComOrderIdCorrespondente() {
        String responseOrderId = response.jsonPath().getString("orderId");
        assertThat(responseOrderId, equalTo(orderId));
    }

    @E("a resposta deve conter um evento com transactionId correspondente")
    public void respostaDeveConterEventoComTransactionIdCorrespondente() {
        String responseTransactionId = response.jsonPath().getString("transactionId");
        assertThat(responseTransactionId, equalTo(transactionId));
    }
}
