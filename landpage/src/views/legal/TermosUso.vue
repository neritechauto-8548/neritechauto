<template>
  <LayoutLegal title="Termos de Uso" lastUpdated="18 de Abril de 2026">
    <template #navigation>
      <a href="#aceitacao">1. Aceitação</a>
      <a href="#servicos">2. Descrição dos Serviços</a>
      <a href="#contas">3. Cadastro e Segurança</a>
      <a href="#assinaturas">4. Assinaturas e Pagamentos</a>
      <a href="#trial">5. Período de Teste</a>
      <a href="#proibido">6. Condutas Proibidas</a>
      <a href="#cancelamento">7. Cancelamento</a>
    </template>
    
    <template #content>
      <section id="aceitacao">
        <h2>1. Aceitação dos Termos</h2>
        <p>Ao acessar ou usar a plataforma NeriTechAuto, você concorda em cumprir e estar vinculado a estes Termos de Uso. Se você não concordar com qualquer parte destes termos, não deverá utilizar nossos serviços.</p>
      </section>

      <section id="servicos">
        <h2>2. Descrição dos Serviços</h2>
        <p>A NeriTechAuto oferece um software como serviço (SaaS) focado na gestão de oficinas mecânicas e estabelecimentos automotivos. Os recursos incluem, mas não se limitam a: abertura de O.S., gestão financeira, controle de estoque e emissão de notas fiscais.</p>
      </section>

      <section id="contas">
        <h2>3. Cadastro e Segurança das Contas</h2>
        <p>Para utilizar a NeriTechAuto, você deve criar uma conta fornecendo informações precisas e completas. Você é o único responsável por:</p>
        <ul>
          <li>Manter a confidencialidade de sua senha e conta;</li>
          <li>Todas as atividades que ocorrem sob sua conta;</li>
          <li>Notificar a NeriTechAuto imediatamente sobre qualquer uso não autorizado ou violação de segurança.</li>
        </ul>
      </section>

      <section id="assinaturas">
        <h2>4. Assinaturas e Pagamentos</h2>
        <p>A NeriTechAuto funciona sob o modelo de assinatura mensal. Os pagamentos são processados via Stripe.</p>
        <ul>
          <li><strong>Cobrança:</strong> As faturas são geradas no início de cada período de faturamento mensal.</li>
          <li><strong>Preços:</strong> Reservamo-nos o direito de alterar nossos planos de preços, notificando os usuários com pelo menos 30 dias de antecedência.</li>
          <li><strong>Impostos:</strong> Você é responsável por todos os impostos aplicáveis relacionados à sua assinatura.</li>
        </ul>
      </section>

      <section id="trial">
        <h2>5. Período de Teste (Free Trial)</h2>
        <p>Oferecemos um período de teste gratuito de 7 dias para novos usuários. Após esse período, o acesso será restrito a menos que uma assinatura paga seja ativada.</p>
      </section>

      <section id="proibido">
        <h2>6. Condutas Proibidas</h2>
        <p>Você concorda em não:</p>
        <ul>
          <li>Usar a plataforma para qualquer finalidade ilegal;</li>
          <li>Tentar obter acesso não autorizado a sistemas ou redes ligadas à NeriTech;</li>
          <li>Praticar engenharia reversa no software;</li>
          <li>Enviar spam ou conteúdo malicioso através da plataforma.</li>
        </ul>
      </section>

      <section id="cancelamento">
        <h2>7. Cancelamento e Devolução</h2>
        <p>Você pode cancelar sua assinatura a qualquer momento através do painel de administração. O cancelamento entrará em vigor no final do período de faturamento atual.</p>
        <p><strong>Reembolso:</strong> Dada a natureza do serviço digital e o período de teste gratuito, reembolsos para períodos já cobrados são analisados caso a caso, salvo disposição legal em contrário.</p>
      </section>
    </template>
  </LayoutLegal>
</template>

<script setup>
import LayoutLegal from '../../components/LayoutLegal.vue';
</script>
