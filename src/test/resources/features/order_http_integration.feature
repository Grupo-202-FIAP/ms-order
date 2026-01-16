# language: pt

Funcionalidade: API de Pedidos - Testes de Integração HTTP

  Esquema do Cenário: Criar pedido com sucesso
    Dado que o servidor está rodando
    E que tenho um pedido válido com "<quantidade_itens>" item
    Quando envio uma requisição POST para o endpoint "<endpoint>"
    Entao devo receber uma resposta com status "<status_code>"
    E a resposta deve conter um pedido com id válido
    E a resposta deve conter um pedido com status "<order_status>"
    E a resposta deve conter um pedido com "<quantidade_itens>" item
    Exemplos:
      | quantidade_itens | endpoint          | status_code | order_status |
      | 1                | /api/order/create | 200         | RECEIVED     |
      | 3                | /api/order/create | 200         | RECEIVED     |
      | 5                | /api/order/create | 200         | RECEIVED     |

  Esquema do Cenário: Listar pedidos por status
    Dado que o servidor está rodando
    Quando envio uma requisição GET para o endpoint "<endpoint>" com status "<status>"
    Entao devo receber uma resposta com status 200
    E a resposta deve conter uma lista de pedidos
    E todos os pedidos na lista devem ter status "<status>"
    Exemplos:
      | endpoint          | status    |
      | /api/order/status | RECEIVED  |
      | /api/order/status | PREPARING |
      | /api/order/status | READY     |
      | /api/order/status | COMPLETED |
      | /api/order/status | CANCELLED |

  Esquema do Cenário: Listar todos os pedidos
    Dado que o servidor está rodando
    Quando envio uma requisição GET para "<endpoint>"
    Entao devo receber uma resposta com status "<status_code>"
    E a resposta deve conter uma lista de pedidos
    E a lista deve conter pelo menos "<quantidade_minima>" pedido
    Exemplos:
      | endpoint   | status_code | quantidade_minima |
      | /api/order | 200         | 0                 |

  Esquema do Cenário: Criar pedido inválido
    Dado que o servidor está rodando
    E que tenho um pedido inválido "<tipo_invalidez>"
    Quando envio uma requisição POST para o endpoint "<endpoint>"
    Entao devo receber uma resposta com status "<status_code>"
    Exemplos:
      | tipo_invalidez | endpoint          | status_code |
      | sem itens      | /api/order/create | 400         |
      | sem customerId | /api/order/create | 400         |
