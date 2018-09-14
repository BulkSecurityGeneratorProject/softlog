# Softplan - LogValue
Esta é uma aplicação para cálculos de fretes fictícios.


## Live Demo

Se desejas utilizar esta aplicação agora, ela está disponível no seguinte endereço:

[LogValue live demo](https://maven.apache.org/download.cgi)

Apenas um detalhe: Ela está em um plano free do Heroku e nele os Dynos costumam de dormir no meio do expediente. Caso a aplicação demore para responder ao primeiro request, calma, os Dynos já acordarão rapidinho e a aplicação funcionará normalmente. 

## Requisitos para desenvolvimento
Esta aplicação foi homologada com o seguinte ambiente:
 
 - Java 8
 - [Maven 3.5.0](https://maven.apache.org/download.cgi)
 - [Node.js 8.11.3](https://nodejs.org/en/) (não funcionará com o NodeJs 10.x)
 - [Yarn 1.9.4](https://yarnpkg.com/lang/en/docs/install/#debian-stable)
 
 Estes são os requisitos obrigatórios, mas o Docker também poderá ser utilizado, opcionalmente, como veremos mais a frente. 

## Desenvolvimento

Antes de *buildar* este projeto, você deve instalar e configurar Node.js e o Yarn en sua estação:

1. Node.js: Usamos o Node para executar um servidor da Web de desenvolvimento e construir o projeto.
2. Yarn: Usamos o Yarn para gerenciar as dependências do Node.

Depois de instalar o Node.js, você poderá executar o seguinte comando para instalar ferramentas de desenvolvimento. 
Você só precisará executar este comando logo após o clone do projeto ou quando as dependências mudarem em package.json.

    yarn install

Execute os seguintes comandos **em dois terminais separados** para iniciar a aplicação back-end e front-end. 
Dessa forma você terá uma boa experiência de desenvolvimento, onde a apliação e seu navegador
serão atualizados automaticamente conforme os arquivos são alterados.

    ./mvnw
    yarn start

Após o comando `./mvnw`, a interface do projeto será exibiba no endereço [http://localhost:8080](http://localhost:8080). 
Após o comando `yarn start`, o browser exibirá o interface do projeto no endereço [http://localhost:9000](http://localhost:9000). Este é o seu endereço para desenvolvimento do front-end.


#### Troubleshooting#1
Após o clone do projeto ou um `clean` ser executado, o webpack precisará reconstruir algumas estrututas da visão que são utilizadas no ambiente de desenvolvimento, livereload etc. Para isso, utilize o seguinte comando 
para iniciar o back-end da aplicação:

	./mvnw -P webpack

As demais vezes em que o back-end for iniciado, não será mais necessário utilizar esse profile específico. 

Isso não será necessário caso o front-end seja inicado com o yarn. Utilizando o comando `yarn start` para iniciar o front-end.


 #### Troubleshooting#2 
 
 Durante o build do projeto, podem ocorrer problemas com uma dependencia chamada **node-sass** [[BUG](https://github.com/sass/node-sass/issues/2032)].
Caso isso ocorra, utilize o seguinte comando:

    yarn

Ele fará um reBuild da arvore de dependências da visão. 


### PWA

Esta aplicação contém um Service Workers simples. Não exatamente a torna uma PSW, mas experimente instalá-la em seu [desktop](https://developers.google.com/web/updates/2018/05/dpwa) ou [celular](https://developers.google.com/web/fundamentals/app-install-banners/) quando fizer um build de Produção.


## Bancos de dados

Esta aplicação utiliza dois bancos de dados. 
Para desenvolvimento, no profile **dev**, um bando H2 armazenará os dados no disco, dentro da pasta target do projeto. 

Para a produção, um banco **PostgreSQL** será utilizado. Ads credenciais desse banco devem ser configuradas no arquivo [application-prod.yml](/src/main/resources/config/application-prod.yml). Este arquivo está configurado para que o Docker forneca este banco de dados.


## Build para Produção

Para otimizar a aplicação para o ambiente de produção, utilize os comando:

    ./mvnw -Pprod clean package

Ele irá concatenar e minificar todos os CSS e JavaScripts do cliente e atualizar as referências no `index.html`.

Então, para saber se tudo esta funcionando bem, execute o `war` da aplicação rodando: 

    java -jar target/logvalue.war


Então navegue para [http://localhost:8080](http://localhost:8080) e veja a mágica. Claro, seria legal configurar o banco de dados antes.


## Testing

Para disparar os testes da aplicação, use:

    ./mvnw clean test

### Client tests

Os testes unitários serão executados pelo [Jest][] e escritos com [Jasmine][]. Ele podem ser executados com:

    yarn test

Os testes de end-to-end da interface do usuário são fornecidos pelo [Protractor][]. Eles podem ser executados iniciando o Spring Boot em um terminal (`./mvnw spring-boot: run`) e executando os testes (`yarn run e2e`) em um segundo.


## Using Docker to simplify development (optional)

>>>>>> Testar isso e descrver 

Você pode usar o Docker para melhorar sua experiência de desenvolvimento do JHipster. Várias configurações do docker-compose estão disponíveis na pasta [src/main/docker](src/main/docker) para iniciar os serviços necessários para a aplicação.

Por exemplo, para iniciar um banco de dados postgresql em um contêiner docker, execute:

    docker-compose -f src/main/docker/postgresql.yml up -d

Para pará-lo e remover o contêiner, execute:

    docker-compose -f src/main/docker/postgresql.yml down

Você também pode dockerizar totalmente seu aplicativo e todos os serviços dos quais ele depende. Para conseguir isso, primeiro crie uma imagem do docker do seu aplicativo executando:

    ./mvnw verify -Pprod dockerfile:build dockerfile:tag@version dockerfile:tag@commit

E então execute:

    docker-compose -f src/main/docker/app.yml up -d

No final, uma imagem Docker de sua aplicação será criada e poderá ser *deployada* onde for necessário.

 ## Scaffolding
 Esta aplicação utilizou o JHipster 5.1.0 como scaffolding.

[JHipster Homepage and latest documentation](https://www.jhipster.tech)
[JHipster 5.1.0 archive](https://www.jhipster.tech/documentation-archive/v5.1.0)
