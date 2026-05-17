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
  background: var(--primary);
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform var(--transition-base), background var(--transition-base);
}

.logo-symbol__letter {
  font-family: var(--font-heading);
  font-weight: 800;
  color: white;
  line-height: 1;
  letter-spacing: -0.04em;
}

.logo-marca--sm .logo-symbol { width: 28px; height: 28px; border-radius: 8px; }
.logo-marca--sm .logo-symbol__letter { font-size: 0.875rem; }

.logo-marca--md .logo-symbol { width: 34px; height: 34px; }
.logo-marca--md .logo-symbol__letter { font-size: 1rem; }

.logo-marca--lg .logo-symbol { width: 40px; height: 40px; border-radius: 10px; }
.logo-marca--lg .logo-symbol__letter { font-size: 1.125rem; }

.logo-marca:hover .logo-symbol {
  transform: scale(1.03);
  background: var(--primary-dark);
}

.logo-wordmark {
  font-family: var(--font-heading);
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.04em;
  line-height: 1;
}

.logo-marca--sm .logo-wordmark { font-size: 1.0625rem; }
.logo-marca--md .logo-wordmark { font-size: 1.2rem; }
.logo-marca--lg .logo-wordmark { font-size: 1.375rem; }

.logo-wordmark__accent { color: var(--primary); }
.logo-wordmark__suffix { font-weight: 700; color: var(--text-muted); }
</style>
