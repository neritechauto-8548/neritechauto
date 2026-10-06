/**
 * Mesh gradient estilo Stripe (canvas 2D).
 * Anima blobs de cor com blend soft-light — leve e sem WebGL.
 */
export function createStripeGradient(canvas, options = {}) {
  if (!canvas) return { destroy() {} };

  const colors = options.colors || [
    { r: 110, g: 195, b: 244 }, // cyan
    { r: 99, g: 91, b: 255 },   // stripe indigo
    { r: 255, g: 97, b: 171 },  // magenta
    { r: 255, g: 186, b: 39 },  // amber
  ];

  const ctx = canvas.getContext('2d', { alpha: false });
  let raf = 0;
  let running = true;
  let w = 0;
  let h = 0;
  let dpr = 1;
  const start = performance.now();

  const blobs = colors.map((c, i) => ({
    color: c,
    x: 0.2 + (i % 2) * 0.55,
    y: 0.25 + Math.floor(i / 2) * 0.45,
    rx: 0.45 + (i % 3) * 0.08,
    ry: 0.38 + (i % 2) * 0.1,
    speed: 0.00018 + i * 0.00004,
    phase: i * 1.7,
  }));

  function resize() {
    const rect = canvas.getBoundingClientRect();
    dpr = Math.min(window.devicePixelRatio || 1, 2);
    w = Math.max(1, Math.floor(rect.width));
    h = Math.max(1, Math.floor(rect.height));
    canvas.width = Math.floor(w * dpr);
    canvas.height = Math.floor(h * dpr);
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
  }

  function draw(now) {
    if (!running) return;
    const t = now - start;

    ctx.fillStyle = '#0a2540';
    ctx.fillRect(0, 0, w, h);

    // Base wash
    const base = ctx.createLinearGradient(0, 0, w, h);
    base.addColorStop(0, '#1a3a6b');
    base.addColorStop(0.45, '#4b3fd6');
    base.addColorStop(1, '#c23a8a');
    ctx.fillStyle = base;
    ctx.fillRect(0, 0, w, h);

    ctx.globalCompositeOperation = 'lighter';

    for (const blob of blobs) {
      const ox = Math.sin(t * blob.speed + blob.phase) * 0.12;
      const oy = Math.cos(t * blob.speed * 0.85 + blob.phase * 1.3) * 0.1;
      const cx = (blob.x + ox) * w;
      const cy = (blob.y + oy) * h;
      const rx = blob.rx * w;
      const ry = blob.ry * h;

      const g = ctx.createRadialGradient(cx, cy, 0, cx, cy, Math.max(rx, ry));
      const { r, g: gg, b } = blob.color;
      g.addColorStop(0, `rgba(${r},${gg},${b},0.85)`);
      g.addColorStop(0.45, `rgba(${r},${gg},${b},0.35)`);
      g.addColorStop(1, `rgba(${r},${gg},${b},0)`);

      ctx.fillStyle = g;
      ctx.beginPath();
      ctx.ellipse(cx, cy, rx, ry, Math.sin(t * blob.speed + blob.phase) * 0.4, 0, Math.PI * 2);
      ctx.fill();
    }

    ctx.globalCompositeOperation = 'source-over';

    // Soft vignette for Stripe depth
    const vig = ctx.createRadialGradient(w * 0.5, h * 0.35, 0, w * 0.5, h * 0.45, Math.max(w, h) * 0.75);
    vig.addColorStop(0, 'rgba(10,37,64,0)');
    vig.addColorStop(1, 'rgba(10,37,64,0.28)');
    ctx.fillStyle = vig;
    ctx.fillRect(0, 0, w, h);

    raf = requestAnimationFrame(draw);
  }

  const onResize = () => {
    resize();
  };

  resize();
  raf = requestAnimationFrame(draw);
  window.addEventListener('resize', onResize);

  return {
    destroy() {
      running = false;
      cancelAnimationFrame(raf);
      window.removeEventListener('resize', onResize);
    },
  };
}
