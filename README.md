# Java - Bean Info Generator

Generate static information about Java Beans

## Observa��es
- Atualmente gera informa��es de matados apenas dos fields.

## Executar (testar/compilar) em tempo de desenvolvimento
- Executar, via linha de comando: mvnDebug clean install;
- O maven vai exibir: Listening for transport dt_socket at address: 8000;
- Colocar breakpoints no c�digo;
- Em uma IDE (ex: Eclipse) iniciar o debug remoto para localhost:8000;
- O maven vai continuar a execu��o e na IDE vai parar no breakpoint.