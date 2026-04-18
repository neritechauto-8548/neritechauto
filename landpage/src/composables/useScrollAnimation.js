import { onMounted, onUnmounted } from 'vue'

/**
 * Animate elements with class "aos-init" when they enter the viewport.
 * Adds "aos-visible" class to trigger CSS transitions.
 */
export function useScrollAnimation() {
  let observer = null

  const activateObserver = () => {
    observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('aos-visible')
            observer.unobserve(entry.target)
          }
        })
      },
      { threshold: 0.1, rootMargin: '0px 0px -40px 0px' }
    )

    document.querySelectorAll('.aos-init').forEach((el) => {
      observer.observe(el)
    })
  }

  onMounted(() => {
    // Small delay so Vue has finished rendering all child components
    setTimeout(activateObserver, 100)
  })

  onUnmounted(() => {
    if (observer) observer.disconnect()
  })
}
