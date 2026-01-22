# language: pt
Funcionalidade: Gerenciamento completo de pedidos

  Contexto:
    Dado que o serviço de pedidos está disponível
    E que o banco de dados está limpo
    E que as filas SQS estão limpas

  Esquema do Cenário: Criar pedido com validações
    Quando eu crio um pedido com tipo "<tipoPedido>"
    Então o resultado da criação deve ser "<resultado>"
    E o status HTTP da resposta deve ser <statusHttp>
    E a resposta deve validar "<validacao>"

    Exemplos:
      | tipoPedido      | resultado | statusHttp | validacao               |
      | valido          | sucesso   | 200        | pedido criado           |
      | multiplos_itens | sucesso   | 200        | pedido com 3 itens      |
      | sem_itens       | erro      | 400        | Lista de itens inválida |
      | itens_nulos     | erro      | 400        | Lista de itens inválida |

  Esquema do Cenário: Listar pedidos com diferentes quantidades
    Dado que existem <quantidade> pedidos cadastrados no sistema
    Quando eu envio uma requisição GET para listar todos os pedidos
    Então o status HTTP da resposta deve ser 200
    E a lista deve conter <quantidade> pedidos

    Exemplos:
      | quantidade |
      | 0          |
      | 3          |
      | 5          |

  Esquema do Cenário: Listar pedidos filtrados por status
    Dado que existem pedidos com os status "<statusExistentes>"
    Quando eu envio uma requisição GET para listar pedidos com status "<statusFiltro>"
    Então o status HTTP da resposta deve ser 200
    E a lista deve conter <quantidadeEsperada> pedidos
    E todos os pedidos retornados devem ter status "<statusFiltro>"

    Exemplos:
      | statusExistentes                   | statusFiltro | quantidadeEsperada |
      | RECEIVED,RECEIVED,PREPARING        | RECEIVED     | 2                  |
      | RECEIVED,PREPARING,PREPARING,READY | PREPARING    | 2                  |
      | READY,COMPLETED                    | READY        | 1                  |
      | COMPLETED,COMPLETED,COMPLETED      | COMPLETED    | 3                  |
      | CANCELLED                          | CANCELLED    | 1                  |
      | RECEIVED                           | COMPLETED    | 0                  |
