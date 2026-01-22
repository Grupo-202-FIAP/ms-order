# language: pt
Funcionalidade: Processamento de mensagens SQS pelo EventConsumer

  Contexto:
    Dado que o serviço de consumer está disponível
    E que o banco de dados de eventos está limpo
    E que as filas SQS estão configuradas

  Esquema do Cenário: Processar mensagens válidas com diferentes status
    Quando eu publico <quantidade> mensagens válidas na fila com status "<statusMensagem>"
    Então todas as <quantidade> mensagens devem ser consumidas com sucesso
    E devem existir <quantidade> eventos salvos no banco de dados
    E os eventos salvos devem ter status "<statusMensagem>"

    Exemplos:
      | quantidade | statusMensagem |
      | 1          | PROCESSED      |
      | 3          | PROCESSED      |
      | 1          | PENDING        |
      | 1          | EXPIRED        |

  Esquema do Cenário: Descartar mensagens inválidas
    Quando eu publico uma mensagem com tipo "<tipoProblema>" na fila
    Então a mensagem deve ser consumida mas descartada
    E nenhum evento deve ser salvo no banco de dados
    E o resultado do processamento deve ser "<resultadoEsperado>"

    Exemplos:
      | tipoProblema  | resultadoEsperado     |
      | json_invalido | descartada_sem_erro   |
      | sem_orderId   | descartada_sem_erro   |
      | evento_nulo   | descartada_sem_erro   |

  Cenário: Processar mensagens com múltiplos status diferentes
    Quando mensagens com diferentes status são publicadas na fila:
      | status    |
      | PROCESSED |
      | PENDING   |
      | EXPIRED   |
    Então todas as mensagens devem ser processadas
    E devem existir 3 eventos salvos com os respectivos status
