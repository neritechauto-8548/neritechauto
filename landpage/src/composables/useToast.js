import { ref } from 'vue';

const toasts = ref([]);

export function useToast() {
  const showToast = (message, type = 'success', duration = 5000) => {
    const id = Date.now();
    const toast = { id, message, type };
    
    toasts.value.push(toast);

    setTimeout(() => {
      removeToast(id);
    }, duration);
  };

  const removeToast = (id) => {
    toasts.value = toasts.value.filter(t => t.id !== id);
  };

  const success = (msg, duration) => showToast(msg, 'success', duration);
  const error = (msg, duration) => showToast(msg, 'error', duration);
  const info = (msg, duration) => showToast(msg, 'info', duration);

  return {
    toasts,
    success,
    error,
    info,
    removeToast
  };
}
