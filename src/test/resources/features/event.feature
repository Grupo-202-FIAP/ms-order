# language: pt
Funcionalidade: Gerenciamento completo de eventos

  Contexto:
    Dado que o serviço de eventos está disponível
    E que o banco de dados de eventos está limpo

  Esquema do Cenário: Buscar evento por filtros com sucesso
    Dado que existe um evento cadastrado com "<tipoFiltro>" igual a "<valorFiltro>"
    Quando eu envio uma requisição GET para buscar evento por "<tipoFiltro>" igual a "<valorFiltro>"
    Então o status HTTP da resposta deve ser 200
    E o evento retornado deve ter "<tipoFiltro>" igual a "<valorFiltro>"

    Exemplos:
      | tipoFiltro    | valorFiltro                           |
      | orderId       | 123e4567-e89b-12d3-a456-426614174000 |
      | transactionId | 123e4567-e89b-12d3-a456-426614174100 |

  Esquema do Cenário: Falha ao buscar evento inexistente
    Quando eu busco evento por "<tipoFiltro>" com valor "<valor>"
    Então o status HTTP da resposta deve ser 404
    E a mensagem de erro deve conter "Evento não encontrado"

    Exemplos:
      | tipoFiltro    | valor                                 |
      | orderId       | 999e4567-e89b-12d3-a456-426614174999 |
      | transactionId | 999e4567-e89b-12d3-a456-426614174999 |

  Esquema do Cenário: Listar eventos com diferentes quantidades
    Dado que existem <quantidadeInicial> eventos cadastrados no sistema
    Quando eu crio mais <quantidadeAdicional> eventos adicionais
    E eu envio uma requisição GET para listar todos os eventos
    Então o status HTTP da resposta deve ser 200
    E a lista de eventos deve conter <quantidadeTotal> eventos

    Exemplos:
      | quantidadeInicial | quantidadeAdicional | quantidadeTotal |
      | 5                 | 0                   | 5               |
      | 2                 | 3                   | 5               |
