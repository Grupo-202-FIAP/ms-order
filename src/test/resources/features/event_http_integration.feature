# language: pt

Funcionalidade: API de Eventos - Testes de Integração HTTP

  Esquema do Cenário: Buscar evento por filtros
    Dado que o servidor está rodando para eventos
    E que tenho um "<orderId>" e um "<transactionId>" validos
    Quando envio uma requisição GET para o endpoint "<endpoint>" "<tipo_filtro>"
    Entao devo receber uma resposta de evento com status "<status_code>"
    E a resposta deve conter um evento com id válido
    E "<validacao_orderId>"
    E "<validacao_transactionId>"
    Exemplos:
      | orderId   | transactionId | endpoint          | tipo_filtro                 | status_code | validacao_orderId                                           | validacao_transactionId                                           |
      | ORDER-123 | TX-456        | /api/event/filter | com orderId                 | 200         | a resposta deve conter um evento com orderId correspondente | a resposta deve conter um evento com transactionId correspondente |
      | ORDER-123 | TX-456        | /api/event/filter | com transactionId           | 200         | a resposta deve conter um evento com orderId correspondente | a resposta deve conter um evento com transactionId correspondente |
      | ORDER-123 | TX-456        | /api/event/filter | com orderId e transactionId | 200         | a resposta deve conter um evento com orderId correspondente | a resposta deve conter um evento com transactionId correspondente |

  Esquema do Cenário: Listar todos os eventos
    Dado que o servidor está rodando para eventos
    Quando envio uma requisição GET para "<endpoint>" sem parâmetros
    Entao devo receber uma resposta de evento com status "<status_code>"
    E a resposta deve conter uma lista de eventos
    E a lista deve conter pelo menos "<quantidade_minima>" evento
    Exemplos:
      | endpoint       | status_code | quantidade_minima |
      | /api/event/all | 200         | 0                 |
