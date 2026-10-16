<template>
  <router-link v-if="!href" :to="to" class="logo-marca" :class="`logo-marca--${size}`">
    <div class="logo-symbol" aria-hidden="true">
      <span class="logo-symbol__letter">N</span>
    </div>
    <span v-if="showWordmark" class="logo-wordmark">
      Neri<span class="logo-wordmark__accent">Tech</span><span class="logo-wordmark__suffix"> Auto</span>
    </span>
  </router-link>
  <a v-else :href="href" class="logo-marca" :class="`logo-marca--${size}`">
    <div class="logo-symbol" aria-hidden="true">
      <span class="logo-symbol__letter">N</span>
    </div>
    <span v-if="showWordmark" class="logo-wordmark">
      Neri<span class="logo-wordmark__accent">Tech</span><span class="logo-wordmark__suffix"> Auto</span>
    </span>
  </a>
</template>

<script setup>
defineProps({
  size: { type: String, default: 'md' },
  showWordmark: { type: Boolean, default: true },
  href: { type: String, default: null },
  to: { type: String, default: '/' },
});
</script>

<style scoped>
.logo-marca {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  flex-shrink: 0;
}

.logo-symbol {
  flex-shrink: 0;
  background: #635BFF;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s ease, background 0.2s ease;
}

.logo-symbol__letter {
  font-family: var(--font-heading);
  font-weight: 600;
  color: white;
  line-height: 1;
  letter-spacing: -0.04em;
}

.logo-marca--sm .logo-symbol { width: 26px; height: 26px; border-radius: 5px; }
.logo-marca--sm .logo-symbol__letter { font-size: 0.8125rem; }

.logo-marca--md .logo-symbol { width: 28px; height: 28px; }
.logo-marca--md .logo-symbol__letter { font-size: 0.875rem; }

.logo-marca--lg .logo-symbol { width: 34px; height: 34px; border-radius: 7px; }
.logo-marca--lg .logo-symbol__letter { font-size: 1rem; }

.logo-marca:hover .logo-symbol {
  transform: scale(1.03);
  background: #7A73FF;
}

.logo-wordmark {
  font-family: var(--font-heading);
  font-weight: 600;
  color: #0A2540;
  letter-spacing: -0.035em;
  line-height: 1;
  transition: color 0.2s ease;
}

.logo-marca--sm .logo-wordmark { font-size: 1rem; }
.logo-marca--md .logo-wordmark { font-size: 1.0625rem; }
.logo-marca--lg .logo-wordmark { font-size: 1.25rem; }

.logo-wordmark__accent {
  color: #635BFF;
  transition: color 0.2s ease;
}
.logo-wordmark__suffix {
  font-weight: 500;
  color: #425466;
  transition: color 0.2s ease;
}
</style>
