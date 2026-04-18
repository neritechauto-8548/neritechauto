<template>
  <div class="foto-viewer">
    <div class="foto-grid">
      <div
        v-for="(foto, index) in fotos"
        :key="index"
        class="foto-thumbnail"
        @click="openLightbox(index)"
      >
        <img :src="foto.url || foto" :alt="`Foto ${index + 1}`" />
        <div class="foto-overlay">
          <i data-lucide="maximize-2"></i>
        </div>
      </div>
    </div>

    <!-- Lightbox Modal -->
    <Teleport to="body">
      <div v-if="lightboxOpen" class="lightbox" @click="closeLightbox">
        <div class="lightbox-content" @click.stop>
          <button class="lightbox-close" @click="closeLightbox">
            <i data-lucide="x"></i>
          </button>

          <button
            v-if="currentIndex > 0"
            class="lightbox-nav lightbox-prev"
            @click="previousImage"
          >
            <i data-lucide="chevron-left"></i>
          </button>

          <div class="lightbox-image-container">
            <img :src="currentImage" :alt="`Foto ${currentIndex + 1}`" />
          </div>

          <button
            v-if="currentIndex < fotos.length - 1"
            class="lightbox-nav lightbox-next"
            @click="nextImage"
          >
            <i data-lucide="chevron-right"></i>
          </button>

          <div class="lightbox-counter">
            {{ currentIndex + 1 }} / {{ fotos.length }}
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  fotos: {
    type: Array,
    default: () => [],
  },
});

const lightboxOpen = ref(false);
const currentIndex = ref(0);

const currentImage = computed(() => {
  const foto = props.fotos[currentIndex.value];
  return foto?.url || foto;
});

const openLightbox = (index) => {
  currentIndex.value = index;
  lightboxOpen.value = true;
  document.body.style.overflow = 'hidden';
  
  setTimeout(() => {
    if (window.lucide) {
      window.lucide.createIcons();
    }
  }, 0);
};

const closeLightbox = () => {
  lightboxOpen.value = false;
  document.body.style.overflow = '';
};

const nextImage = () => {
  if (currentIndex.value < props.fotos.length - 1) {
    currentIndex.value++;
  }
};

const previousImage = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--;
  }
};

const handleKeydown = (e) => {
  if (!lightboxOpen.value) return;
  
  if (e.key === 'Escape') {
    closeLightbox();
  } else if (e.key === 'ArrowRight') {
    nextImage();
  } else if (e.key === 'ArrowLeft') {
    previousImage();
  }
};

onMounted(() => {
  window.addEventListener('keydown', handleKeydown);
  if (window.lucide) {
    window.lucide.createIcons();
  }
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeydown);
  document.body.style.overflow = '';
});
</script>

<style scoped>
.foto-viewer {
  width: 100%;
}

.foto-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
}

.foto-thumbnail {
  position: relative;
  aspect-ratio: 1;
  border-radius: var(--radius);
  overflow: hidden;
  cursor: pointer;
  transition: var(--transition);
}

.foto-thumbnail:hover {
  transform: scale(1.05);
}

.foto-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.foto-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: var(--transition);
}

.foto-thumbnail:hover .foto-overlay {
  opacity: 1;
}

.foto-overlay i {
  width: 32px;
  height: 32px;
  color: var(--white);
}

/* Lightbox */
.lightbox {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.95);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  animation: fadeIn 0.3s ease-out;
}

.lightbox-content {
  position: relative;
  width: 90%;
  height: 90%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.lightbox-image-container {
  max-width: 100%;
  max-height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.lightbox-image-container img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  border-radius: var(--radius);
}

.lightbox-close {
  position: absolute;
  top: 1rem;
  right: 1rem;
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 50%;
  color: var(--white);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: var(--transition);
  z-index: 10;
}

.lightbox-close:hover {
  background: rgba(255, 255, 255, 0.2);
}

.lightbox-close i {
  width: 24px;
  height: 24px;
}

.lightbox-nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 50%;
  color: var(--white);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: var(--transition);
  z-index: 10;
}

.lightbox-nav:hover {
  background: rgba(255, 255, 255, 0.2);
}

.lightbox-nav i {
  width: 24px;
  height: 24px;
}

.lightbox-prev {
  left: 1rem;
}

.lightbox-next {
  right: 1rem;
}

.lightbox-counter {
  position: absolute;
  bottom: 1rem;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(255, 255, 255, 0.1);
  color: var(--white);
  padding: 0.5rem 1rem;
  border-radius: 9999px;
  font-size: 0.875rem;
  font-weight: 500;
}

@media (max-width: 768px) {
  .foto-grid {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
    gap: 0.5rem;
  }
  
  .lightbox-nav {
    width: 40px;
    height: 40px;
  }
  
  .lightbox-nav i {
    width: 20px;
    height: 20px;
  }
}
</style>
