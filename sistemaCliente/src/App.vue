<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue';

onMounted(() => {
  // Inicializar ícones Lucide em toda a aplicação
  if (window.lucide) {
    window.lucide.createIcons();
  }
});
</script>

<style>
/* Estilos globais já estão em style.css */
</style>
