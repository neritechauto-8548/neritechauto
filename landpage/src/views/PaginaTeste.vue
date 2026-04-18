<template>
  <div class="trial-view">
    <SecaoFormularioTeste />
    <SecaoRodape />
  </div>
</template>

<script setup>
import SecaoFormularioTeste from '../components/SecaoFormularioTeste.vue';
import SecaoRodape from '../components/SecaoRodape.vue';
</script>

<style scoped>
.trial-view {
  display: flex;
  flex-direction: column;
}
</style>
