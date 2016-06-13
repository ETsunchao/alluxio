---
layout: global
title: Configurando o Alluxio com JSS
nickname: Alluxio com JSS
group: Under Store
priority: 4
---

Este guia descreve como configurar o Alluxio com o [Jcloud JSS](http://www.jcloud.com/products/cloudstorage.html) 
como um sistema de armazenamento inferior. O `Object Storage Service` (JSS) é massivo, seguro e altamente 
confiável como um serviço de armazenamento na nuvem fornecido pela Jcloud.

## Configuração Inicial

Para executar o Alluxio `cluster` em um conjunto de máquina, você deve implantar os binários do Alluxio em 
cada um de seus servidores. Você pode também 
[compilar os binários a partir do código fonte](http://alluxio.org/documentation/master/Building-Alluxio-Master-Branch.html) 
ou [baixar o binário pré-compilador diretamente](http://alluxio.org/documentation/master/Running-Alluxio-Locally.html).

Então se você não ainda não criou o arquivo de configuração do `template`, faça-o:

{% include Common-Commands/copy-alluxio-env.md %}

Também, para preparar a utilização do `JSS` com o Alluxio, crie um `bucket` ou utilize um já existente. Você também deve 
tomar nota do diretório que você quer utilizar no `bucket`, ou pela criação de um novo diretório no `bucket` ou por utilizar 
um já existente. Para intenção deste guia, o nome do `bucket JSS` será chamado de `JSS_BUCKET` e o diretório será 
chamado de `JSS_DIRECTORY`. Também, para utilizar um serviço JSS, você deve prover um `endpoint JSS` para especificar 
uma variedade do `bucket`. O `endpoint` será chamado de `JSS_ENDPOINT` e para aprender mais sobre `endpoints` de uma 
variedade especial, você pode ver [aqui](http://jcloud.com/help/store_qa.html#qa2). 
Para maiores informações sobre `JSS Bucket`, por favor, visite 
[aqui](http://jcloud.com/help/store_sum.html#jbgn)

## Configurando o Alluxio

Para configurar o Alluxio no uso do `JSS` como um sistema de armazenamento inferior, devem ser feitas alterações no arquivo 
`conf/alluxio-env.sh`. A primeira alteração é para especificar um `JSS bucket` existente e um diretório como um sistema
de armazenamento inferior. Você pode especificar um modificando o `conf/alluxio-env.sh` para incluir:

{% include Configuring-Alluxio-with-JSS/underfs-address.md %}
    
Em seguida, você pode especificar as credenciais de acesso `JSS`. Na seção `ALLUXIO_JAVA_OPTS` do arquivo 
`conf/alluxio-env.sh`, adicione:

{% include Configuring-Alluxio-with-JSS/jss-access.md %}
    
Aqui, `<JSS_ACCESS_KEY>` e `<JSS_SECRET_KEY>` devem ser substituídos pelas atuais 
[chaves do Jcloud](http://jcloud.com/help/store_sum.html#jbgn) e outras variáveis de ambiente que contém suas credenciais.
O `<JSS_ENDPOINT>` para o seu `JSS range` pode ser obtido 
[aqui](http://jcloud.com/help/store_qa.html#qa2). 

Se você não tiver certeza de como alterar o arquivo `conf/alluxio-env.sh`, existe outra forma de prove esta configuração. 
Você pode prover o arquivo de propriedades de configuração nomeado: `alluxio-site.properties` no diretório `conf/` 
e edita-lo conforme abaixo:

{% include Configuring-Alluxio-with-JSS/properties.md %}

Depois desta alterações, o Aluuxio deve estar configurado para trabalhar com o `JSS` como `under storage system` e você 
pode rodar o Alluxio locamente com o `JSS`.

## Configurando Aplicações Distribuídas

Se você estiver utilizando um Alluxio `client` que está rodando separadamente do Alluxio `master` e dos `workers` 
(em um `JVM` separado), então você precisa ter certeza que as credenciais do Jcloud também estão fornecidas para os 
processos da aplicação `JVM`. A maneira mais fácil de fazer isso consiste em adicionar as opções na linha de comando 
quando inicializar um processo `client JVM`. Por exemplo:

{% include Configuring-Alluxio-with-JSS/java-bash.md %}

## Rodando o Alluxio Localmente com o JSS

Depois de tudo estar configurado, você pode inicializar o Alluxio localmente para ver se tudo está funcionando.

{% include Common-Commands/start-alluxio.md %}
    
Isto deve iniciar um Alluxio `master` e um Alluxio `worker`. Você pode acessar o `master UI` em http://localhost:19999.

Em seguida, você pode executar um programa de teste:

{% include Common-Commands/runTests.md %}
    
Depois de obter sucesso, você pode visitar seu diretório `JSS_BUCKET/JSS_DIRECTORY` para verificar que os arquivos e 
diretório criados pelo Alluxio existem. Para este teste, você deve ver os arquivos nomeados como:

{% include Configuring-Alluxio-with-JSS/jss-file.md %}

Para parar o Alluxio, você deve executar:

{% include Common-Commands/stop-alluxio.md %}
