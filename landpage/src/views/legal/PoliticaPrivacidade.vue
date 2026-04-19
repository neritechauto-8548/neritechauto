<template>
  <LayoutLegal title="Política de Privacidade" lastUpdated="18 de Abril de 2026">
    <template #navigation>
      <a href="#introducao">1. Introdução</a>
      <a href="#dados-coletados">2. Dados Coletados</a>
      <a href="#uso-dados">3. Uso dos Dados</a>
      <a href="#compartilhamento">4. Compartilhamento</a>
      <a href="#seguranca">5. Segurança</a>
      <a href="#seus-direitos">6. Seus Direitos</a>
    </template>
    
    <template #content>
      <section id="introducao">
        <h2>1. Introdução</h2>
        <p>A NeriTechAuto Sistemas ("nós", "nosso" ou "NeriTechAuto") valoriza a sua privacidade. Esta Política de Privacidade descreve como coletamos, usamos e protegemos suas informações pessoais quando você utiliza nossa plataforma de gestão para oficinas mecânicas.</p>
        <p>Ao utilizar nossos serviços, você concorda com a coleta e o uso de informações de acordo com esta política, em total conformidade com a Lei Geral de Proteção de Dados (LGPD - Lei nº 13.709/2018).</p>
      </section>

      <section id="dados-coletados">
        <h2>2. Dados Coletados</h2>
        <h3>2.1 Dados de Cadastro</h3>
        <p>Coletamos informações como nome completo, e-mail, CPF/CNPJ, nome da empresa, endereço comercial e telefone/WhatsApp.</p>
        <h3>2.2 Dados de Transação</h3>
        <p>Para processamento de pagamentos, utilizamos o Stripe. Seus dados de cartão de crédito não são armazenados em nossos servidores, sendo processados de forma criptografada pelo Stripe.</p>
        <h3>2.3 Dados de Uso do Sistema</h3>
        <p>Armazenamos os dados que você insere no sistema (ordens de serviço, cadastros de clientes da sua oficina, estoque, etc.) para a prestação do serviço contratado.</p>
      </section>

      <section id="uso-dados">
        <h2>3. Uso dos Dados</h2>
        <p>Utilizamos os dados coletados para:</p>
        <ul>
          <li>Prover e manter o funcionamento do sistema NeriTech;</li>
          <li>Notificar você sobre alterações em nossos serviços;</li>
          <li>Processar pagamentos de assinaturas;</li>
          <li>Fornecer suporte técnico via WhatsApp ou e-mail;</li>
          <li>Garantir a segurança da plataforma e prevenir fraudes.</li>
        </ul>
      </section>

      <section id="compartilhamento">
        <h2>4. Compartilhamento de Dados</h2>
        <p>Não vendemos seus dados para terceiros. O compartilhamento ocorre apenas com parceiros essenciais para o serviço:</p>
        <ul>
          <li><strong>AWS (Amazon Web Services):</strong> Para armazenamento seguro em nuvem;</li>
          <li><strong>Stripe:</strong> Para processamento de pagamentos e assinaturas;</li>
          <li><strong>Serviços de E-mail:</strong> Para envio de notificações de sistema e chaves de acesso.</li>
        </ul>
      </section>

      <section id="seguranca">
        <h2>5. Segurança</h2>
        <p>A segurança dos seus dados é nossa prioridade. Implementamos:</p>
        <ul>
          <li>Criptografia SSL (HTTPS) em todas as comunicações;</li>
          <li>Backups diários automáticos;</li>
          <li>Controle de acesso rigoroso à base de dados;</li>
          <li>Conformidade com padrões de segurança bancária via Stripe.</li>
        </ul>
      </section>

      <section id="seus-direitos">
        <h2>6. Seus Direitos (LGPD)</h2>
        <p>Como titular dos dados, você tem direito a:</p>
        <ul>
          <li>Confirmar a existência de tratamento de seus dados;</li>
          <li>Acessar seus dados armazenados;</li>
          <li>Corrigir dados incompletos ou inexatos;</li>
          <li>Solicitar a exclusão de seus dados (respeitando obrigações legais de guarda de documentos fiscais).</li>
        </ul>
      </section>
    </template>
  </LayoutLegal>
</template>

<script setup>
import LayoutLegal from '../../components/LayoutLegal.vue';
</script>
