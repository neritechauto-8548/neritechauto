<template>
  <LayoutLegal title="Data Processing Agreement (DPA)" lastUpdated="18 de Abril de 2026">
    <template #navigation>
      <a href="#objeto">1. Objeto e Escopo</a>
      <a href="#obrigacoes">2. Obrigações do Operador</a>
      <a href="#suboperadores">3. Suboperadores</a>
      <a href="#seguranca">4. Medidas de Segurança</a>
      <a href="#incidentes">5. Incidentes de Dados</a>
      <a href="#transferencia">6. Transferência Internacional</a>
    </template>
    
    <template #content>
      <section id="objeto">
        <h2>1. Objeto e Escopo</h2>
        <p>Este Acordo de Processamento de Dados (DPA) aplica-se sempre que a NeriTechAuto atua como "Operador" (Processor) de dados pessoais em nome do Cliente, que atua como "Controlador" (Controller) nos termos da LGPD.</p>
        <p>Este documento regula as condições de segurança e proteção dos dados dos clientes da sua oficina que são inseridos em nossa plataforma.</p>
      </section>

      <section id="obrigacoes">
        <h2>2. Obrigações da NeriTechAuto (Operador)</h2>
        <p>A NeriTechAuto compromete-se a:</p>
        <ul>
          <li>Processar dados pessoais apenas conforme as instruções documentadas do Cliente (através do uso do sistema);</li>
          <li>Garantir que as pessoas autorizadas a processar os dados pessoais tenham se comprometido com a confidencialidade;</li>
          <li>Auxiliar o Cliente no cumprimento de suas obrigações perante os Titulares de Dados;</li>
          <li>Disponibilizar ao Cliente as informações necessárias para demonstrar conformidade com as obrigações da LGPD.</li>
        </ul>
      </section>

      <section id="suboperadores">
        <h2>3. Suboperadores</h2>
        <p>O Cliente autoriza a NeriTech a contratar suboperadores para funções específicas da plataforma (ex: hospedagem em nuvem). Atualmente, nossos principais suboperadores são:</p>
        <ul>
          <li><strong>Amazon Web Services (AWS):</strong> Infraestrutura de servidores e banco de dados.</li>
          <li><strong>Stripe, Inc.:</strong> Processamento de pagamentos.</li>
        </ul>
      </section>

      <section id="seguranca">
        <h2>4. Medidas de Segurança</h2>
        <p>Manteremos medidas técnicas e organizacionais adequadas para proteger os dados pessoais contra destruição acidental ou ilícita, perda acidental, alteração, divulgação ou acesso não autorizado.</p>
      </section>

      <section id="incidentes">
        <h2>5. Incidentes de Dados</h2>
        <p>Em caso de incidente de segurança que afete os dados pessoais processados, a NeriTech notificará o Cliente sem demora injustificada após tomar conhecimento do ocorrido, fornecendo detalhes suficientes para que o Cliente possa cumprir suas obrigações de notificação perante a ANPD.</p>
      </section>

      <section id="transferencia">
        <h2>6. Transferência Internacional</h2>
        <p>O Cliente reconhece que o uso de provedores de infraestrutura global (como AWS) pode envolver a transferência de dados para fora do território nacional. A NeriTech garante que tais transferências ocorrem seguindo mecanismos legais de conformidade internacional aceitos pela LGPD.</p>
      </section>
    </template>
  </LayoutLegal>
</template>

<script setup>
import LayoutLegal from '../../components/LayoutLegal.vue';
</script>
