<template>
  <LayoutLegal title="Política de Cookies" lastUpdated="18 de Abril de 2026">
    <template #navigation>
      <a href="#o-que-sao">1. O que são Cookies?</a>
      <a href="#como-usamos">2. Como usamos os Cookies?</a>
      <a href="#tipos">3. Tipos de Cookies</a>
      <a href="#controle">4. Como controlar Cookies?</a>
    </template>
    
    <template #content>
      <section id="o-que-sao">
        <h2>1. O que são Cookies?</h2>
        <p>Cookies são pequenos arquivos de texto enviados pelo site ao seu navegador e armazenados em seu dispositivo. Eles permitem que o site recorde informações sobre sua visita, facilitando o próximo acesso e tornando o site mais útil para você.</p>
      </section>

      <section id="como-usamos">
        <h2>2. Como usamos os Cookies?</h2>
        <p>A NeriTech utiliza cookies para entender como você interage com nosso site e plataforma, ajudando-nos a melhorar sua experiência, garantir a segurança do seu login e processar suas preferências de navegação.</p>
      </section>

      <section id="tipos">
        <h2>3. Tipos de Cookies que utilizamos</h2>
        
        <h3>3.1 Cookies Estritamente Necessários</h3>
        <p>Essenciais para que o site funcione corretamente. Eles permitem a navegação e o acesso a áreas seguras, como o login no sistema.</p>
        
        <h3>3.2 Cookies de Funcionalidade</h3>
        <p>Permitem que o site se lembre de escolhas que você fez (como seu nome de usuário ou idioma) e forneça recursos aprimorados e mais personalizados.</p>

        <h3>3.3 Cookies de Desempenho e Analíticos</h3>
        <p>Usados para coletar informações sobre como os visitantes utilizam nosso site (ex: Google Analytics). Esses cookies nos ajudam a identificar erros e melhorar o desempenho da plataforma.</p>

        <h3>3.4 Cookies de Terceiros (Stripe)</h3>
        <p>O Stripe utiliza cookies para fins de prevenção de fraudes e processamento seguro de pagamentos. Sem esses cookies, não poderíamos garantir a segurança financeira da sua conta.</p>
      </section>

      <section id="controle">
        <h2>4. Como você pode controlar os Cookies?</h2>
        <p>Você pode controlar e/ou excluir cookies como desejar através das configurações do seu navegador. Você pode excluir todos os cookies já instalados no seu computador e configurar a maioria dos navegadores para impedir sua instalação.</p>
        <p><strong>Atenção:</strong> Se você desativar todos os cookies, algumas funcionalidades da plataforma neritechauto (como o login automático) podem ser afetadas ou deixar de funcionar completamente.</p>
      </section>
    </template>
  </LayoutLegal>
</template>

<script setup>
import LayoutLegal from '../../components/LayoutLegal.vue';
</script>
