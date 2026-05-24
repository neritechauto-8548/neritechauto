<template>
  <div class="app-wrapper">
    <CabecalhoNav />
    <router-view />
    <ToastContainer />
  </div>
</template>

<script setup>
import CabecalhoNav from './components/CabecalhoNav.vue';
import ToastContainer from './components/ToastContainer.vue';
</script>

<style>
/* Global reset that affects router-view transitions */
.app-wrapper {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>